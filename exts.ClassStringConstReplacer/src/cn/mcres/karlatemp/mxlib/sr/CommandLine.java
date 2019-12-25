/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: CommandLine.java@author: karlatemp@vip.qq.com: 19-12-21 下午12:08@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.sr;

import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class CommandLine {
    static final PrintStream cout = System.out;
    static final PrintStream err = System.err;
    private static OutputStream os = null;
    private static InputStream input;
    private static String process, file;
    private static final PrintWriter pw;
    private static final List<String> pallow = new ArrayList<>(), pdisable = new ArrayList<>();

    static {
        Logging.log("Copyright", Level.INFO, "#ClassStringConstReplacer v" + CommandLine.class.getPackage().getImplementationVersion() + " by Karlatemp.\nAll rights reserved.");
    }

    static {
        final OutputStream ow = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                if (os != null) os.write(b);
            }

            @Override
            public void write(@NotNull byte[] b) throws IOException {
                if (os != null) os.write(b);
            }

            @Override
            public void write(@NotNull byte[] b, int off, int len) throws IOException {
                if (os != null) os.write(b, off, len);
            }
        };
        pw = new PrintWriter(new OutputStreamWriter(ow, StandardCharsets.UTF_8));
    }

    private static class Filter implements StringReplacer {
        StringReplacer sup;
        boolean print;

        private boolean a(String c) {
            if (!pdisable.isEmpty()) {
                for (String p : pdisable) {
                    if (c.startsWith(p)) return true;
                }
            }
            if (pallow.isEmpty()) return false;
            for (String a : pallow) {
                if (c.startsWith(a)) return false;
            }
            return true;
        }

        @Override
        public String replaceField(@NotNull String className, @NotNull String fieldName, @NotNull String defaultValue) {
            if (a(className)) return null;
            String s = sup.replaceField(className, fieldName, defaultValue);
            if (print && s != null) cout.println(className + "." + fieldName + ": " + s);
            return s;
        }

        @Override
        public String replaceMethod(@NotNull String className, @NotNull String method, @NotNull String method_desc, @NotNull String defaultValue, int index) {
            if (a(className)) return null;
            String s = sup.replaceMethod(className, method, method_desc, defaultValue, index);
            if (print && s != null) cout.println(className + "." + method + method_desc + "[" + index + "]: " + s);
            return s;
        }
    }

    public static Map<String, Map<String, Object>> loadMappings(File f) throws IOException {
        try (FileInputStream fis = new FileInputStream(f)) {
            try (InputStreamReader ir = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
                return new GsonBuilder().disableHtmlEscaping().create().fromJson(
                        ir, (Type) Map.class
                );
            }
        }
    }

    public static void main(String[] args) throws Throwable {
        Thread.setDefaultUncaughtExceptionHandler((thread, error) -> {
            Logging.error(thread.getName(), Level.SEVERE, null, error);
        });
        if (args.length == 0) {
            dumpHelp();
            return;
        }
        ClassesInfo mappings = new ClassesInfo().initialize();
        root:
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            switch (arg) {
                case "-preview": {
                    mappings.load(loadMappings(new File(args[++i])));
                    Logging.log("Main", Level.INFO, "main.preload.mapping", args[i]);
                    break;
                }
                case "-output": {
                    File f = new File(args[++i]);
                    new File(f, "..").mkdirs();
                    f.createNewFile();
                    os = new NioFileOutputStream(new RandomAccessFile(f, "rw"));
                    Logging.log("Main", Level.INFO, "main.output.set", args[i]);
                    break;
                }
                case "-process": {
                    process = args[++i];
                    Logging.log("Main", Level.INFO, "main.process", args[i]);
                    break;
                }
                case "-hidden": {
                    pdisable.add(args[++i].replace('.', '/'));
                    Logging.log("Main", Level.INFO, "main.hidden", args[i]);
                    break;
                }
                case "-only": {
                    pallow.add(args[++i].replace('.', '/'));
                    Logging.log("Main", Level.INFO, "main.only", args[i]);
                    break;
                }
                case "-help":
                case "-?": {
                    dumpHelp();
                    return;
                }
                default: {
                    file = arg;
                    Logging.log("Main", Level.INFO, "main.input.set", file);
                    break root;
                }
            }
        }
        if (file == null) dumpHelp();
        if (os == null) {
            Logging.log("Main", Level.WARNING, "main.output.unset");
            os = cout;
        }

        Filter f = new Filter();
        AtomicInteger files = new AtomicInteger(), classes = new AtomicInteger();
        Timer t = new Timer("Process Dump", true);
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                Logging.log("Main", Level.INFO, "process.running", classes.get(), files.get());
            }
        }, 1000, 1000);
        ClassStringReplacer sr = new ClassStringReplacer(Opcodes.ASM7, f);
        try (ZipInputStream zip = new ZipInputStream(new FileInputStream(file))) {
            InputStream is = new FilterInputStream(zip) {
                @Override
                public void close() {
                }
            };
            if (process == null) {
                Logging.log("Main", Level.INFO, "main.gen.mapping");
                ClassesInfo ci = new ClassesInfo().initialize();
                f.sup = ci;
                ClassesInfo copyed = mappings.copy();
                do {
                    try {
                        final ZipEntry entry = zip.getNextEntry();
                        if (entry == null) break;
                        if (entry.isDirectory()) continue;
                        try {
                            ClassReader reader = new ClassReader(is);
                            reader.accept(new ClassesInfoPreChecker(Opcodes.ASM7, copyed), 0);
                            reader.accept(sr, 0);
                            classes.getAndIncrement();
                        } catch (Throwable ignore) {
                        }
                    } finally {
                        files.getAndIncrement();
                    }
                } while (true);
                t.cancel();
                Logging.log("Main", Level.INFO, "writing.mapping");
                ci.override(mappings);
                new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()
                        .toJson(ci.toMap(), pw);
                pw.flush();
            } else {
                ClassesInfo ci = new ClassesInfo(
                        loadMappings(new File(process))
                ).override(mappings);
                Logging.log("Main", Level.INFO, "main.jar.mapping");
                f.sup = ci.newProcessor();
                f.print = false;
                try (ZipOutputStream zos = new ZipOutputStream(os)) {

                    do {
                        try {
                            final ZipEntry entry = zip.getNextEntry();
                            if (entry == null) break;
                            ZipEntry ze = new ZipEntry(entry.getName());
                            {
                                Optional.ofNullable(entry.getCreationTime()).ifPresent(ze::setCreationTime);
                                Optional.ofNullable(entry.getLastAccessTime()).ifPresent(ze::setLastAccessTime);
                                Optional.ofNullable(entry.getLastModifiedTime()).ifPresent(ze::setLastModifiedTime);
                                Optional.ofNullable(entry.getComment()).ifPresent(ze::setComment);
                                ze.setTime(entry.getTime());
                            }
                            zos.putNextEntry(ze);

                            if (entry.isDirectory()) continue;
                            byte[] data;
                            try {
                                ByteArrayOutputStream os = new ByteArrayOutputStream(Math.max(200, (int) Math.max(entry.getSize(), entry.getCompressedSize())));
                                Toolkit.IO.writeTo(is, os);
                                data = os.toByteArray();
                            } catch (Throwable wt) {
                                Toolkit.IO.writeTo(is, zos);
                                continue;
                            }

                            try {
                                ClassWriter cw = new ClassWriter(0);
                                ClassReader reader = new ClassReader(new ByteArrayInputStream(data));
                                reader.accept(new ClassesInfoPreChecker(Opcodes.ASM7, ci), 0);
                                reader.accept(new ClassStringReplacer(Opcodes.ASM7, cw, f), 0);
                                classes.getAndIncrement();
                                zos.write(cw.toByteArray());
                                continue;
                            } catch (Throwable ignore) {
                            }
                            Toolkit.IO.writeTo(new ByteArrayInputStream(data), zos);
                        } finally {
                            files.getAndIncrement();
                        }
                    } while (true);
                    t.cancel();
                    Logging.log("Main", Level.INFO, "writing.mapped");
                }

            }
        }
        if (os != cout)
            os.close();
        Logging.log("Main", Level.INFO, "process.finish", classes.get(), files.get());
    }

    private static void dumpHelp() {
        cout.println("Class String Const Replacer");
        cout.println();
        cout.println("java -classpath [libraries...] -jar ClassStringConstReplacer.jar [options] [jarFile]");
        cout.println();
        cout.println("    -preload [file]             Preload mapping file.");
        cout.println("    -output [file]              Set output location");
        cout.println("    -hidden [package]           Skip package.");
        cout.println("    -only [package]             Process input packages only.");
        cout.println("    -process [Mapping table]    Use mapping table and join replace mode..");
        System.exit(5);
    }
}

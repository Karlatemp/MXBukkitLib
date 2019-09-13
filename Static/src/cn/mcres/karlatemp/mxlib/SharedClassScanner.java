package cn.mcres.karlatemp.mxlib;

import cn.mcres.karlatemp.mxlib.exceptions.ScanException;
import cn.mcres.karlatemp.mxlib.tools.CharCompiler;
import cn.mcres.karlatemp.mxlib.tools.IClassScanner;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class SharedClassScanner implements IClassScanner {

    private static String dump(String a) {
        if (a == null) return "";
        while (true) {
            if (a.length() > 0) {
                if (a.charAt(0) == '/') {
                    a = a.substring(1);
                    continue;
                }
            }
            break;
        }
        return a;
    }

    @NotNull
    @Override
    public List<String> scan(@NotNull File file, @NotNull List<String> list) throws ScanException {
        if (file.isFile()) {
            try {
                try (FileInputStream clazz = new FileInputStream(file)) {
                    ClassReader reader = new ClassReader(clazz);
                    list.add(replace(reader.getClassName(), false));
                } catch (Exception ioe0) {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        try (ZipInputStream zip = new ZipInputStream(fis)) {
                            ZipEntry entry;
                            while ((entry = zip.getNextEntry()) != null) {
                                if (!entry.isDirectory()) {
                                    final String name = dump(entry.getName());
                                    if (name.endsWith(".class")) {
                                        if (!name.startsWith("META-INF")) {
                                            list.add(replace(name, true));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (IOException ioe) {
                throw new ScanException(ioe.toString(), ioe);
            }
        } else if (file.isDirectory()) {
            ScanException top = null;
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    try {
                        scan(f, list);
                    } catch (ScanException se) {
                        if (top == null) {
                            top = se;
                        } else {
                            top.addSuppressed(se);
                        }
                    }
                }
            }
            if (top != null) {
                throw top;
            }
        }
        return list;
    }

    private static String replace(String name, boolean a) {
        char[] set = name.toCharArray();
        if (a) {
            char[] nw = new char[set.length - 6];
            System.arraycopy(set, 0, nw, 0, nw.length);
            set = nw;
        }
        for (int i = 0; i < set.length; i++) {
            if (set[i] == '/') {
                set[i] = '.';
            }
        }
        return new String(set);
    }

    @NotNull
    @Override
    public List<String> scan(@NotNull Class c, @NotNull List<String> list) throws ScanException {
        final URL resource = c.getResource(c.getSimpleName() + ".class");
        if (resource != null) {
            return scan(resource, list);
        }
        return list;
    }

    @NotNull
    @Override
    public List<String> scan(@NotNull URL url, @NotNull List<String> list) throws ScanException {
        final String protocol = url.getProtocol();
        switch (protocol.toLowerCase()) {
            case "file": {
                File file = new File(url.getFile());
                if (file.exists()) {
                    if (file.isFile()) {
                        file = new File(file, "..");
                    }
                    return scan(file, list);
                }
                break;
            }
            case "jar": {
                String path = url.getPath();
                if (path.startsWith("file:")) {
                    String file = path.substring(5);
                    int end = file.indexOf('!');
                    String cut, pck;
                    if (end == -1) {
                        cut = file;
                        pck = "";
                    } else {
                        cut = file.substring(0, end);
                        pck = file.substring(end + 1);
                    }
                    if (pck.endsWith(".class")) {
                        int a = pck.lastIndexOf('/');
                        if (a == -1) {
                            pck = "";
                        } else {
                            pck = pck.substring(0, a + 1);
                        }
                    }
                    pck = dump(pck);
                    cut = CharCompiler.decode('%', cut, StandardCharsets.UTF_8);
                    File jar = new File(cut);
                    if (jar.isFile()) {
                        try (FileInputStream fis = new FileInputStream(jar)) {
                            try (ZipInputStream zis = new ZipInputStream(fis)) {
                                ZipEntry ze;
                                while ((ze = zis.getNextEntry()) != null) {
                                    if (ze.isDirectory()) continue;
                                    String name = ze.getName();
                                    if (name.endsWith(".class")) {
                                        if (dump(name).startsWith(pck)) {
                                            list.add(replace(new ClassReader(zis).getClassName(), false));
                                        }
                                    }
                                }
                            }
                        } catch (IOException ioe) {
                            throw new ScanException(ioe.toString(), ioe);
                        }
                    }
                }
                break;
            }
        }
        return list;
    }
}

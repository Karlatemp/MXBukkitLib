/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: JarEdit.java@author: karlatemp@vip.qq.com: 2019/12/25 下午2:04@version: 2.0
 */

package cn.mcres.karlatemp.mxlib;

import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import org.objectweb.asm.*;

import java.io.*;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class JarEdit {
    private static final ConcurrentLinkedQueue<File> temps = new ConcurrentLinkedQueue<>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            for (File f : temps) {
                f.delete();
            }
        }));
    }

    public static void main(String... args) throws Throwable {
        for (String s : args) {
            mk(new File(s));
        }
    }

    public static void mk(File f) throws IOException {
        File out = new File("dist", f.getName() + ".tmp");
        out.createNewFile();
        temps.add(out);
        try (var fos = new FileOutputStream(out)) {
            try (var fis = new FileInputStream(f)) {
                try (var out_zip = new ZipOutputStream(fos)) {
                    try (var input_zip = new ZipInputStream(fis)) {
                        String FACTORY_PRE = "cn/mcres/karlatemp/mxlib/Java9ToJava8/";
                        String classname = FACTORY_PRE + UUID.randomUUID().toString().replace('-', '_') + "/StringFactory";
                        {
                            ZipEntry ze = new ZipEntry(classname + ".class");
                            out_zip.putNextEntry(ze);
                            out_zip.write(StringFactoryCreator.dump(classname, Opcodes.V1_8));
                        }
                        final String FACTORY_CLASS_NAME = classname;
                        do {
                            final ZipEntry entry = input_zip.getNextEntry();
                            if (entry == null) break;
                            if (entry.getName().startsWith(FACTORY_PRE)) continue;
                            if (entry.isDirectory()) {
                                out_zip.putNextEntry(entry);
                                continue;
                            } else {
                                ZipEntry copy = new ZipEntry(entry.getName());
                                Optional.ofNullable(entry.getLastModifiedTime()).ifPresent(copy::setLastModifiedTime);
                                Optional.ofNullable(entry.getLastAccessTime()).ifPresent(copy::setLastAccessTime);
                                Optional.ofNullable(entry.getTimeLocal()).ifPresent(copy::setTimeLocal);
                                Optional.ofNullable(entry.getCreationTime()).ifPresent(copy::setCreationTime);
                                Optional.ofNullable(entry.getComment()).ifPresent(copy::setComment);
                                out_zip.putNextEntry(entry);
                            }
                            if (entry.getName().endsWith(".class") && !entry.getName().equals("module-info.class")) {
                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                Toolkit.IO.writeTo(input_zip, bos);
                                byte[] data = bos.toByteArray();
                                ClassWriter cw = new ClassWriter(0);
                                new ClassReader(data).accept(new ClassVisitor(Opcodes.ASM7, cw) {
                                    @Override
                                    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                                        if (name.equals("module-info"))
                                            super.visit(version, access, name, signature, superName, interfaces);
                                        else
                                            super.visit(Math.min(52, version), access, name, signature, superName, interfaces);
                                    }

                                    @Override
                                    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                                        return new MethodVisitor(api, super.visitMethod(access, name, descriptor, signature, exceptions)) {
                                            @Override
                                            public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
                                                if (name.equals("makeConcatWithConstants")) {
                                                    if (bootstrapMethodHandle.getOwner().equals("java/lang/invoke/StringConcatFactory") ||
                                                            (bootstrapMethodHandle.getOwner().startsWith(FACTORY_PRE) && bootstrapMethodHandle.getOwner().endsWith("StringFactory"))) {
                                                        super.visitInvokeDynamicInsn(
                                                                "makeConcatWithConstants",
                                                                descriptor,
                                                                new Handle(Opcodes.H_INVOKESTATIC,
                                                                        FACTORY_CLASS_NAME,
                                                                        "makeConcatWithConstants",
                                                                        "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite;",
                                                                        false
                                                                ), bootstrapMethodArguments);
                                                        return;
                                                    }
                                                }
                                                super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
                                            }
                                        };
                                    }
                                }, 0);
                                out_zip.write(cw.toByteArray());
                            } else {
                                Toolkit.IO.writeTo(input_zip, out_zip);
                            }
                        } while (true);
                    }
                }
            }
        }
        f.delete();
        out.renameTo(f);
    }
}

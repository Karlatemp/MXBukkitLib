/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: ByteCodeDumpr.java@author: karlatemp@vip.qq.com: 2020/1/4 上午12:08@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.ext;

import org.objectweb.asm.ClassReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.jar.JarEntry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ByteCodeDumpr {
    public static File output;
    public static final Map<ClassLoader, RandomAccessFile> zos = new HashMap<>();

    public static void premain(String opt, Instrumentation vm) throws Throwable {
        if (opt == null || opt.isEmpty()) {
            opt = "./codes";
        }
        output = new File(opt);
        output.mkdirs();
        Class.forName("java.io.IOException");
        Class.forName("java.io.RandomAccessFile", true, ClassLoader.getSystemClassLoader());
        vm.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(
                    ClassLoader loader,
                    String className,
                    Class<?> classBeingRedefined,
                    ProtectionDomain protectionDomain,
                    byte[] classfileBuffer) throws IllegalClassFormatException {
                if (loader == null) return classfileBuffer;
                RandomAccessFile get = getFile(loader);
                if (get != null) {
                    try {
                        get.writeShort(classfileBuffer.length);
                        get.write(classfileBuffer);
                    } catch (IOException ignore) {
                    }
                }
                return classfileBuffer;
            }
        });
    }

    public static RandomAccessFile getFile(ClassLoader loader) {
        final RandomAccessFile file = zos.get(loader);
        if (file != null) {
            return file;
        }
        File output = new File(ByteCodeDumpr.output, loader.getClass().getName() + "-" + Long.toHexString(
                System.identityHashCode(loader)
        ));
        try {
            output.createNewFile();
            RandomAccessFile raf = new RandomAccessFile(output, "rw");
            zos.put(loader, raf);
            return raf;
        } catch (IOException e) {
            return null;
        }
    }

    @SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
    public static void main(String[] args) throws Throwable {
        String path = args.length == 0 ? new Scanner(System.in).nextLine() : args[0];
        File out = new File("out/vm");
        out.mkdirs();
        for (File f : new File(path).listFiles()) {
            File jar = new File(out, f.getName() + ".jar");
            f.createNewFile();
            try (FileOutputStream fos = new FileOutputStream(jar)) {
                try (ZipOutputStream zip = new ZipOutputStream(fos)) {
                    try (RandomAccessFile raf = new RandomAccessFile(f, "r")) {
                        while (raf.getFilePointer() < raf.length()) {
                            int size = raf.readUnsignedShort();
                            byte[] clazz = new byte[size];
                            raf.readFully(clazz);
                            zip.putNextEntry(new ZipEntry(new ClassReader(clazz).getClassName() + ".class"));
                            zip.write(clazz);
                            zip.flush();
                        }
                    } catch (Throwable ioe) {
                        ioe.printStackTrace();
                    }
                }
            }
        }
    }
}

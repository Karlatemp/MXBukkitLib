/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: EmptyJar.java@author: karlatemp@vip.qq.com: 2020/1/24 下午4:23@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.plugin_class_definer;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.jar.JarOutputStream;

public class EmptyJar {
    private static String path = "plugins/MXBukkitLib/EmptyJar.jar";

    public static String getPath() {
        return path;
    }

    static {
        try {
            new File("plugins/MXBukkitLib").mkdirs();
            new File(path).createNewFile();
            RandomAccessFile raf = new RandomAccessFile(path, "rw");
            //noinspection EmptyTryBlock
            try (var JarOut = new JarOutputStream(new BufferedOutputStream(new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    raf.write(b);
                }

                @Override
                public void write(@NotNull byte[] b) throws IOException {
                    raf.write(b);
                }

                @Override
                public void write(@NotNull byte[] b, int off, int len) throws IOException {
                    raf.write(b, off, len);
                }

                @Override
                public void close() throws IOException {
                    raf.setLength(raf.getFilePointer());
                    raf.close();
                }
            }))) {
            }
        } catch (IOException ioe) {
            throw new ExceptionInInitializerError(ioe);
        }
    }
}

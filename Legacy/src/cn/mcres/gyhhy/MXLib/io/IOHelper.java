/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: IOHelper.java@author: karlatemp@vip.qq.com: 19-9-11 下午1:13@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.io;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;

public class IOHelper {

    public static long writeTo(@NotNull InputStream is, @NotNull OutputStream os) throws IOException {
        return writeTo(is, os, null);
    }

    public static long writeTo(@NotNull InputStream is, @NotNull OutputStream os, @Nullable byte[] buffer) throws IOException {
        return writeTo(is, os, buffer, 0);
    }

    public static long writeTo(@NotNull InputStream is, @NotNull OutputStream os, @Nullable byte[] buffer, long length) throws IOException {
        long buf = 0;
        if (buffer == null) {
            buffer = new byte[1024];
        }
        if (length == 0) {
            while (true) {
                int leng = is.read(buffer);
                if (leng == -1) {
                    break;
                }
                os.write(buffer, 0, leng);
                buf += leng;
            }
        } else {
            final int bl = buffer.length;
            while (length > 0) {
                int leng = is.read(buffer, 0, Math.max(0, Math.min((int) length, bl)));
                if (leng == -1) {
                    break;
                }
                os.write(buffer, 0, leng);
                buf += leng;
                length -= leng;
            }
        }
        return buf;
    }

}

/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: IOHelper.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.io;

import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;

public class IOHelper {

    public static long writeTo(@NotNull InputStream is, @NotNull OutputStream os) throws IOException {
        return Toolkit.IO.writeTo(is, os);
    }

    public static long writeTo(@NotNull InputStream is, @NotNull OutputStream os, @Nullable byte[] buffer) throws IOException {
        return Toolkit.IO.writeTo(is, os, buffer);
    }

    public static long writeTo(@NotNull InputStream is, @NotNull OutputStream os, @Nullable byte[] buffer, long length) throws IOException {
        return Toolkit.IO.writeTo(is, os, buffer, length);
    }

}

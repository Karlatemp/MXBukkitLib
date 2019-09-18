/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Channels.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.io;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public class Channels {

    public static OutputStream newOutputStream(AsynchronousByteChannel ch) {
        if (ch instanceof OutputStream) {
            return (OutputStream) ch;
        }
        return java.nio.channels.Channels.newOutputStream(ch);
    }

    public static OutputStream newOutputStream(WritableByteChannel ch) {
        if (ch instanceof OutputStream) {
            return (OutputStream) ch;
        }
        return java.nio.channels.Channels.newOutputStream(ch);
    }

    public static InputStream newInputStream(AsynchronousByteChannel ch) {
        if (ch instanceof InputStream) {
            return (InputStream) ch;
        }
        return java.nio.channels.Channels.newInputStream(ch);
    }

    public static InputStream newInputStream(ReadableByteChannel ch) {
        if (ch instanceof InputStream) {
            return (InputStream) ch;
        }
        return java.nio.channels.Channels.newInputStream(ch);
    }

    public static ReadableByteChannel newChannel(InputStream is) {
        if (is instanceof ReadableByteChannel) {
            return (ReadableByteChannel) is;
        }
        return java.nio.channels.Channels.newChannel(is);
    }

    public static WritableByteChannel newChannel(OutputStream is) {
        if (is instanceof ReadableByteChannel) {
            return (WritableByteChannel) is;
        }
        return java.nio.channels.Channels.newChannel(is);
    }

    private Channels() {
    }
}

/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: InlinePrintStream.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.logging;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

abstract class InlinePrintStream extends java.io.PrintStream {
    static OutputStream EO = new OutputStream() {
        @Override
        public void write(int b) throws IOException {
        }

        @Override
        public void write(@NotNull byte[] b) throws IOException {
        }

        @Override
        public void write(@NotNull byte[] b, int off, int len) throws IOException {
        }

        @Override
        public void flush() throws IOException {
        }

        @Override
        public void close() throws IOException {
        }
    };

    protected InlinePrintStream(OutputStream out) {
        super(out);
    }

    protected InlinePrintStream() {
        this(EO);
    }

    @Override
    public java.io.PrintStream format(@NotNull String format, Object... args) {
        print(String.format(format, args));
        return this;
    }

    @Override
    public java.io.PrintStream format(Locale l, @NotNull String format, Object... args) {
        print(String.format(l, format, args));
        return this;
    }

    @Override
    public void print(Object obj) {
        print(String.valueOf(obj));
    }

    @Override
    public abstract void print(String s);

    @Override
    public void print(boolean b) {
        print(String.valueOf(b));
    }

    @Override
    public void print(char c) {
        print(String.valueOf(c));
    }

    @Override
    public void print(char[] s) {
        print(String.valueOf(s));
    }

    @Override
    public void print(double d) {
        print(String.valueOf(d));
    }

    @Override
    public void print(float f) {
        print(String.valueOf(f));
    }

    @Override
    public void print(int i) {
        print(String.valueOf(i));
    }

    @Override
    public void print(long l) {
        print(String.valueOf(l));
    }

    @Override
    public java.io.PrintStream printf(@NotNull String format, Object... args) {
        return format(format, args);
    }

    @Override
    public java.io.PrintStream printf(Locale l, @NotNull String format, Object... args) {
        return format(l, format, args);
    }

    @Override
    public abstract void println();

    @Override
    public void println(Object x) {
        println(String.valueOf(x));
    }

    @Override
    public abstract void println(String x);

    @Override
    public void println(boolean x) {
        println(String.valueOf(x));
    }

    @Override
    public void println(char x) {
        println(String.valueOf(x));
    }

    @Override
    public void println(char[] x) {
        println(String.valueOf(x));
    }

    @Override
    public void println(double x) {
        println(String.valueOf(x));
    }

    @Override
    public void println(float x) {
        println(String.valueOf(x));
    }

    @Override
    public void println(int x) {
        println(String.valueOf(x));
    }

    @Override
    public void println(long x) {
        println(String.valueOf(x));
    }
}
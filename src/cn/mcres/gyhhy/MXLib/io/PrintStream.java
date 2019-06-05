/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.io;

import java.io.OutputStream;
import java.util.Locale;

/**
 *
 * @author 32798
 */
public abstract class PrintStream extends java.io.PrintStream {

    protected PrintStream(OutputStream out) {
        super(out);
    }

    protected PrintStream() {
        this(cn.mcres.gyhhy.MXLib.io.EmptyStream.stream.asOutputStream());
    }

    @Override
    public java.io.PrintStream format(String format, Object... args) {
        print(String.format(format, args));
        return this;
    }

    @Override
    public java.io.PrintStream format(Locale l, String format, Object... args) {
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
    public java.io.PrintStream printf(String format, Object... args) {
        return format(format, args);
    }

    @Override
    public java.io.PrintStream printf(Locale l, String format, Object... args) {
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

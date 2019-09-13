/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: LinePrintWriter.java@author: karlatemp@vip.qq.com: 19-9-11 下午1:14@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.io;

import java.io.PrintWriter;
import java.util.Locale;
import java.util.Objects;

public class LinePrintWriter extends PrintWriter implements LineWritable {

    private final PrintWriter p;

    public LinePrintWriter(PrintWriter out) {
        super(Objects.requireNonNull(out, "Null Writer"));
        this.p = out;
    }

    @Override
    public String toString() {
        return p.toString();
    }

    @Override
    public void close() {
        p.close();
    }

    @Override
    public void flush() {
        p.flush();
    }

    @Override
    public boolean checkError() {
        return p.checkError();
    }

    @Override
    public void write(int b) {
        p.write(b); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void println(String x) {
        p.println(x); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void println() {
        p.println(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void print(String s) {
        p.print(s); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LinePrintWriter append(CharSequence csq, int start, int end) {
        p.append(csq, start, end); //To change body of generated methods, choose Tools | Templates.
        return this;
    }

    @Override
    public LinePrintWriter append(char c) {
        p.append(c); //To change body of generated methods, choose Tools | Templates.
        return this;
    }

    @Override
    public LinePrintWriter append(CharSequence csq) {
        p.append(csq);
        return this;
    }

    @Override
    public LinePrintWriter format(String format, Object... args) {
        p.format(format, args);
        return this;
    }

    @Override
    public LinePrintWriter format(Locale l, String format, Object... args) {
        p.format(format, args);
        return this;
    }

    @Override
    public void print(Object obj) {
        p.print(obj);
    }

    @Override
    public void print(boolean b) {
        p.print(b);
    }

    @Override
    public void print(char c) {
        p.print(c);
    }

    @Override
    public void print(char[] s) {
        p.print(s);
    }

    @Override
    public void print(double d) {
        p.print(d);
    }

    @Override
    public void print(float f) {
        p.print(f);
    }

    @Override
    public void print(int i) {
        p.print(i);
    }

    @Override
    public void print(long l) {
        p.print(l);
    }

    @Override
    public LinePrintWriter printf(String format, Object... args) {
        p.printf(format, args);
        return this;
    }

    @Override
    public LinePrintWriter printf(Locale l, String format, Object... args) {
        p.format(l, format, args);
        return this;
    }

    @Override
    public void println(Object x) {
        p.println(x);
    }

    @Override
    public void println(boolean x) {
        p.println(x);
    }

    @Override
    public void println(char x) {
        p.println(x);
    }

    @Override
    public void println(char[] x) {
        p.println(x);
    }

    @Override
    public void println(double x) {
        p.println(x);
    }

    @Override
    public void println(float x) {
        p.println(x);
    }

    @Override
    public void println(int x) {
        p.println(x);
    }

    @Override
    public void println(long x) {
        p.println(x);
    }
}

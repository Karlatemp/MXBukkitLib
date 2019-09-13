/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: CharCompiler.java@author: karlatemp@vip.qq.com: 19-9-11 下午1:11@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.io;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.function.IntPredicate;

/**
 * Compile chars. 编译字符
 * {@link cn.mcres.karlatemp.mxlib.tools.CharCompiler}
 *
 * @author Karlatemp
 */
public class CharCompiler {
    public static IntPredicate getDefaultTester() {
        return cn.mcres.karlatemp.mxlib.tools.CharCompiler.getDefaultTester();
    }

    public static IntPredicate getURITester() {
        return cn.mcres.karlatemp.mxlib.tools.CharCompiler.getURITester();
    }

    public static IntPredicate getURLTester() {
        return cn.mcres.karlatemp.mxlib.tools.CharCompiler.getURLTester();
    }

    public static Charset getCharset(String name) throws UnsupportedEncodingException {
        return cn.mcres.karlatemp.mxlib.tools.CharCompiler.getCharset(name);
    }

    public static String encode(String line) {
        return cn.mcres.karlatemp.mxlib.tools.CharCompiler.encode(line);
    }

    public static String encode(String line, String charset) throws UnsupportedEncodingException {
        return encode('%', line, getCharset(charset));
    }

    public static String encode(char s, String line) {
        return cn.mcres.karlatemp.mxlib.tools.CharCompiler.encode(s, line);
    }

    public static String encode(char s, String line, Charset cs) {
        return cn.mcres.karlatemp.mxlib.tools.CharCompiler.encode(s, line, cs);
    }


    public static String encode(char s, String line, Charset cs, IntPredicate tester) {
        return cn.mcres.karlatemp.mxlib.tools.CharCompiler.encode(s, line, cs, tester);
    }

    public static String decode(String line) {
        return cn.mcres.karlatemp.mxlib.tools.CharCompiler.decode(line);
    }

    public static String decode(String line, String charset) throws UnsupportedEncodingException {
        return cn.mcres.karlatemp.mxlib.tools.CharCompiler.decode(line, charset);
    }

    public static String decode(char s, String line) {
        return cn.mcres.karlatemp.mxlib.tools.CharCompiler.decode(s, line);
    }

    public static String decode(char s, String line, Charset cs) {
        return cn.mcres.karlatemp.mxlib.tools.CharCompiler.decode(s, line, cs);
    }
}

/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: StringHelper.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import cn.mcres.gyhhy.MXLib.format.DefaultFormatter;
import cn.mcres.gyhhy.MXLib.format.Formatter;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 */
public class StringHelper {

    public final static String[] hexDigits
            = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
    /**
     * A table of hex digits
     */
    public static final char[] hexDigit = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    public static String time(long l) {
        return cn.mcres.karlatemp.mxlib.tools.StringHelper.time(l);
    }

    public static char[] stringToChars(String line) {
        return cn.mcres.karlatemp.mxlib.tools.StringHelper.stringToChars(line);
    }

    public static String md5(String inputStr) {
        return encodeByMD5(inputStr);
    }

    public static String encodeByMD5(String originString) {
        return cn.mcres.karlatemp.mxlib.tools.StringHelper.encodeByMD5(originString);
    }

    public static String byteArrayToHexString(byte[] b) {
        return cn.mcres.karlatemp.mxlib.tools.StringHelper.byteArrayToHexString(b);
    }

    public static String byteToHexString(byte b) {
        return cn.mcres.karlatemp.mxlib.tools.StringHelper.byteToHexString(b);
    }

    public static String utf_decode(String str) {
        return cn.mcres.karlatemp.mxlib.tools.StringHelper.utf_decode(str);
    }

    public static char toHex(int nibble) {
        return cn.mcres.karlatemp.mxlib.tools.StringHelper.toHex(nibble);
    }

    public static String html(String h) {
        return cn.mcres.karlatemp.mxlib.tools.StringHelper.html(h);
    }

    public static String reg(String s) {
        return cn.mcres.karlatemp.mxlib.tools.StringHelper.reg(s);
    }

    public static String utf_endode(String str) {
        return StringHelper.utf_decode(str);
    }

    public static String[] cut(String[] strings, int off, int length) {
        return cn.mcres.karlatemp.mxlib.tools.StringHelper.cut(strings, off, length);
    }

    public static boolean isemp(String str) {
        return cn.mcres.karlatemp.mxlib.tools.StringHelper.isemp(str);
    }

    public static String color(String a) {
        return cn.mcres.karlatemp.mxlib.tools.StringHelper.color(a);
    }

    public static String ve(String str, String ver, String val) {
        return cn.mcres.karlatemp.mxlib.tools.StringHelper.ve(str, ver, val);
    }

    public static String get(String[] a, int b) {
        return cn.mcres.karlatemp.mxlib.tools.StringHelper.get(a, b);
    }

    public static String variable(String line, Object[] args) {
        if (line == null || line.isEmpty() || args == null || args.length == 0) {
            return line;
        }
        HashMap<String, Object> mmp = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            mmp.put(String.valueOf(i), args[i]);
        }
        return variable(line, mmp);
    }

    public static Formatter variableFormatter = new DefaultFormatter();

    public static String variable(String line, Map<String, Object> args) {
        if (variableFormatter != null) {
            return variableFormatter.format(line, Formatter.mapping(args));
        }
        if (line == null || line.isEmpty() || args == null || args.isEmpty()) {
            return line;
        }
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            switch (c) {
                case '{': {
                    StringBuilder bx = new StringBuilder();
                    i++;
                    for (; i < line.length(); i++) {
                        char z = line.charAt(i);
                        boolean b = false;
                        switch (z) {
                            case '}': {
                                b = true;
                                break;
                            }
                            case '%': {
                                i++;
                                char f = line.charAt(i);
                                switch (f) {
                                    case '{':
                                    case '%':
                                    case '}': {
                                        bx.append(f);
                                        break;
                                    }
                                    default: {
                                        bx.append(c).append(f);
                                        break;
                                    }
                                }
                                break;
                            }
                            default: {
                                bx.append(z);
                                break;
                            }
                        }
                        if (b) {
                            break;
                        }
                    }
                    String to = bx.toString();
                    Object get = args.get(to);
                    if (get == null) {
                        buffer.append('{').append(to).append('}');
                    } else {
                        buffer.append(get);
                    }
                    break;
                }
                case '%': {
                    i++;
                    char f = line.charAt(i);
                    switch (f) {
                        case '{':
                        case '%':
                        case '}': {
                            buffer.append(f);
                            break;
                        }
                        default: {
                            buffer.append(c).append(f);
                            break;
                        }
                    }
                    break;
                }
                default: {
                    buffer.append(c);
                    break;
                }
            }
        }
        return buffer.toString();
    }

    public static String rechangeString(String string, String fromCode, String toCode) throws UnsupportedEncodingException {
        return new String(string.getBytes(fromCode), toCode);
    }

    public static String UTF_8toGBK(String string) throws UnsupportedEncodingException {
        return rechangeString(string, "UTF-8", "GBK");
    }

    public static String GBKtoUTF_8(String string) throws UnsupportedEncodingException {
        return rechangeString(string, "GBK", "UTF-8");
    }

    public static byte[] getBytes(String string, String charset) throws UnsupportedEncodingException {
        return string.getBytes(charset);
    }

    public static byte[] getBytes(String string) throws UnsupportedEncodingException {
        return getBytes(string, "UTF-8");
    }

    public static String fill(char c, int length) {
        return cn.mcres.karlatemp.mxlib.tools.StringHelper.fill(c, length);
    }

    private String str;

    public StringHelper(String str) {
        this.str = str;
    }

    public String nocolor() {
        return String.valueOf(str);
    }

    public String toString() {
        return color(String.valueOf(str));
    }

    public StringHelper r(String a, String b) {
        this.str = ve(this.str, a, b);
        return this;
    }
}

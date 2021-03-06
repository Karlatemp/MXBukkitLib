/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: StringHelper.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.karlatemp.mxlib.tools;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.security.MessageDigest;

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
        long ms = l % 1000;
        long s = (l - ms) / 1000;
        StringBuilder b = new StringBuilder();
        if (s >= 60) {
            long tm = s % 60;
            long m = (s - tm) / 60;
            s = tm;
            if (m > 60) {
                tm = m % 60;
                long h = (m - tm) / 60;
                m = tm;
                if (h > 0) {
                    b.append(h).append("h");
                }
            }
            if (m > 0) {
                b.append(m).append("min");
            }
        }
        if (s > 0) {
            b.append(s).append("s");
        }
        if (ms > 0) {
            b.append(ms).append("ms");
        }
        return b.toString();
    }

    public static char[] stringToChars(String line) {
        if (line == null) {
            return new char[0];
        }
        return line.toCharArray();
    }

    public static String md5(String inputStr) {
        return encodeByMD5(inputStr);
    }

    public static String encodeByMD5(String originString) {
        if (originString != null) {
            try {
                //创建具有指定算法名称的信息摘要 
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                //使用指定的字节数组对摘要进行最后更新，然后完成摘要计算 
                byte[] results = md5.digest(originString.getBytes());
                //将得到的字节数组变成字符串返回  
                String result = byteArrayToHexString(results);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String byteArrayToHexString(byte[] b) {
        var builder = new StringBuilder();
        for (var byte_ : b) {
            builder.append(hexDigit[(byte_ >> 4) & 0xF]);
            builder.append(hexDigit[byte_ & 0xF]);
        }
        return builder.toString();
    }

    public static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    private static String loadConvert(char[] in, int off, int len, char[] convtBuf) {
        if (convtBuf.length < len) {
            int newLen = len * 2;
            if (newLen < 0) {
                newLen = Integer.MAX_VALUE;
            }
            convtBuf = new char[newLen];
        }
        char aChar;
        char[] out = convtBuf;
        int outLen = 0;
        int end = off + len;

        while (off < end) {
            aChar = in[off++];
            if (aChar == '\\') {
                aChar = in[off++];
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = in[off++];
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed \\uxxxx encoding.");
                        }
                    }
                    out[outLen++] = (char) value;
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    } else if (aChar == 'r') {
                        aChar = '\r';
                    } else if (aChar == 'n') {
                        aChar = '\n';
                    } else if (aChar == 'f') {
                        aChar = '\f';
                    }
                    out[outLen++] = aChar;
                }
            } else {
                out[outLen++] = aChar;
            }
        }
        return new String(out, 0, outLen);
    }

    public static String utf_decode(String str) {
        byte[] bytes = str.getBytes();
        char[] ch = new char[bytes.length];
        for (int x = 0; x < bytes.length; x++) {
            ch[x] = (char) bytes[x];
        }
        return loadConvert(ch, 0, bytes.length, new char[bytes.length]);
    }

    public static char toHex(int nibble) {
        return hexDigit[(nibble & 0xF)];
    }

    public static String html(String h) {
        if (h == null) {
            return "";
        }
        h = h.replaceAll("\\&", reg("&amp;"));
        h = h.replaceAll("\\<", reg("&lt;"));
        h = h.replaceAll("\\>", reg("&gt;"));

        h = h.replaceAll("\\\u2200", reg("&forall;"));
        h = h.replaceAll("\\\u2202", reg("&part;"));
        h = h.replaceAll("\\\u2203", reg("&exist;"));
        h = h.replaceAll("\\\u2205", reg("&empty;"));
        h = h.replaceAll("\\\u2207", reg("&nabla;"));
        h = h.replaceAll("\\\u2208", reg("&isin;"));
        h = h.replaceAll("\\\u2209", reg("&notin;"));
        h = h.replaceAll("\\\u220B", reg("&ni;"));
        h = h.replaceAll("\\\u220F", reg("&prod;"));
        h = h.replaceAll("\\\u2211", reg("&sum;"));
        h = h.replaceAll("\\\u0391", reg("&Alpha;"));
        h = h.replaceAll("\\\u0392", reg("&Beta;"));
        h = h.replaceAll("\\\u0393", reg("&Gamma;"));
        h = h.replaceAll("\\\u0394", reg("&Delta;"));
        h = h.replaceAll("\\\u0395", reg("&Epsilon;"));
        h = h.replaceAll("\\\u0396", reg("&Zeta;"));
        h = h.replaceAll("\\\u00A9", reg("&copy;"));
        h = h.replaceAll("\\\u00AE", reg("&reg;"));
        h = h.replaceAll("\\\u20AC", reg("&euro;"));
        h = h.replaceAll("\\\u2122", reg("&trade;"));
        h = h.replaceAll("\\\u2190", reg("&larr;"));
        h = h.replaceAll("\\\u2191", reg("&uarr;"));
        h = h.replaceAll("\\\u2192", reg("&rarr;"));
        h = h.replaceAll("\\\u2193", reg("&darr;"));
        h = h.replaceAll("\\\u2660", reg("&spades;"));
        h = h.replaceAll("\\\u2663", reg("&clubs;"));
        h = h.replaceAll("\\\u2665", reg("&hearts;"));
        h = h.replaceAll("\\\u2666", reg("&diams;"));
        return h;
    }

    public static String reg(String s) {
        if (s == null) {
            return "";
        }
        s = s.replaceAll("\\\\", "\\\\\\\\");
        s = s.replaceAll("\\^", "\\\\\\^");
        s = s.replaceAll("\\$", "\\\\\\$");
        s = s.replaceAll("\\*", "\\\\\\*");
        s = s.replaceAll("\\+", "\\\\\\+");
        s = s.replaceAll("\\?", "\\\\\\?");
        s = s.replaceAll("\\=", "\\\\\\=");
        s = s.replaceAll("\\!", "\\\\\\!");
        s = s.replaceAll("\\<", "\\\\\\<");
        s = s.replaceAll("\\>", "\\\\\\>");
        s = s.replaceAll("\\-", "\\\\\\-");
        s = s.replaceAll("\\[", "\\\\\\[");
        s = s.replaceAll("\\]", "\\\\\\]");
        s = s.replaceAll("\\|", "\\\\\\|");
        s = s.replaceAll("\\{", "\\\\\\{");
        s = s.replaceAll("\\}", "\\\\\\}");
        s = s.replaceAll("\\(", "\\\\\\(");
        s = s.replaceAll("\\)", "\\\\\\)");
        s = s.replaceAll("\\,", "\\\\\\,");
        s = s.replaceAll("\\.", "\\\\\\.");
        s = s.replaceAll("\\:", "\\\\\\:");
        s = s.replaceAll("\\/", "\\\\\\/");
        s = s.replaceAll("\\n", "\\\\n");
        return s;
    }

    public static String utf_endode(String str) {
        boolean escapeSpace = false;
        final String theString = str;
        int len = theString.length();
        int bufLen = len * 2;
        if (bufLen < 0) {
            bufLen = Integer.MAX_VALUE;
        }
        StringBuffer outBuffer = new StringBuffer(bufLen);
        for (int x = 0; x < len; x++) {
            char aChar = theString.charAt(x);
            // Handle common case first, selecting largest block that
            // avoids the specials below
            if ((aChar > 61) && (aChar < 127)) {
                if (aChar == '\\') {
                    outBuffer.append('\\');
                    outBuffer.append('\\');
                    continue;
                }
                outBuffer.append(aChar);
                continue;
            }
            switch (aChar) {
                case ' ':
                    if (x == 0 || escapeSpace) {
                        outBuffer.append('\\');
                    }
                    outBuffer.append(' ');
                    break;
                case '\t':
                    outBuffer.append('\\');
                    outBuffer.append('t');
                    break;
                case '\n':
                    outBuffer.append('\\');
                    outBuffer.append('n');
                    break;
                case '\r':
                    outBuffer.append('\\');
                    outBuffer.append('r');
                    break;
                case '\f':
                    outBuffer.append('\\');
                    outBuffer.append('f');
                    break;
                case '\"':
                    outBuffer.append('\\');
                    outBuffer.append('\"');
                    break;
                case '\'':
                    outBuffer.append('\\');
                    outBuffer.append('\'');
                    break;
//                case '=': // Fall through
//                case ':': // Fall through
//                case '#': // Fall through
//                case '!':
//                    outBuffer.append('\\'); outBuffer.append(aChar);
//                    break;//Disable
                default:
                    if (((aChar < 0x0020) || (aChar > 0x007e))) {
                        outBuffer.append('\\');
                        outBuffer.append('u');
                        outBuffer.append(toHex((aChar >> 12) & 0xF));
                        outBuffer.append(toHex((aChar >> 8) & 0xF));
                        outBuffer.append(toHex((aChar >> 4) & 0xF));
                        outBuffer.append(toHex(aChar & 0xF));
                    } else {
                        outBuffer.append(aChar);
                    }
            }
        }
        return outBuffer.toString();
    }

    public static String[] cut(String[] strings, int off, int length) {
        length = Math.max(length, 0);
        off = Math.max(off, 0);
        String[] s = new String[length];
        int i = 0;
        for (; off < strings.length && i < s.length; off++, i++) {
            s[i] = strings[off];
        }
        return s;
    }

    public static boolean isemp(String str) {
        if (str == null) {
            return true;
        }
        str = str.replaceAll("([&§].|\\s)", "");
        return str.isEmpty();
    }

    public static String color(String a) {
        a = a.replaceAll("&1", "\u00a71");
        a = a.replaceAll("&2", "\u00a72");
        a = a.replaceAll("&3", "\u00a73");
        a = a.replaceAll("&4", "\u00a74");
        a = a.replaceAll("&5", "\u00a75");
        a = a.replaceAll("&6", "\u00a76");
        a = a.replaceAll("&7", "\u00a77");
        a = a.replaceAll("&8", "\u00a78");
        a = a.replaceAll("&9", "\u00a79");
        a = a.replaceAll("&0", "\u00a70");
        a = a.replaceAll("&a", "\u00a7a");
        a = a.replaceAll("&b", "\u00a7b");
        a = a.replaceAll("&c", "\u00a7c");
        a = a.replaceAll("&d", "\u00a7d");
        a = a.replaceAll("&e", "\u00a7e");
        a = a.replaceAll("&f", "\u00a7f");
        a = a.replaceAll("&r", "\u00a7r");
        a = a.replaceAll("&o", "\u00a7o");
        a = a.replaceAll("&l", "\u00a7l");
        a = a.replaceAll("&k", "\u00a7k");
        a = a.replaceAll("&n", "\u00a7n");
        a = a.replaceAll("&m", "\u00a7m");
        return a;
    }

    public static String ve(String str, String ver, String val) {
        return str.replace("{@}".replace("@", ver), val);
    }

    public static String get(String[] a, int b) {
        if (b < 0) {
            b = 0;
        }
        String ret = null;
        if (a == null) {
            return "";
        }
        if (b < a.length) {
            ret = a[b];
        }
        if (ret == null) {
            ret = "";
        }
        return ret;
    }

    public static String fill(char c, int length) {
        char[] list = new char[length];
        for (int i = 0; i < length; i++) {
            list[i] = c;
        }
        return new String(list);
    }

    private String str;

    public StringHelper(String str) {
        this.str = str;
    }

    public static boolean isEmpty(String s) {
        return s.trim().isEmpty();
    }

    public static char parseUnicode(char c, char c1, char c2, char c3) {
        //noinspection PointlessBitwiseExpression
        return (char) (0 |
                charHexToInt(c) << 12 |
                charHexToInt(c1) << 8 |
                charHexToInt(c2) << 4 |
                charHexToInt(c3)
        );
    }

    public static int charHexToInt(int charx) {
        if (charx >= '0' && charx <= '9') {
            return charx - '0';
        }
        if (charx >= 'A' && charx <= 'F') {
            return charx - 'A' + 0xA;
        }
        if (charx >= 'a' && charx <= 'f') {
            return charx - 'a' + 0xA;
        }
        return 0;
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

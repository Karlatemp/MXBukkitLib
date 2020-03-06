/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: CharCompiler.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tools;


import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.BitSet;
import java.util.function.IntPredicate;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Compile chars. 编译字符
 *
 * @author 32798
 */
public class CharCompiler {

    private static final IntPredicate deftester = c -> c >= Byte.MAX_VALUE;
    private static final BitSet URL_NO_ENCODING = new BitSet(256);
    private static final IntPredicate urltester = c -> !URL_NO_ENCODING.get(c);
    private static byte[] rr = new byte[128];

    public static IntPredicate getDefaultTester() {
        return deftester;
    }

    public static IntPredicate getURITester() {
        return urltester;
    }

    public static IntPredicate getURLTester() {
        return urltester;
    }

    static {
        /* The list of characters that are not encoded has been
         * determined as follows:
         *
         * RFC 2396 states:
         * -----
         * Data characters that are allowed in a URI but do not have a
         * reserved purpose are called unreserved.  These include upper
         * and lower case letters, decimal digits, and a limited set of
         * punctuation marks and symbols.
         *
         * unreserved  = alphanum | mark
         *
         * mark        = "-" | "_" | "." | "!" | "~" | "*" | "'" | "(" | ")"
         *
         * Unreserved characters can be escaped without changing the
         * semantics of the URI, but this should not be done unless the
         * URI is being used in a context that does not allow the
         * unescaped character to appear.
         * -----
         *
         * It appears that both Netscape and Internet Explorer escape
         * all special characters from this list with the exception
         * of "-", "_", ".", "*". While it is not clear why they are
         * escaping the other characters, perhaps it is safest to
         * assume that there might be contexts in which the others
         * are unsafe if not escaped. Therefore, we will use the same
         * list. It is also noteworthy that this is consistent with
         * O'Reilly's "HTML: The Definitive Guide" (page 164).
         *
         * As a last note, Intenet Explorer does not encode the "@"
         * character which is clearly not unreserved according to the
         * RFC. We are being consistent with the RFC in this matter,
         * as is Netscape.
         *
         */
        BitSet dontNeedEncoding = URL_NO_ENCODING;
        int i;
        for (i = 'a'; i <= 'z'; i++) {
            dontNeedEncoding.set(i);
        }
        for (i = 'A'; i <= 'Z'; i++) {
            dontNeedEncoding.set(i);
        }
        for (i = '0'; i <= '9'; i++) {
            dontNeedEncoding.set(i);
        }
        // dontNeedEncoding.set(' ');
        /* encoding a space to a + is done
                                    * in the encode() method */
        dontNeedEncoding.set('-');
        dontNeedEncoding.set('_');
        dontNeedEncoding.set('.');
        dontNeedEncoding.set('*');
    }

    static {
        for (byte i = 0; i < 10; i++) {
            rr['0' + i] = i;
        }
        for (byte i = 0xA; i < 0xF + 1; i++) {
            int off = i - 0xA;
            rr['a' + off] = i;
            rr['A' + off] = i;
        }
    }

    public static Charset getCharset(String name) throws UnsupportedEncodingException {
        try {
            return Charset.forName(name);
        } catch (IllegalCharsetNameException | UnsupportedCharsetException e) {
            throw new UnsupportedEncodingException(name);
        }
    }

    public static String encode(String line) {
        return encode('%', line);
    }

    public static String encode(String line, String charset) throws UnsupportedEncodingException {
        return encode('%', line, getCharset(charset));
    }

    public static String encode(char s, String line) {
        return encode(s, line, null);
    }

    public static String encode(char s, String line, Charset cs) {
        return encode(s, line, cs, deftester);
    }

    private static void a(char s, StringBuilder buffer, StringBuilder sb, Charset cs) {
        int leng = buffer.length();
        if (leng != 0) {
            char[] cr = new char[leng];
            buffer.getChars(0, leng, cr, 0);
            CharBuffer cb = CharBuffer.wrap(cr);
            ByteBuffer bb = cs.encode(cb);
            while (bb.position() < bb.limit()) {
                int read = bb.get() & 0xFF;
                sb.append(s).append(StringHelper.hexDigit[(read >> 4) & 0xF]).append(StringHelper.hexDigit[read & 0xF]);
            }
            buffer.delete(0, leng);
        }
    }

    public static String encode(char s, String line, Charset cs, IntPredicate tester) {
        if (line == null) {
            return "";
        }
        if (line.isEmpty()) {
            return line;
        }
        if (cs == null) {
            cs = UTF_8;
        }
        StringBuilder sb = new StringBuilder(line.length());
        StringBuilder buffer = new StringBuilder();
        for (char c : line.toCharArray()) {
            if (c == s || tester.test(c)) {
                buffer.append(c);
            } else {
                a(s, buffer, sb, cs);
                sb.append(c);
            }
        }
        a(s, buffer, sb, cs);
        return sb.toString();
    }

    public static String decode(String line) {
        return decode('%', line);
    }

    public static String decode(String line, String charset) throws UnsupportedEncodingException {
        return decode('%', line, getCharset(charset));
    }

    public static String decode(char s, String line) {
        return decode(s, line, null);
    }

    private static char a(char[] b, int c) {
        if (c > -1) {
            if (c < b.length) {
                return b[c];
            }
        }
        return 0;
    }

    private static void dex(StringBuilder buf, StringBuilder sb, Charset cc) {
        int l = buf.length();
        if (l != 0) {
            char[] crs = new char[l];
            buf.getChars(0, l, crs, 0);
            buf.delete(0, l);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            for (int i = 0; i < l; i += 2) {
                int b = (rr[a(crs, i)] << 4) + rr[a(crs, i + 1)];
                baos.write(b);
            }
            CharBuffer cb = cc.decode(ByteBuffer.wrap(baos.toByteArray()));
            char[] c = new char[cb.limit() - cb.position()];
            cb.get(c);
            sb.append(c);
        }
    }

    public static String decode(char s, String line, Charset cs) {
        if (line == null) {
            return "";
        }
        if (line.isEmpty()) {
            return line;
        }
        if (cs == null) {
            cs = UTF_8;
        }
        StringBuilder sb = new StringBuilder(line.length());
        StringBuilder buf = new StringBuilder();
        char[] arr = line.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            char c = arr[i];
            if (c == s) {
                int r = Math.min(2, arr.length - i - 1);
                while (r-- > 0) {
                    buf.append(arr[++i]);
                }
            } else {
                dex(buf, sb, cs);
                sb.append(c);
            }
        }
        dex(buf, sb, cs);
        return sb.toString();
    }
}

/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: URLEncoder.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */
package cn.mcres.karlatemp.mxlib.encrypt;

import cn.mcres.karlatemp.mxlib.tools.CharCompiler;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;

/**
 * Utility class for HTML form encoding. This class contains static methods for
 * converting a String to the <CODE>application/x-www-form-urlencoded</CODE>
 * MIME format. For more information about HTML form encoding, consult the HTML
 * <A HREF="http://www.w3.org/TR/html4/">specification</A>.
 *
 * <p>
 * When encoding a String, the following rules apply:
 *
 * <ul>
 * <li>The alphanumeric characters &quot;{@code a}&quot; through
 * &quot;{@code z}&quot;, &quot;{@code A}&quot; through &quot;{@code Z}&quot;
 * and &quot;{@code 0}&quot; through &quot;{@code 9}&quot; remain the same.
 * <li>The special characters &quot;{@code .}&quot;, &quot;{@code -}&quot;,
 * &quot;{@code *}&quot;, and &quot;{@code _}&quot; remain the same.
 * <li>The space character &quot; &nbsp; &quot; is converted into a plus sign
 * &quot;{@code +}&quot;.
 * <li>All other characters are unsafe and are first converted into one or more
 * bytes using some encoding scheme. Then each byte is represented by the
 * 3-character string &quot;<i>{@code %xy}</i>&quot;, where <i>xy</i> is the
 * two-digit hexadecimal representation of the byte. The recommended encoding
 * scheme to use is UTF-8. However, for compatibility reasons, if an encoding is
 * not specified, then the default encoding of the platform is used.
 * </ul>
 *
 * <p>
 * For example using UTF-8 as the encoding scheme the string &quot;The string
 * &#252;@foo-bar&quot; would get converted to
 * &quot;The+string+%C3%BC%40foo-bar&quot; because in UTF-8 the character &#252;
 * is encoded as two bytes C3 (hex) and BC (hex), and the character @ is encoded
 * as one byte 40 (hex).
 *
 * @author Herb Jellinek
 * @since JDK1.0
 */
public class URLEncoder {

    // static final BitSet dontNeedEncoding;
    // static final int caseDiff = ('a' - 'A');
    static final String dfltEncName;

    static {

        dfltEncName = System.getProperty("file.encoding", "utf-8");
    }

    /**
     * You can't call the constructor.
     */
    private URLEncoder() {
    }

    /**
     * Translates a string into {@code x-www-form-urlencoded} format. This
     * method uses the platform's default encoding as the encoding scheme to
     * obtain the bytes for unsafe characters.
     *
     * @param s {@code String} to be translated.
     * @return the translated {@code String}.
     * @deprecated The resulting string may vary depending on the platform's
     * default encoding. Instead, use the encode(String,String) method to
     * specify the encoding.
     */
    @Deprecated
    public static String encode(String s) {

        String str = null;

        try {
            str = encode(s, dfltEncName);
        } catch (UnsupportedEncodingException e) {
            // The system should always have the platform default
        }

        return str;
    }

    public static String encode(String s, String enc)
            throws UnsupportedEncodingException {

        if (enc == null) {
            throw new NullPointerException("charsetName");
        }

        try {
            return encode(s, Charset.forName(enc));
        } catch (IllegalCharsetNameException | UnsupportedCharsetException e) {
            throw new UnsupportedEncodingException(enc);
        }

    }

    public static String encode(String s, Charset charset) {
        return CharCompiler.encode('%', s, charset, CharCompiler.getURLTester());
    }
}

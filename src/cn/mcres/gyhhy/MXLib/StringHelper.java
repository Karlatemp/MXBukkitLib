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


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class StringHelper {
    public final static String[] hexDigits =
    {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
    /** A table of hex digits */
    public static final char[] hexDigit = {
        '0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'
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
    public static char[] stringToChars(String line){
        if(line == null) return new char[0];
        return line.toCharArray();
    }
    public static String md5(String inputStr){ 
        return encodeByMD5(inputStr); 
    } 
    public static String encodeByMD5(String originString){ 
        if (originString!=null) { 
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
    public static String byteArrayToHexString(byte[] b){ 
        StringBuffer resultSb = new StringBuffer(); 
        for(int i=0;i<b.length;i++){ 
            resultSb.append(byteToHexString(b[i])); 
        } 
        return resultSb.toString(); 
    } 
    public static String byteToHexString(byte b){ 
        int n = b; 
        if(n<0){
            n=256+n; 
        }
        int d1 = n/16; 
        int d2 = n%16; 
        return hexDigits[d1] + hexDigits[d2]; 
    } 
    private static String loadConvert (char[] in, int off, int len, char[] convtBuf) {
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
                if(aChar == 'u') {
                    // Read the xxxx
                    int value=0;
                    for (int i=0; i<4; i++) {
                        aChar = in[off++];
                        switch (aChar) {
                          case '0': case '1': case '2': case '3': case '4':
                          case '5': case '6': case '7': case '8': case '9':
                             value = (value << 4) + aChar - '0';
                             break;
                          case 'a': case 'b': case 'c':
                          case 'd': case 'e': case 'f':
                             value = (value << 4) + 10 + aChar - 'a';
                             break;
                          case 'A': case 'B': case 'C':
                          case 'D': case 'E': case 'F':
                             value = (value << 4) + 10 + aChar - 'A';
                             break;
                          default:
                              throw new IllegalArgumentException(
                                           "Malformed \\uxxxx encoding.");
                        }
                     }
                    out[outLen++] = (char)value;
                } else {
                    if (aChar == 't') aChar = '\t';
                    else if (aChar == 'r') aChar = '\r';
                    else if (aChar == 'n') aChar = '\n';
                    else if (aChar == 'f') aChar = '\f';
                    out[outLen++] = aChar;
                }
            } else {
                out[outLen++] = aChar;
            }
        }
        return new String (out, 0, outLen);
    }
    public static String utf_decode(String str){
        byte[] bytes = str.getBytes();
        char[] ch = new char[bytes.length];
        for(int x = 0;x < bytes.length; x ++){
            ch[x] = (char) bytes[x];
        }
        return loadConvert(ch,0,bytes.length,new char[bytes.length]);
    }
    public static char toHex(int nibble) {
        return hexDigit[(nibble & 0xF)];
    }
    public static String html(String h){
        if(h == null)return"";
        h = h.replaceAll("\\&",reg("&amp;"));
        h = h.replaceAll("\\<",reg("&lt;"));
        h = h.replaceAll("\\>",reg("&gt;"));
        
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
    public static String reg(String s){
        if(s  == null)return "";
        s = s.replaceAll("\\\\","\\\\\\\\");
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
        s = s.replaceAll("\\n","\\\\n");
        return s;
    }
    public static String utf_endode(String str){
        boolean escapeSpace = false;
        final String theString = str;
        int len = theString.length();
        int bufLen = len * 2;
        if (bufLen < 0) {
            bufLen = Integer.MAX_VALUE;
        }
        StringBuffer outBuffer = new StringBuffer(bufLen);
        for(int x=0; x<len; x++) {
            char aChar = theString.charAt(x);
            // Handle common case first, selecting largest block that
            // avoids the specials below
            if ((aChar > 61) && (aChar < 127)) {
                if (aChar == '\\') {
                    outBuffer.append('\\'); outBuffer.append('\\');
                    continue;
                }
                outBuffer.append(aChar);
                continue;
            }
            switch(aChar) {
                case ' ':
                    if (x == 0 || escapeSpace)
                        outBuffer.append('\\');
                    outBuffer.append(' ');
                    break;
                case '\t':outBuffer.append('\\'); outBuffer.append('t');
                          break;
                case '\n':outBuffer.append('\\'); outBuffer.append('n');
                          break;
                case '\r':outBuffer.append('\\'); outBuffer.append('r');
                          break;
                case '\f':outBuffer.append('\\'); outBuffer.append('f');
                          break;
                case '\"':outBuffer.append('\\'); outBuffer.append('\"');
                          break;
                case '\'':outBuffer.append('\\'); outBuffer.append('\'');
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
                        outBuffer.append(toHex((aChar >>  8) & 0xF));
                        outBuffer.append(toHex((aChar >>  4) & 0xF));
                        outBuffer.append(toHex( aChar        & 0xF));
                    } else {
                        outBuffer.append(aChar);
                    }
            }
        }
        return outBuffer.toString();
    }
    public static String[] cut(String[] strings, int off, int length) {
        length = Math.max(length,0);
        off = Math.max(off,0);
        String[] s = new String[length];
        int i = 0;
        for (;off < strings.length && i < s.length;off++,i++){
            s[i] = strings[off];
        }
        return s;
    }
    public static boolean isemp(String str){
        if (str == null){
            return true;
        }
        str = str.replaceAll("([&§].|\\s)", "");
        return str.isEmpty();
    }
    public static String color(String a){
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
    public static String ve(String str,String ver,String val){
        return str.replace("{@}".replace("@", ver), val);
    }
    public static String get(String[] a,int b){
        if (b < 0){
            b = 0;
        }
        String ret = null;
        if (a == null){
            return "";
        }
        if (b < a.length){
            ret = a[b];
        }
        if (ret == null){
            ret = "";
        }
        return ret;
    }
    public static String variable(String line,Object[] args){
        if(line == null|| line.isEmpty() || args == null || args.length == 0){
            return line;
        }
        HashMap<String,Object> mmp = new HashMap();
        for (int i = 0;i < args.length;i++){
            mmp.put(String.valueOf(i),args[i]);
        }
        return variable(line,mmp);
    }
    public static String variable(String line,Map<String,Object> args){
        if(line == null|| line.isEmpty() || args == null || args.isEmpty()){
            return line;
        }
        StringBuilder buffer = new StringBuilder();
        for(int i = 0;i < line.length(); i++){
            char c = line.charAt(i);
            switch(c){
                case '{':{
                    StringBuilder bx = new StringBuilder();
                    i++;
                    for(;i < line.length();i++){
                        char z = line.charAt(i);
                        boolean b = false;
                        switch(z){
                            case '}':{
                                b = true;
                                break;
                            }
                            case '%':{
                                i ++;
                                char f = line.charAt(i);
                                switch(f){
                                    case '{':
                                    case '%':
                                    case '}':{
                                        bx.append(f);
                                    }
                                    default:{
                                        bx.append(c).append(f);
                                        break;
                                    }
                                }
                                break;
                            }
                            default:{
                                bx.append(z);
                                break;
                            }
                        }
                        if(b)break;
                    }
                    String to = bx.toString();
                    Object get = args.get(to);
                    if(get == null){
                        buffer.append('{').append(to).append('}');
                    } else {
                        buffer.append(get);
                    }
                    break;
                }
                case '%':{
                    i ++;
                    char f = line.charAt(i);
                    switch(f){
                        case '{':
                        case '%':
                        case '}':{
                            buffer.append(f);
                            break;
                        }
                        default:{
                            buffer.append(c).append(f);
                            break;
                        }
                    }
                    break;
                }
                default:{
                    buffer.append(c);
                    break;
                }
            }
        }
        return buffer.toString();
    }
    public static String rechangeString(String string,String fromCode,String toCode) throws UnsupportedEncodingException{
        return new String(string.getBytes(fromCode),toCode);
    }
    public static String UTF_8toGBK(String string) throws UnsupportedEncodingException{
        return rechangeString(string,"UTF-8","GBK");
    }
    public static String GBKtoUTF_8(String string) throws UnsupportedEncodingException{
        return rechangeString(string,"GBK","UTF-8");
    }
    public static byte[] getBytes(String string,String charset) throws UnsupportedEncodingException{
        return string.getBytes(charset);
    }
    public static byte[] getBytes(String string) throws UnsupportedEncodingException{
        return getBytes(string,"UTF-8");
    }
    private String str;
    public StringHelper(String str){
        this.str = str;
    }
    public String nocolor(){
        return String.valueOf(str);
    }
    public String toString(){
        return color(String.valueOf(str));
    }
    public StringHelper r(String a,String b){
        this.str = ve(this.str,a,b);
        return this;
    }
}

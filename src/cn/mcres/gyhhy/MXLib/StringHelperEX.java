/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib;

import java.lang.reflect.Field;

/**
 *
 * @author 32798
 */
public class StringHelperEX extends StringHelper{
    public static final char[] abc = ("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
    public static final int awa = 70;
    public static final Field String$value;
    public static String random(int count){
        return random(count,StringHelperEX.hexDigit);
    }
    public static String random(int count, char[] chars){
        char[] cx = new char[count];
        int clen = chars.length;
        for(int i = 0; i < count; i ++){
            cx[i] = chars[(int)Math.floor(clen * Math.random())];
        }
        return new String(cx);
    }
    static {
        Field fe = null;
        try {
            fe = String.class.getDeclaredField("value");
        } catch (Throwable thr) {
            thr.printStackTrace();
        }
        String$value = fe;
    }

    public static char[] getChars(String str) {
        return RefUtil.get(String$value, (Object)str);
    }
    public static char[] setChars(String str,char[] news){
        return RefUtil.set(String$value, (Object)str, news);
    }
    /*public static void main(String[] ag){
        System.out.println(ClassLoader.getSystemClassLoader().getResource("sun/reflect/UnsafeQualifiedStaticObjectFieldAccessorImpl.class"));
        
        System.out.println((Object)RefUtil.get(StringHelperEX.class, "awa"));
        
        RefUtil.set(StringHelperEX.class, "awa",10);
//        System.out.println(field.getA);
        
        System.out.println((Object)RefUtil.get(StringHelperEX.class, "awa"));
    }*/

    public StringHelperEX(String str) {
        super(str);
    }
}

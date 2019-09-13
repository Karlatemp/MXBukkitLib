/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib;

import cn.mcres.gyhhy.MXLib.ext.lookup.Looker;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;

/**
 *
 * @author 32798
 */
public class StringHelperEX extends StringHelper {

    public static final char[] abc = ("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
    public static final int awa = 70;
    @Deprecated
    public static final Field String$value;
    private static final MethodHandle String_value_set;
    private static final MethodHandle String_value_get;

    static {
        Looker looker = RefUtilEx.looker(null);
        String_value_set = looker.findSetter(String.class, "value", char[].class);
        String_value_get = looker.findGetter(String.class, "value", char[].class);
    }

    public static String random(int count) {
        return random(count, StringHelperEX.hexDigit);
    }

    public static String random(int count, char[] chars) {
        char[] cx = new char[count];
        int clen = chars.length;
        for (int i = 0; i < count; i++) {
            cx[i] = chars[(int) Math.floor(clen * Math.random())];
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
        return RefUtilEx.invoke(String_value_get, str);
    }

    public static char[] setChars(String str, char[] news) {
        char[] o = getChars(str);
        RefUtilEx.invoke(String_value_set, str, news);
        return o;
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

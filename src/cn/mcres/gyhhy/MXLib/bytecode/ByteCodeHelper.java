/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.bytecode;

import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 32798
 */
public class ByteCodeHelper {
    
    public static final MXDynamicClassLoader DynamicClassLoader = new MXDynamicClassLoader();
    public static void classInfo(Class<?> cp,PrintStream out) {
        out.println();
        out.println(cp);
        out.println("Super: " + cp.getSuperclass());
        out.println("Fields:");
        Field[] fes = cp.getDeclaredFields();
        for (Field fe : fes) {
            out.println("  " + fe);
        }
        out.println("\nConstructors:");
        Constructor<?>[] cos = cp.getDeclaredConstructors();
        for (Constructor<?> co : cos) {
            out.println("  " + co);
        }
        out.println("\nMethods:");
        Method[] mets = cp.getDeclaredMethods();
        for (Method met : mets) {
            out.println("  " + met);
        }
    }
    public static String toJavaName(String str){
        return javassist.ClassMap.toJavaName(str);
    }
    public static String toJVMName(String str){
        return javassist.ClassMap.toJvmName(str);
    }
    public static Class<?> load(String name,byte[] bytecode){
        return getDynamicClassLoader().load(name, bytecode);
    }
    public static MXDynamicClassLoader getDynamicClassLoader() {
        return DynamicClassLoader;
    }
    private ByteCodeHelper() {
    }
    @SuppressWarnings("PublicInnerClass")
    public static class MXDynamicClassLoader extends ClassLoader {
        
        private String name;
        private byte[] codes;
        private MXDynamicClassLoader() {
            super(MXDynamicClassLoader.class.getClassLoader());
        }
        @SuppressWarnings({"AssignmentToMethodParameter", "AssignmentToCollectionOrArrayFieldFromParameter"})
        public Class<?> load(String name,byte[] byteCode){
            name = toJavaName(name);
            this.name = name;
            this.codes = byteCode;
            try {
                return findClass(name);
            } catch (ClassNotFoundException ex) {
                return null;
            }
        }
        @Override
        @SuppressWarnings("AssignmentToMethodParameter")
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            name  = toJavaName(name);
            if(name.equals(this.name)){
                return super.defineClass(name, codes, 0, codes.length);
            }
            throw new ClassNotFoundException(name);
        }
    }
}

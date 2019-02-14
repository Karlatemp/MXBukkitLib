/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package awawaw;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javassist.ByteArrayClassPath;
import javassist.CtNewConstructor;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.Modifier;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;

public class ex {

    private static void rdrd(ClassPool pool) throws Exception {
        CtClass packet = pool.get("net.minecraft.server.v1_8_R3.PacketPlayOutChat");
        CtConstructor[] constructors = packet.getConstructors();
        for (CtConstructor ct : constructors) {
            System.out.println("PACKET: " + ct.getSignature());
        }
    }

    public static String version(String vers, String str) {
        return str.replace("[V]", vers);
    }

    public static void eeee() throws Exception {

        Class<?> EnumProtocol = Class.forName("net.minecraft.server.v1_13_R2.EnumProtocol");

        Field[] fies = EnumProtocol.getDeclaredFields();
        Field fff = null;
        for (Field f : fies) {
            System.out.println(f);
            String s = f.getGenericType().toString();
            System.out.println(s);
            if (s.startsWith("java.util.Map<") && s.contains("EnumProtocolDirection") && s.endsWith(">>>")) {
                fff = f;
            }
        }
        System.out.println("__PW: "+fff);
    }

    @SuppressWarnings("null")
    public static void main(String[] args) throws Exception {
        eeee();
        if(true)return;
        ClassPool pool = new ClassPool();
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        pool.appendClassPath(new LoaderClassPath(loader));

        CtClass bc = pool.get("net.md_5.bungee.api.chat.BaseComponent[]");
        { // Append Field read
            ClassPool pool2 = new ClassPool(pool);
            CtClass MC = pool2.makeClass("test/test");
            MC.addField(new CtField(bc, "bcs", MC));
            pool.appendClassPath(new ByteArrayClassPath("test.test", MC.toBytecode()));
        }

        String ver = "v1_12_R1";

        CtClass packet = pool.get(version(ver, "net.minecraft.server.[V].PacketPlayOutChat"));
        CtClass mkClass = pool.makeClass("test/test", packet);
        mkClass.setModifiers(Modifier.PUBLIC);
        CtMethod[] mets = packet.getMethods();
        CtMethod main = null;
        rdrd(pool);
        for (CtMethod met : mets) {
            if (met.getName().equals("b")) {
                System.out.println(met);
                System.out.println(met.getSignature());
                if (met.getSignature().contains("PacketDataSerializer")) {
                    System.out.println("The METHOD!");
                    main = met;
                }
            }
        }
        {
            CtMethod mmd = main;
            main = new CtMethod(mmd.getReturnType(), mmd.getName(), mmd.getParameterTypes(), mkClass);

        }
        mkClass.addField(new CtField(bc, "bcs", mkClass));
        CtConstructor cooc = new CtConstructor(new CtClass[]{bc}, mkClass);
        System.out.println("SIGN: " + cooc.getSignature());
        cooc.setModifiers(Modifier.PUBLIC);
        CtConstructor[] costs = packet.getConstructors();
        boolean empt = false;
        boolean chatType = false;
        for (CtConstructor c : costs) {
            System.out.println("MPACKET: " + c.getSignature());
            if (c.getSignature().equals("()V")) {
                empt = true;
            } else if (c.getSignature().contains("ChatMessageType")) {
                chatType = true;
            }
        }
        if (empt) {
            cooc.setBody("{super();this.bcs = $1;}");
        } else if (chatType) {
            cooc.setBody("{super(null,(byte)0);this.bcs = $1;}");
        } else {
            cooc.setBody(version(ver, "{super(null,net.minecraft.server.[V].ChatMessageType.CHAT});this.bcs = $1;}"));
        }
        mkClass.addConstructor(cooc);

        main.setBody("{$1.a(net.md_5.bungee.chat.ComponentSerializer.toString(this.bcs));$1.writeByte(0);}");
        mkClass.addMethod(main);
        classInfo(getClass(mkClass.toBytecode(), mkClass.getName()).asSubclass(Class.forName(version(ver, "net.minecraft.server.[V].PacketPlayOutChat"))));
//        mkClass.setName(mkClass.);
//        classInfo(mkClass.toClass());
//        net.minecraft.server.v1_12_R1.ChatMessageType;
    }

    private static Class<?> getClass(byte[] bytes, String name) {
        String name_decode = name.replaceAll("/", ".");
        ClassLoader loader = new ClassLoader() {

            @Override
            protected Class<?> findClass(String nxe) throws ClassNotFoundException {
                if (nxe.endsWith(name_decode)) {
                    return this.defineClass(name_decode, bytes, 0, bytes.length);
                }
                throw new ClassNotFoundException(nxe);
            }

        };
        try {
            return loader.loadClass(name_decode);
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

    private static void classInfo(Class<?> cp) {
        System.out.println();
        System.out.println(cp);
        System.out.println("Super: " + cp.getSuperclass());
        System.out.println("Fields:");
        Field[] fes = cp.getDeclaredFields();
        for (Field fe : fes) {
            System.out.println("  " + fe);
        }
        System.out.println("\nConstructors:");
        Constructor<?>[] cos = cp.getDeclaredConstructors();
        for (Constructor<?> co : cos) {
            System.out.println("  " + co);
        }
        System.out.println("\nMethods:");
        Method[] mets = cp.getDeclaredMethods();
        for (Method met : mets) {
            System.out.println("  " + met);
        }
    }

}

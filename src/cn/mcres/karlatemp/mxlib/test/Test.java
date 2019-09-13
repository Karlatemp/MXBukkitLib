/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Test.java@author: karlatemp@vip.qq.com: 19-9-11 下午10:31@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.test;

import cn.mcres.gyhhy.MXLib.system.VMHelper;
import cn.mcres.gyhhy.MXLib.system.VMHelperImpl;
import cn.mcres.karlatemp.mxlib.instrumentation.Bootstrapper;
import cn.mcres.karlatemp.mxlib.network.NetWorkManager;
import cn.mcres.karlatemp.mxlib.security.InstrumentationSecuritySupport;
import cn.mcres.karlatemp.mxlib.share.$MXBukkitLibConfiguration;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.net.URLStreamHandler;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.ObjIntConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Test {
    private static String dump(Object o) {
        if (o == null) return "null";
        if (o instanceof Object[]) return Arrays.toString((Object[]) o);
        return String.valueOf(o);
    }

    public static void premain(String opt, Instrumentation instrumentation) {
        OpenJ11.setLogger((a, b, l, lr) -> {
            if (l == null) l = Level.INFO;
            String logger = "";
            if (lr != null) {
                logger = " [§a" + lr.getLoggerName() + "§r]";
            }
            return "§r[§bMain§e " + Thread.currentThread().getName() + "§r]" + logger + " [§6" + l + "§r]§b ";
        });
        System.out.println("P Call: " + opt);
        System.out.println("§11§22§33§44§55§66§77§88§99§00§aa§bb§cc§dd§ee§ff");
        Logger l = Logger.getLogger("mxlib.instrumentation");
        System.out.println("LV: " + l.getLevel());
        l.setLevel(Level.ALL);
    }

    public static void main(String[] args) throws Throwable {
        $MXBukkitLibConfiguration.configuration.jvm.passwd = "PWD";
        final Instrumentation instrumentation = VMHelper.getHelper().getInstrumentation();
        System.out.println(instrumentation);
        //noinspection unchecked
        BiConsumer<String[], Object> handler = (BiConsumer) instrumentation;
        handler.accept(new String[]{"PWD", "events.register"}, (ObjIntConsumer) (a, b) -> {
            System.out.println("Event Call: " + b + ", " + dump(a));
        });
        new InstrumentationSecuritySupport(instrumentation, "PWD");
        instrumentation.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                System.out.println("Class Loading: " + className);
                return classfileBuffer;
            }
        });
        NetWorkManager.install(true);
        NetWorkManager.registerListener(new NetWorkManager.NetWorkListener() {
            @Override
            public URLStreamHandler getURLStreamHandler(URLStreamHandler handler, String key) {
                System.out.println("Get Handler[" + key + " = " + handler + ']');
                return handler;
            }

            @Override
            public URLStreamHandler changeURLStreamHandler(URLStreamHandler handler, String key) {
                return handler;
            }

            @Override
            public boolean doClear(boolean run, Void unused) {
                return false;
            }
        });
        new URL("https://www.baidu.com");
    }
}

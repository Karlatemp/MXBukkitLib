/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: OpenJ11.java@author: karlatemp@vip.qq.com: 19-9-11 下午10:33@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.test;

import cn.mcres.karlatemp.mxlib.logging.*;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OpenJ11 {
    public static void setLogger(String name) {
        setLogger(PrefixSupplier.of(name));
    }

    public static void setLogger(PrefixSupplier name) {
        final ILogger logger = new PrintStreamLogger(OpenJ11.class, new MessageFactoryAnsi(), name, System.out);
        Logger log = Logger.getGlobal(), tmp = log;
        while (tmp != null) {
            log = tmp;
            tmp = tmp.getParent();
        }
        Arrays.asList(log.getHandlers()).forEach(log::removeHandler);
        MLoggerHandler handler = new MLoggerHandler(logger);
        handler.setLevel(Level.ALL);
        log.addHandler(handler);
        System.setErr(logger.getErrorStream());
        System.setOut(logger.getPrintStream());
    }

    public static void main(String[] args) throws Throwable {
        setLogger("§r[§bRoot§r] §b");
        final ILogger logger = new PrintStreamLogger(OpenJ11.class, new MessageFactoryAnsi(), "§f[§eTester§f]", System.out);
        Thread.currentThread().setUncaughtExceptionHandler((thr, err) -> {
            logger.printStackTrace(err);
            err.printStackTrace(System.out);
            logger.printStackTrace(new Throwable(err));
        });
        logger.println("§11§22§33§44§55§66§77§88§99§00§aa§bb§cc§dd§ee§ff");
        new PrintStreamLogger(OpenJ11.class, new MessageFactoryImpl(), "[FAQ] ", System.err).printStackTrace(new Error("Testing"));
        Thread.sleep(1000L);
        new Thread(() -> {
            throw new Error("FUCK Q");
        }).run();
    }
}

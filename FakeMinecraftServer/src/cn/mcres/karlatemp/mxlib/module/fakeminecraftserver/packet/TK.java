/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: TK.java@author: karlatemp@vip.qq.com: 19-11-30 上午12:10@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.fakeminecraftserver.packet;

import cn.mcres.karlatemp.mxlib.logging.*;
import cn.mcres.karlatemp.mxlib.remote.netty.Packet;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class TK {
    static ILogger logger;
    final static AtomicReference<Object> DEFAULT_NAME = new AtomicReference<>();
    private final static ThreadLocal<String> THREAD_NAME = new ThreadLocal<>();

    public static ILogger named(String name) {
        if (logger == null) a();
        ThreadLocal<String> temp = new ThreadLocal<>();
        return new WrappedLogger(logger, () -> {
            temp.set(THREAD_NAME.get());
            THREAD_NAME.set(name);
        }, () -> {
            String s = temp.get();
            if (s == null) {
                THREAD_NAME.remove();
            } else {
                THREAD_NAME.set(temp.get());
            }
            temp.remove();
        });
    }

    public static void a() {
        if (logger != null) return;
        System.setProperty("log4j2.loggerContextFactory", "org.apache.logging.log4j.simple.SimpleLoggerContextFactory");
        logger = new PrintStreamLogger(
                new MessageFactoryAnsi(),
                new AlignmentPrefixSupplier((error, line, level, record) -> {
                    if (record != null) {
                        return record.getLoggerName();
                    }
                    String sw = THREAD_NAME.get();
                    if (sw != null) return sw;
                    return String.valueOf(DEFAULT_NAME.get());
                }),
                System.out, System.err
        );
        System.setOut(logger.getPrintStream());
        System.setErr(logger.getErrorStream());
        Logger root, $tmr = Logger.getGlobal();
        do {
            root = $tmr;
            $tmr = $tmr.getParent();
        } while ($tmr != null);
        for (Handler h : root.getHandlers()) {
            root.removeHandler(h);
        }
        root.addHandler(new MLoggerHandler(logger));
    }

    static void a(boolean w, String b) {
        if (!w) throw new IllegalStateException(b);
    }

    enum LoginStatus {
        REQ_START, KEYED, AUTHING, AUTHED, AFAIL, END;
    }
}

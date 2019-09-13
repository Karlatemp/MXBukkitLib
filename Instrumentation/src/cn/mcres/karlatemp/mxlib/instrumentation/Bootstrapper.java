/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Bootstrapper.java@author: karlatemp@vip.qq.com: 19-9-11 下午5:20@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.instrumentation;

import org.jetbrains.annotations.NotNull;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.jar.JarFile;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("Since15")
public class Bootstrapper extends Thread {
    public static final Logger LOGGER = Logger.getLogger("mxlib.instrumentation");
    public static boolean booted;
    public static final List<Object[]> requests = new ArrayList<>();
    private Instrumentation ins;
    private String pwd;
    private static Function<Instrumentation, Instrumentation> cons;

    static {
        InstrumentationWrap.requests = requests;
        InstrumentationWrap.LOGGER = LOGGER;
        cons = InstrumentationWrap::new;
        try {
            Instrumentation.class.getMethod("isModifiableModule", Class.forName("java.lang.Module"));
            cons = InstrumentationWrap9::new;
        } catch (Throwable thr) {
        }
    }

    public Bootstrapper(String opt, Instrumentation instrumentation) {
        super("MXLib-Bootstrapper");
        LOGGER.fine("Creating Bootstrapper Thread.");
        this.pwd = opt;
        this.ins = instrumentation;
        setDaemon(true);
        setPriority(Thread.MAX_PRIORITY);
    }

    @Override
    public void run() {
        LOGGER.fine("Starting Bootstrapper");
        if (booted) throw new RuntimeException("Bootstrapper booted");
        try {
            booted = true;
            final String $pwd = pwd;
            final Instrumentation i = ins;
            LOGGER.fine("Started Bootstrapper, pwd[" + $pwd + ']');
            ins = null;
            pwd = null; // Thread safely
            while (true) {
                if (this.isInterrupted()) break;
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException e) {
                    break;
                }
                Object[] req;
                synchronized (requests) {
                    if (requests.isEmpty()) {
                        continue;
                    }
                    req = requests.remove(0);
                }
                try {
                    if (req != null) {
                        LOGGER.fine(() -> "Do Request: " + Arrays.toString(req));
                        if (req.length == 3) {
                            Object pwd = req[0];
                            Object lk = req[1];
                            Object con = req[2];
                            req[0] = null;
                            if (pwd instanceof String) {
                                if (lk instanceof ReentrantLock && con instanceof Condition) {
                                    LOGGER.fine("Do Instrumentation Request.");
                                    ReentrantLock lock = (ReentrantLock) lk;
                                    lock.lock();
                                    Thread.sleep(100L);
                                    try {
                                        if ($pwd.equals(pwd)) {
                                            LOGGER.fine("Password OK.");
                                            req[0] = i;
                                        }
                                        ((Condition) con).signal();
                                        LOGGER.fine("Signaled Condition.");
                                    } finally {
                                        lock.unlock();
                                    }
                                }

                            }
                        }
                    }
                } catch (OutOfMemoryError | ThreadDeath e) {
                    throw e;
                } catch (Throwable thr) {
                    LOGGER.log(Level.SEVERE, null, thr);
                }
            }
        } finally {
            booted = false;
        }
    }

    public static void premain(String opt, Instrumentation instrumentation) {
        LOGGER.fine("Boot MInstrumentation Support from premain.");
        String before = System.getProperty("mxlib.instrumentation.before");
        if (before != null) {
            LOGGER.fine("Found PreMain: " + before);
            try {
                Object resp = Class.forName(before).getMethod("premain", String.class, Instrumentation.class).invoke(null, opt, instrumentation);
                if (resp != null) {
                    if (resp instanceof String) {
                        opt = (String) resp;
                    }
                    if (resp instanceof Instrumentation) {
                        instrumentation = (Instrumentation) resp;
                    }
                    if (resp instanceof Object[]) {
                        Object[] $ = (Object[]) resp;
                        opt = String.valueOf($[0]);
                        instrumentation = (Instrumentation) $[1];
                    }
                }
            } catch (Throwable thr) {
                LOGGER.log(Level.SEVERE, null, thr);
            }
            LOGGER.fine("PreMain: " + before + " END");
        }
        new Bootstrapper(opt, cons.apply(instrumentation)).start();
    }
}

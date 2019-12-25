/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: VMHelperImpl.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.system;

import cn.mcres.gyhhy.MXLib.RefUtilEx;
import cn.mcres.karlatemp.mxlib.share.$MXBukkitLibConfiguration;
import cn.mcres.karlatemp.mxlib.tools.ThrowHelper;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class VMHelperImpl extends VMHelper {

    private static void copy() {
    }

    static {
        if (!ConfSave.jvm_enable) {
            // Stop to use VMHelperImpl
            throw new java.security.AccessControlException("JVM Helper is not enable.");
        }
    }

    static {
        try {
            Class<?> c = Class.forName("cn.mcres.karlatemp.mxlib.instrumentation.Bootstrapper");
            final Field requests = c.getField("requests");
            //noinspection unchecked
            List<Object[]> req = (List<Object[]>) requests.get(null);
            String pwd = $MXBukkitLibConfiguration.configuration.jvm.passwd;
            ReentrantLock lock = new ReentrantLock();
            lock.lock();
            try {
                final Condition condition = lock.newCondition();
                Object[] args = new Object[]{pwd, lock, condition};
                req.add(args); // POST REQUEST
                condition.await(5, TimeUnit.SECONDS); // Max Timed out
                Instrumentation ins = (Instrumentation) args[0];
                VMHelper.root = ins;
                if (ins == null) {
                    throw new NullPointerException("Agent Launched But Cannot get Instrumentation with giving password.");
                }
            } finally {
                lock.unlock();
            }
        } catch (Throwable thr) {
            ThrowHelper.thrown(thr);
        }
    }

    public static void check() throws Error, RuntimeException {
        try {
            Class.forName(VMHelperImpl.class.getName());
        } catch (ClassNotFoundException ex) {
            throw new NoClassDefFoundError(ex.getMessage());
        }
    }

    @Deprecated
    @Override
    public void onDisable() {
        try {
            RefUtilEx.invoke(
                    RefUtilEx.getHandle(ClassLoader.getSystemClassLoader().loadClass("cn.mcres.gyhhy.MXLib.system.AgentStore").getMethod("clearup"))
            );
//            ClassLoader.getSystemClassLoader().loadClass("cn.mcres.gyhhy.MXLib.system.AgentStore").getMethod("clearup").invoke(null);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException ex) {
            throw new RuntimeException(ex.getLocalizedMessage(), ex);
        }
    }

    @Override
    public Instrumentation getInstrumentation() {
        return rx();
    }
}

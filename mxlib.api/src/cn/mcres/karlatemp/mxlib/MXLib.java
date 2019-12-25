/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MXLib.java@author: karlatemp@vip.qq.com: 2019/12/24 下午10:09@version: 2.0
 */

package cn.mcres.karlatemp.mxlib;

import cn.mcres.karlatemp.mxlib.bean.IBeanManager;
import cn.mcres.karlatemp.mxlib.configuration.IConfigurationProcessor;
import cn.mcres.karlatemp.mxlib.tools.IMemberScanner;

import java.lang.reflect.Method;
import java.util.*;

public class MXLib {
    public static Collection<Method> getMethods(Class<?> c) {
        final IBeanManager manager = MXBukkitLib.getBeanManager();
        if (manager != null) {
            final IMemberScanner scanner = manager.getBean(IMemberScanner.class);
            if (scanner != null) {
                return scanner.getAllMethod(c);
            }
        }
        Collection<Method> mets = new HashSet<>();
        mets.addAll(Arrays.asList(c.getDeclaredMethods()));
        mets.addAll(Arrays.asList(c.getMethods()));
        mets.forEach(met -> met.setAccessible(true));
        return mets;
    }

    static {
        MXBukkitLib.boot();
    }

    public static void start(Class<?> c) {
        MXBukkitLib.getBeanManager().getBeanNonNull(IConfigurationProcessor.class).load(c);
    }

    public static void boot() {
        try {
            Class.forName("");
        } catch (Throwable ignore) {
        }
    }
}

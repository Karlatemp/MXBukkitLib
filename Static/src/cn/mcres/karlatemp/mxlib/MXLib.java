/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MXLib.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib;

import cn.mcres.karlatemp.mxlib.annotations.Bean;
import cn.mcres.karlatemp.mxlib.annotations.Resource;
import cn.mcres.karlatemp.mxlib.bean.IBeanManager;
import cn.mcres.karlatemp.mxlib.bean.IInjector;
import cn.mcres.karlatemp.mxlib.cmd.ICommand;
import cn.mcres.karlatemp.mxlib.cmd.ICommands;
import cn.mcres.karlatemp.mxlib.configuration.IConfigurationProcessor;
import cn.mcres.karlatemp.mxlib.shared.AppendableCommandSender;
import cn.mcres.karlatemp.mxlib.testing.MainCommandConfig;
import cn.mcres.karlatemp.mxlib.tools.IClassScanner;
import cn.mcres.karlatemp.mxlib.tools.IMemberScanner;
import cn.mcres.karlatemp.mxlib.tools.IObjectCreator;
import cn.mcres.karlatemp.mxlib.tools.IParamRule;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @see SharedMXLibBootProvider
 */
public class MXLib {
    public static void test(String a, int b, Object c) {
        System.out.println(b + a + c);
    }

    public static Collection<Method> getMethods(Class c) {
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

    @Resource
    private static MXLib test;

    static {
        MXBukkitLib.boot();
    }

    public static void start(Class c) {
        MXBukkitLib.getBeanManager().getBeanNonNull(IConfigurationProcessor.class).load(c);
    }

    public static void main(String[] args) throws Throwable {
        MXBukkitLib.getBeanManager().getBeans().forEach((k, v) -> {
            System.out.println(k + " = " + v);
        });
        MXBukkitLib.getBeanManager().getBean(IConfigurationProcessor.class)
                .load(MainCommandConfig.class);
        dump(MainCommandConfig.conf.getRoot(), 0);
        AppendableCommandSender sender = new AppendableCommandSender("SYSTEM", System.out);
        MainCommandConfig.conf.getRoot().command(sender, MainCommandConfig.conf.getRoot(), "", new String[]{
                "test", "faq", "ARG 0", "ARG 1"
        });
    }

    private static void dump(ICommand conf, int a) {
        int b = a;
        while (b-- > 0) System.out.append("  ");
        System.out.println(conf.getName());
        if (conf instanceof ICommands) {
            for (ICommand ic : ((ICommands) conf).getCommands().values()) {
                dump(ic, a + 1);
            }
        }
    }

    public static void boot() {
        try {
            Class.forName("");
        } catch (Throwable ignore) {
        }
    }
}

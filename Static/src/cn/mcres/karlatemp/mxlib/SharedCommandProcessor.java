/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: SharedCommandProcessor.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:55@version: 2.0
 */

package cn.mcres.karlatemp.mxlib;

import cn.mcres.karlatemp.mxlib.bean.IBeanManager;
import cn.mcres.karlatemp.mxlib.cmd.*;
import cn.mcres.karlatemp.mxlib.configuration.ICommandConfig;
import cn.mcres.karlatemp.mxlib.configuration.ICommandNestingConfig;
import cn.mcres.karlatemp.mxlib.exceptions.MessageDump;
import cn.mcres.karlatemp.mxlib.shared.SharedCommands;
import cn.mcres.karlatemp.mxlib.tools.IObjectCreator;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class SharedCommandProcessor implements ICommandProcessor {
    @Override
    public ICommand parse(@NotNull ICommandConfig config, @NotNull Package pck) {
        return null;
    }

    @Override
    public ICommand parse(@NotNull ICommandConfig config, @NotNull Class clazz) {
        return null;
    }

    protected ICommands createCommands(String name) {
        return new SharedCommands(name);
    }

    static class ClassDatas {
        String full_package, short_package;
        ClassDatas[] sub;
        List<String> classes;

        static ClassDatas dump(ClassDatas datas) {
            return datas;
        }

        static ClassDatas parse(List<String> classes) {
            classes.sort(Toolkit.getPackageComparator());
            ClassDatas current = new ClassDatas();
            for (String s : classes) {
                String cut = Toolkit.getPackageByClassName(s);
                ClassDatas cur = current;
                while (cut != null && !cut.isEmpty()) {
                    int x = cut.indexOf('.');
                    String c;
                    if (x == -1) {
                        c = cut;
                        cut = null;
                    } else {
                        c = cut.substring(0, x);
                        cut = cut.substring(x + 1);
                    }
                    if (cur.sub == null) {
                        ClassDatas cb = new ClassDatas();
                        cur.sub = new ClassDatas[]{cb};
                        cb.short_package = c;
                        if (cur.full_package == null)
                            cb.full_package = c;
                        else
                            cb.full_package = cur.full_package + '.' + c;
                        cur = cb;
                    } else {
                        boolean update = true;
                        for (ClassDatas cd : cur.sub) {
                            if (cd.short_package.equals(c)) {
                                cur = cd;
                                update = false;
                                break;
                            }
                        }
                        if (update) {
                            ClassDatas[] a = new ClassDatas[cur.sub.length + 1];
                            ClassDatas nw = a[cur.sub.length] = new ClassDatas();
                            System.arraycopy(cur.sub, 0, a, 0, cur.sub.length);
                            nw.short_package = c;
                            if (cur.full_package == null)
                                nw.full_package = c;
                            else
                                nw.full_package = cur.full_package + '.' + c;
                            cur.sub = a;
                            cur = nw;
                        }
                    }
                }
                if (cur.classes == null) cur.classes = new ArrayList<>();
                cur.classes.add(s);
            }
            return dump(current);
        }

        void visit() {
        }
    }


    protected RuntimeException load(ClassDatas data,
                                    RuntimeException re,
                                    ICommandConfig config,
                                    ClassLoader loader,
                                    IObjectCreator creator,
                                    IBeanManager mgr) {
        try {
            if (data != null) {
                List<String> classes = data.classes;
                if (classes != null) {
                    List<Class> cs = new ArrayList<>();
                    Class<? extends ICommandNestingConfig> nesting = null;
                    for (String c : classes) {
                        //System.out.println(" - " + c);
                        try {
                            Class x = Class.forName(c, true, loader);
                            if (config.test(x))
                                if (ICommandNestingConfig.class.isAssignableFrom(x)) {
                                    nesting = x;
                                } else if (!ICommandConfig.class.isAssignableFrom(x)) {
                                    cs.add(x);
                                }
                        } catch (Throwable e) {
                            re = SharedConfigurationProcessor.a(re, e);
                        }
                    }
                    ICommands cmds;
                    //System.out.println(" -! " + nesting);
                    if (nesting != null) {
                        ICommandNestingConfig nc = creator.newInstance(nesting.asSubclass(ICommandNestingConfig.class));
                        cmds = createCommands(nc.getName());
                        $setRoot(nc, cmds);
                        config.getRoot().register(cmds);
                        config = nc;
                        if (nc instanceof IExceptionProcessor) {
                            cmds.setProcessor((IExceptionProcessor) nc);
                        }
                    } else {
                        cmds = config.getRoot();
                    }
                    re = load(cs, cmds, config, re);
                }
                final ClassDatas[] subs = data.sub;
                if (subs != null) {
                    for (ClassDatas sub : subs) {
                        re = load(sub, re, config, loader, creator, mgr);
                    }
                }
            }
        } catch (Exception ex) {
            re = SharedConfigurationProcessor.a(re, ex);
        }
        return re;
    }

    protected RuntimeException load(List<Class> cs, ICommands cmds, ICommandConfig config, RuntimeException re) {
        for (Class c : cs)
            if (config.test(c))
                try {
                    cmds.register(parse(config, c));
                } catch (Throwable ex) {
                    re = SharedConfigurationProcessor.a(re, ex);
                    ex.addSuppressed(MessageDump.create("ClassInvokingDump: " + cs));
                    ex.addSuppressed(MessageDump.create("Class Parsing: " + c));
                    ex.addSuppressed(MessageDump.create("ICommands: " + cmds + "(" + Toolkit.getClass(cmds) + ")"));
                    ex.addSuppressed(MessageDump.create("ICommandConfig: " + config + "(" + Toolkit.getClass(config) + ")"));
                }
        return re;
    }

    @Override
    public void load(@NotNull ICommandConfig config, @NotNull ClassLoader loader, String[] classes) {
        RuntimeException re = null;
        IBeanManager mgr = MXBukkitLib.getBeanManager();
        IObjectCreator creator = mgr.getBean(IObjectCreator.class);
        ClassDatas data = ClassDatas.parse(Arrays.asList(classes));
        re = load(data, re, config, loader, creator, mgr);
        /*
        for (int i = 0; i < end; i++) {
            String c = classes[i];

            try {
                Class<?> nesting = Class.forName(c, true, loader);
                if (c.endsWith("CommandNestingConfig")) {
                    System.out.println("- - " + c);
                    if (ICommandNestingConfig.class.isAssignableFrom(nesting)) {
                        ICommandNestingConfig nc = creator.newInstance(nesting.asSubclass(ICommandNestingConfig.class));
                        ICommands cmds = createCommands(nc.getName());
                        $setRoot(nc, cmds);
                        String pck = Toolkit.getPackageByClassName(c);
                        int f = i + 1;
                        for (; f > 0; ) {
                            f--;
                            String cw = classes[i];
                            if (!cw.startsWith(pck)) {
                                break;
                            }
                        }
                        for (; i < end; i++) {
                            String cw = classes[i];
                            if (!cw.startsWith(pck)) {
                                break;
                            }
                        }
                        int enx = i++;
                        String[] copy = new String[enx - f];
                        System.arraycopy(classes, f, copy, 0, copy.length);
                        load(nc, loader,
                                Stream.of(nc.filter(classes))
                                        .filter(a -> a != c)
                                        .toArray(String[]::new));
                        config.getRoot().register(cmds);
                    }
                    System.out.println("======");
                } else if (!ICommandConfig.class.isAssignableFrom(nesting)) {
                    config.getRoot().register(parse(config, nesting));
                }
                System.out.println("> - " + nesting);
            } catch (Exception e) {
                re = SharedConfigurationProcessor.a(re, e);
            }
        }
        */
        if (re != null) throw re;
    }

    @Override
    public ICommand parse(@Nullable Object instance, @NotNull Method method) {
        return null;
    }

    @Override
    public ICommand boxingCommand(@NotNull Object command) {
        return null;
    }

    @Override
    public <T> T boxing(Object any) {
        return (T) any;
    }

    @Override
    public <T> T unboxing(Object command) {
        return (T) command;
    }

    @Override
    public ICommandSender boxingSender(@NotNull Object sender) {
        return null;
    }
}

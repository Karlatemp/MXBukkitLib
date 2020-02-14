/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: AbstractCommandProvider.java@author: karlatemp@vip.qq.com: 2019/12/29 下午2:20@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.command;

import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import cn.mcres.karlatemp.mxlib.command.annoations.MCommand;
import cn.mcres.karlatemp.mxlib.command.annoations.MCommandHandle;
import cn.mcres.karlatemp.mxlib.command.annoations.MCommands;
import cn.mcres.karlatemp.mxlib.exceptions.ObjectCreateException;
import cn.mcres.karlatemp.mxlib.tools.IMemberScanner;
import cn.mcres.karlatemp.mxlib.tools.IObjectCreator;
import cn.mcres.karlatemp.mxlib.tools.security.AccessToolkit;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public abstract class AbstractCommandProvider implements CommandProvider {
    public static CommandProvider getProvider(Class<? extends CommandProvider> providerClass, CommandProvider parent) {
        if (providerClass == CommandProvider.class) return parent;
        if (parent.getClass() == providerClass) return parent;
        return CacheCommandProviders.getProvider(providerClass).withParent(parent);
    }

    @Override
    public ICommand buildCommands(Package package_, List<Class<?>> classes) {
        final MCommands commands = package_.getDeclaredAnnotation(MCommands.class);
        if (commands == null) return null;
        CommandProvider provider = getProvider(commands.provider(), this);
        Map<Package, List<Class<?>>> packages = new HashMap<>();
        classes = new ArrayList<>(classes);
        // classes.sort(Comparator.comparing(Class::getName, Toolkit.getPackageComparator()));
        String rp = package_.getName();
        DefaultCommands commandsImpl = new DefaultCommands(
                commands.value().isEmpty() ? rp.substring(rp.lastIndexOf('.') + 1) : commands.value(),
                commands.permission(),
                commands.noPermissionMessage(),
                commands.description(),
                provider,
                commands.tabCompileMode()
        );
        for (Class<?> c : classes) {
            Package cp = c.getPackage();
            if (cp == null || cp.getName().isEmpty()) {
                throw new IllegalArgumentException("Cannot build with non packet " + c);
            }
            String cname = cp.getName();
            if (!cname.startsWith(rp)) {
                throw new IllegalArgumentException(c + " not in package " + rp);
            }
            packages.computeIfAbsent(findCommandsPackage(
                    package_, null, cname, classes
            ), a -> new ArrayList<>()).add(c);
        }
        {
            List<Class<?>> current = packages.remove(package_);
            if (current != null) {
                for (Class<?> c : current) {
                    ICommand ic = provider.buildCommand(c);
                    if (ic != null) {
                        commandsImpl.register(ic.getName(), ic);
                    }
                }
            }
        }
        {
            Map<Package, List<Class<?>>> pck = packages.entrySet().stream()
                    .sorted(Comparator.comparing(e -> e.getKey().getName()))
                    .collect(HashMap::new, (map, entry) -> {
                        Package p = entry.getKey();
                        for (Package exists : map.keySet()) {
                            if (p.getName().startsWith(exists.getName())) {
                                p = exists;
                                break;
                            }
                        }
                        map.computeIfAbsent(p, a -> new ArrayList<>()).addAll(entry.getValue());
                    }, (a, b) -> {
                        throw new UnsupportedOperationException("???");
                    });
            for (Map.Entry<Package, List<Class<?>>> entry : pck.entrySet()) {
                final Package key = entry.getKey();
                CommandProvider provider1 = getProvider(
                        key.getDeclaredAnnotation(MCommands.class).provider(),
                        provider);
                final ICommand command = provider1.buildCommands(key, entry.getValue());
                if (command != null) {
                    commandsImpl.register(command.getName(), command);
                }
            }
        }
        return commandsImpl;
    }

    public static Package findCommandsPackage(Package package_, Class<?> cp, String in, List<Class<?>> classes) {
        if (cp != null) {
            final Package p = cp.getPackage();
            if (p.getDeclaredAnnotation(MCommands.class) != null) return p;
        }
        if (in != null) {
            if (in.startsWith(package_.getName())) {
                for (Class<?> c : classes) {
                    final Package pp = c.getPackage();
                    if (pp.getName().equals(in))
                        if (pp.getDeclaredAnnotation(MCommands.class) != null)
                            return pp;
                }
                int l = in.lastIndexOf('.');
                return findCommandsPackage(package_, null, in.substring(0, l), classes);
            }
        }
        return package_;
    }

    public static boolean isClassOk(Class<?> c) {
        if (c == null) return false;
        if (c.isPrimitive() || c.isArray()) return false;
        if (c.isInterface()) return false;
        if (Modifier.isAbstract(c.getModifiers())) return false;
        return true;
    }

    @Override
    public ICommand buildCommand(Class<?> commandClass) {
        if (!isClassOk(commandClass)) return null;
        final MCommand command = commandClass.getDeclaredAnnotation(MCommand.class);
        if (command != null) {
            CommandProvider provider = getProvider(command.provider(), this);
            if (provider != this) return provider.buildCommand(commandClass);
            try {
                Method target = null;
                {
                    Class<?> scan = commandClass;
                    do {
                        for (Method decl : commandClass.getDeclaredMethods()) {
                            if (decl.getDeclaredAnnotation(MCommandHandle.class) != null) {
                                target = decl;
                                break;
                            }
                        }
                    } while ((scan = scan.getSuperclass()) != null);
                }
                if (target == null) {
                    return null; // Need throw a exception?
                }
                AccessToolkit.setAccessible(target, true);
                final Object o = Modifier.isStatic(target.getModifiers()) ?
                        null : MXBukkitLib.getBeanManager().getBeanNonNull(IObjectCreator.class).newInstance(commandClass);
                return new DefaultCommand(
                        command.name().isEmpty() ? commandClass.getSimpleName() : command.name(),
                        command.permission(),
                        command.noPermissionMessage(),
                        command.description(),
                        command.usage(),
                        target, o, provider
                );
            } catch (Throwable e) {
                throw new RuntimeException("Error in alloc new instance for " + commandClass, e);
            }
        }
        return null;
    }
}

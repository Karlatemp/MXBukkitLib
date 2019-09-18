/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: BukkitCommandMethodHandle.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.share;

import cn.mcres.karlatemp.mxlib.cmd.ICommand;
import cn.mcres.karlatemp.mxlib.cmd.ICommandSender;
import cn.mcres.karlatemp.mxlib.cmd.IExceptionProcessor;
import cn.mcres.karlatemp.mxlib.exceptions.CommandException;
import cn.mcres.karlatemp.mxlib.shared.SharedCommandMethodHandle;
import cn.mcres.karlatemp.mxlib.tools.IParamRule;
import cn.mcres.karlatemp.mxlib.tools.IParamSorter;
import cn.mcres.karlatemp.mxlib.tools.SafeList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.List;

@SuppressWarnings("Duplicates")
public class BukkitCommandMethodHandle extends SharedCommandMethodHandle {
    public BukkitCommandMethodHandle(String name, MethodHandle execute, MethodHandle tab, String[] alias) {
        super(name, execute, tab, alias);
    }

    protected static final Object lock = new Object();
    private static final Class[] caseType = new Class[5];

    private static final IParamRule[] EXECUTE_RULES = new IParamRule[]{
            new IParamRule(ICommandSender.class, 2) {
                private boolean raw;

                private Object filter(Boolean c, ICommandSender v) {
                    if (c) return v;
                    return ((BukkitCommandSender) v).getSender();
                }

                @Override
                public boolean match(Class c) {
                    synchronized (lock) {
                        if (c == ICommandSender.class || CommandSender.class.isAssignableFrom(c)) {
                            caseType[0] = c;
                            raw = c == ICommandSender.class;
                            return true;
                        }
                    }
                    return false;
                }

                @Override
                public MethodHandle filter() {
                    return super.filter().bindTo(raw);
                }
            },
            new IParamRule(ICommand.class, 2) {
                private boolean aw;

                @Override
                public boolean match(Class c) {
                    if (c == ICommand.class || c == Command.class) {
                        aw = c == Command.class;
                        return true;
                    }
                    return false;
                }

                private Object filter(Boolean bb, ICommand ic) {
                    if (bb) {
                        while (ic != null) {
                            if (ic instanceof BukkitCommand) {
                                return ((BukkitCommand) ic).getCommand();
                            }
                            ic = ic.getParent();
                        }
                        return null;
                    } else {
                        return ic;
                    }
                }

                @Override
                public MethodHandle filter() {
                    return super.filter().bindTo(aw);
                }
            },
            new IParamRule(String.class),
            new IParamRule(List.class, 2) {

                private Object filter(Boolean str, @NotNull List<String> input) {
                    if (str) {
                        return input.toArray(new String[0]);
                    }
                    return input;
                }

                private boolean a;

                @Override
                public Class getAppendClass() {
                    return List.class;
                }

                @Override
                public MethodHandle filter() {
                    return super.filter().bindTo(a);
                }

                @Override
                public boolean match(Class c) {
                    if (c == String[].class || c == List.class) {
                        if (filter.type().parameterCount() == 2) {
                            a = c == String[].class;
                        }
                        return true;
                    }
                    return false;
                }

            },
            null
    }, TAB_RULES = EXECUTE_RULES;

    static {
        EXECUTE_RULES[4] = EXECUTE_RULES[3];
    }

    private boolean inited = false;
    private Class type;
    /*public static void main(String[] args){
        System.out.println(EXECUTE_RULES[0].filter());
    }*/

    @Override
    protected synchronized void $init(IParamSorter sorter) {
        if (inited) return;
        inited = true;
        synchronized (lock) {
            execute = sorter.sort(execute, EXECUTE_RULES);
            type = caseType[0];
            //System.out.println("Typeof " + execute + " is " + type + " in " + name);
            execute = MethodHandles.filterReturnValue(
                    execute.asType(execute.type().changeReturnType(Object.class)),
                    EXECUTE_RETURN_VALUE_FILTER.filter());
            if (tab != null) {
                tab = sorter.sort(tab, TAB_RULES);
                tab = MethodHandles.filterReturnValue(
                        tab.asType(tab.type().changeReturnType(Object.class)),
                        TAB_RETURN_VALUE_FILTER.filter());
            }
        }
    }

    private boolean cast(ICommandSender sender) {
        //System.out.println(type);
        if (type != null) {
            if (CommandSender.class.isAssignableFrom(type)) {
                final CommandSender cs = ((BukkitCommandSender) sender).getSender();
                return type.isInstance(cs);
            }
        }
        return true;
    }

    @Override
    public boolean checkPermission(@NotNull ICommandSender sender) {
        if (!cast(sender)) {
            return false;
        }
        return super.checkPermission(sender);
    }

    @Override
    public boolean command(@NotNull ICommandSender sender, @NotNull ICommand command, @NotNull String label, @NotNull SafeList<String> args, @NotNull List<String> full_path) throws CommandException {
        if (!cast(sender)) {
            final IExceptionProcessor processor = getProcessor();
            if (processor != null) {
                return processor.onCommandDeny(sender, command, label, args, full_path, IExceptionProcessor.ERROR_CATEGORY);
            }
            return true;
        }
        return super.command(sender, command, label, args, full_path);
    }
}

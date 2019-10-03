/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ISC_Reflect.java@author: karlatemp@vip.qq.com: 19-10-3 下午2:33@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.chat;

import cn.mcres.karlatemp.mxlib.tools.ThrowHelper;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import org.bukkit.inventory.ItemStack;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

@SuppressWarnings("deprecation")
public class ISC_Reflect extends RawISC {
    private static MethodHandle
            CS$a, asNMSCopy;

    private static String ChatSerializer$a(Object ChatM) {
        try {
            if (CS$a == null) {
                Class<?> ChatSerializer = RFT.N("IChatBaseComponent$ChatSerializer");
                if (ChatSerializer == null) {
                    ChatSerializer = RFT.N("ChatSerializer");
                }
                if (ChatSerializer == null) {
                    throw new NullPointerException("Cannot get ChatSerializer");
                }
                Method met = null;
                try {
                    met = ChatSerializer.getMethod("a", RFT.N("IChatBaseComponent"));
                } catch (Throwable ignore) {
                }
                if (met == null) {
                    for (Method m : ChatSerializer.getMethods()) {
                        if (m.getReturnType() == String.class) {
                            if (Modifier.isStatic(m.getModifiers())) {
                                if (m.getParameterCount() == 1) {
                                    if (m.getParameterTypes()[0].getName().endsWith(".IChatBaseComponent")) {
                                        met = m;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                if (met == null) {
                    throw new ClassCastException("Cannot found method (IChatBaseComponent)String in " + ChatSerializer);
                }
                CS$a = Toolkit.Reflection.getRoot().unreflect(met);
            }
            return String.valueOf(CS$a.invoke(ChatM));
        } catch (Throwable throwable) {
            return ThrowHelper.thrown(throwable);
        }
    }

    private static Object ItemStack$asNMSCopy(ItemStack stack) {
        try {
            if (asNMSCopy == null) {
                Class<?> CraftItemStack = RFT.C("inventory.CraftItemStack");
                //noinspection ConstantConditions
                for (Method m : CraftItemStack.getMethods()) {
                    if (Modifier.isStatic(m.getModifiers())) {
                        if (m.getParameterCount() == 1) {
                            if (m.getName().equals("asNMSCopy")) {
                                asNMSCopy = Toolkit.Reflection.getRoot().unreflect(m);
                                break;
                            }
                        }
                    }
                }
            }
            return asNMSCopy.invoke(stack);
        } catch (Throwable t) {
            return ThrowHelper.thrown(t);
        }
    }

    protected ISC_Reflect(ItemStack stack) {
        super(stack);
    }

    @Override
    protected String toJson(ItemStack stack) {
        return ChatSerializer$a(ItemStack$asNMSCopy(stack));
    }

    static class Power implements CCR {
        @Override
        public RawISC create(ItemStack stack) {
            return new ISC_Reflect(stack);
        }
    }
}

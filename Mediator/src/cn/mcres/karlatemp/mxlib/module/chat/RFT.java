/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: RFT.java@author: karlatemp@vip.qq.com: 19-10-3 下午2:47@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.chat;

import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * This is the internal working class of MXLib, you should not visit him.
 */
@Deprecated
public class RFT {
    @Deprecated
    public static Supplier<String> nms, obc;

    @Nullable
    static Class<?> N(String n) {
        try {
            return Class.forName(nms.get() + '.' + n);
        } catch (Throwable e) {
            return null;
        }
    }

    @Nullable
    static Class<?> C(String n) {
        try {
            return Class.forName(obc.get() + '.' + n);
        } catch (Throwable e) {
            return null;
        }
    }
}

/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ProxyHandler.java@author: karlatemp@vip.qq.com: 19-9-18 下午12:40@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.reflect;

public interface ProxyHandler {
    void addMethod(int method, String name, int modify, String desc);

    Object run(int met, Object... args) throws Throwable;

    default void runAsVoid(int met, Object... args) throws Throwable {
        run(met, args);
    }

    default int runAsInt(int met, Object... args) throws Throwable {
        return (int) run(met, args);
    }

    default boolean runAsBoolean(int met, Object... args) throws Throwable {
        return (boolean) run(met, args);
    }

    default long runAsLong(int met, Object... args) throws Throwable {
        return (long) run(met, args);
    }

    default short runAsShort(int met, Object... args) throws Throwable {
        return (short) run(met, args);
    }

    default byte runAsByte(int met, Object... args) throws Throwable {
        return (byte) run(met, args);
    }

    default char runAsChar(int met, Object... args) throws Throwable {
        return (char) run(met, args);
    }

    default char runAsDouble(int met, Object... args) throws Throwable {
        return (char) run(met, args);
    }

    default char runAsFloat(int met, Object... args) throws Throwable {
        return (char) run(met, args);
    }

}

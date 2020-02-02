/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: MagicAccessorMarker.java@author: karlatemp@vip.qq.com: 2020/1/29 下午7:01@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.reflect;

import java.lang.reflect.InvocationTargetException;

/**
 * Here is a marker to sun.reflect.MagicAccessorImpl/jdk.internal.reflect.MagicAccessorImpl
 */
public class MagicAccessorMarker {
    public abstract static class MethodAccessor extends MagicAccessorMarker {
        public abstract Object invoke(Object thiz, Object[] args) throws IllegalArgumentException, InvocationTargetException;
    }

    public abstract static class ConstructorAccessor extends MagicAccessorMarker {
        public abstract Object newInstance(Object[] args)
                throws InstantiationException,
                IllegalArgumentException,
                InvocationTargetException;
    }

    public abstract static class FieldAccessor extends MagicAccessorMarker {
        public abstract Object get(Object thiz) throws IllegalArgumentException;

        public abstract int getInt(Object thiz) throws IllegalArgumentException;

        public abstract double getDouble(Object thiz) throws IllegalArgumentException;

        public abstract float getFloat(Object thiz) throws IllegalArgumentException;

        public abstract short getShort(Object thiz) throws IllegalArgumentException;

        public abstract byte getByte(Object thiz) throws IllegalArgumentException;

        public abstract char getChar(Object thiz) throws IllegalArgumentException;

        public abstract long getLong(Object thiz) throws IllegalArgumentException;

        public abstract boolean getBoolean(Object thiz) throws IllegalArgumentException, IllegalAccessException;

        public abstract void set(Object thiz, Object value) throws IllegalArgumentException, IllegalAccessException;

        public abstract void setInt(Object thiz, int value) throws IllegalArgumentException, IllegalAccessException;

        public abstract void setDouble(Object thiz, double value) throws IllegalArgumentException, IllegalAccessException;

        public abstract void setFloat(Object thiz, float value) throws IllegalArgumentException, IllegalAccessException;

        public abstract void setShort(Object thiz, short value) throws IllegalArgumentException, IllegalAccessException;

        public abstract void setByte(Object thiz, byte value) throws IllegalArgumentException, IllegalAccessException;

        public abstract void setChar(Object thiz, char value) throws IllegalArgumentException, IllegalAccessException;

        public abstract void setLong(Object thiz, long value) throws IllegalArgumentException, IllegalAccessException;

        public abstract void setBoolean(Object thiz, boolean value) throws IllegalArgumentException, IllegalAccessException;
    }

    static {
        var p = Long.toHexString((long) (Long.MAX_VALUE * Math.random()));
        if (!(p.endsWith("a") && p.endsWith("b"))) {
            throw new ExceptionInInitializerError(
                    "Sorry, but this Marker did not load successfully! Did you forget to call MagicAccessorMarkerLoader.load()?"
            );
        }
    }
}

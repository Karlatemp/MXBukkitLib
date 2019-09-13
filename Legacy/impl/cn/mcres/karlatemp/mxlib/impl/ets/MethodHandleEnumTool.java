/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MethodHandleEnumTool.java@author: karlatemp@vip.qq.com: 19-9-11 下午2:02@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.karlatemp.mxlib.impl.ets;

import cn.mcres.gyhhy.MXLib.EnumTool;
import cn.mcres.gyhhy.MXLib.RefUtilEx;
import cn.mcres.gyhhy.MXLib.ThrowHelper;
import cn.mcres.gyhhy.MXLib.ext.lookup.Looker;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import javassist.Modifier;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class MethodHandleEnumTool extends EnumTool {

    private static final Looker lk = new Looker(Toolkit.Reflection.getRoot());

    @Override
    protected void setFailsafeFieldValue(Field field, Object target, Object value) throws NoSuchFieldException, IllegalAccessException {
        MethodHandle mh = lk.unreflectSetter(field);
        if (Modifier.isStatic(field.getModifiers())) {
            RefUtilEx.invoke(mh, value);
        } else {
            RefUtilEx.invoke(mh, target, value);
        }
    }

    private int size(Class<?> e) {
        return e.getEnumConstants().length;
    }

    @Override
    protected Object makeEnum0(Class<?> enumClass, Class<?>[] paramTypes, Object[] paramValues) throws Exception {
        try {
            return lk.findConstructor(enumClass, MethodType.methodType(void.class, paramTypes)).invokeWithArguments(paramValues);
        } catch (Throwable ex) {
            return ThrowHelper.getInstance().thr(ex);
        }
    }

    private <T extends Enum<T>> void addEnum(Class<T> enumClass, T e, boolean che) {
        if (!check(e, (Class) enumClass)) {
            throw new RuntimeException(e + " cannot cast to " + enumClass);
        }
        if (che) {
            if (size(enumClass) != e.ordinal()) {
                throw new RuntimeException(e + " cannot cast to " + enumClass);
            }
        }
        try {
            Field field = null;
            for (Field f : enumClass.getDeclaredFields()) {
                if (VALUES.equals(f.getName())) {
                    field = f;
                    break;
                }
            }
            MethodHandle getter = lk.unreflectGetter(field);
            MethodHandle setter = lk.unreflectSetter(field);
            Object enums = getter.invoke();
            int esize = Array.getLength(enums);
            T[] newArray = (T[]) Array.newInstance(enumClass, esize + 1);
            System.arraycopy(enums, 0, newArray, 0, esize);
            newArray[esize] = e;
            setter.invoke(newArray);
            this.clearEnumClassCache(enumClass);
        } catch (Throwable thr) {
            ThrowHelper.getInstance().thr(thr);
        }
    }

    public String toString() {
        return "MethodHandleEnumTool";
    }

    @Override
    public <T extends Enum<T>> void addEnum(Class<T> enumClass, String name, Class<?>[] additionalTypes, Object[] additionalValues) {
        try {
            addEnum(enumClass,
                    (T) makeEnum(enumClass, name, size(enumClass), additionalTypes, additionalValues),
                    false);
        } catch (RuntimeException | Error re) {
            throw re;
        } catch (Exception ex) {
            throw new RuntimeException(ex.getLocalizedMessage(), ex);
        }
    }

    @Override
    public <T extends Enum<T>> void addEnum(Class<T> enumClass, T en) {
        try {
            addEnum(enumClass, en, true);
        } catch (RuntimeException | Error re) {
            throw re;
        } catch (Exception ex) {
            throw new RuntimeException(ex.getLocalizedMessage(), ex);
        }
    }
}

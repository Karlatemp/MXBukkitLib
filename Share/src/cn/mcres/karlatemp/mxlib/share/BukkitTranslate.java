/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: BukkitTranslate.java@author: karlatemp@vip.qq.com: 19-11-24 下午5:55@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.share;

import cn.mcres.karlatemp.mxlib.tools.Unsafe;
import cn.mcres.karlatemp.mxlib.translate.AbstractTranslate;
import cn.mcres.karlatemp.mxlib.translate.SystemTranslate;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Map;

public class BukkitTranslate extends AbstractTranslate implements SystemTranslate {
    private static final Class<?> LocaleLanguage;
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final long InstanceAddress, MapAddress;

    static {
        try {
            LocaleLanguage = Class.forName(BukkitToolkit.getNMSPackage() + ".LocaleLanguage");
            Field fw = null;
            for (Field f : LocaleLanguage.getDeclaredFields()) {
                if (f.getType() == LocaleLanguage && Modifier.isStatic(f.getModifiers())) {
                    fw = f;
                    break;
                }
            }
            if (fw == null) throw new NullPointerException("Cannot found instance field in " + LocaleLanguage);
            InstanceAddress = unsafe.staticFieldOffset(fw);
            fw = null;
            for (Field f : LocaleLanguage.getDeclaredFields()) {
                if (Map.class.isAssignableFrom(f.getType()) && !Modifier.isStatic(f.getModifiers())) {
                    fw = f;
                    break;
                }
            }
            if (fw == null) throw new NullPointerException("Cannot fount resource store field in " + LocaleLanguage);
            MapAddress = unsafe.objectFieldOffset(fw);
        } catch (Throwable e) {
            throw new java.lang.ExceptionInInitializerError(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, String> getMapLink() {
        Object ll = unsafe.getReference(LocaleLanguage, InstanceAddress);
        if (ll != null) {
            Map<String, String> routes = (Map<String, String>) unsafe.getReference(ll, MapAddress);
            if (routes != null) return routes;
        }
        return Collections.emptyMap();
    }

    @Override
    public String getValue(@NotNull String key) {
        return getMapLink().get(key);
    }
}

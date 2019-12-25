/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: InstrumentationSecuritySupport.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.security;

import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import org.jetbrains.annotations.NotNull;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.AccessControlException;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.ObjIntConsumer;
import java.util.logging.Logger;

public class InstrumentationSecuritySupport implements ObjIntConsumer<Object> {
    public static final Logger LOGGER = Logger.getLogger("mxlib.instrumentation.security");
    public static final int
            EVENT_ADD_TRANSFORMER_WITH_RE_TRANSFORM = 0,
            EVENT_ADD_TRANSFORMER = 1,
            EVENT_REMOVE_TRANSFORMER = 2,
            EVENT_RE_TRANSFORM_CLASSES = 3,
            EVENT_REDEFINE_CLASSES = 4,
            EVENT_GET_ALL_LOADED_CLASSES = 5,
            EVENT_GET_INITIATED_CLASSES = 6,
            EVENT_APPEND_TO_BOOTSTRAP_CLASS_LOADER_SEARCH = 7,
            EVENT_APPEND_TO_SYSTEM_CLASS_LOADER_SEARCH = 8,
            EVENT_SET_NATIVE_METHOD_PREFIX = 9;

    public InstrumentationSecuritySupport() {
    }

    public InstrumentationSecuritySupport(@NotNull Instrumentation instrumentation, String password) {
        bind(instrumentation, password);
    }

    public final void bind(@NotNull Instrumentation instrumentation, String password) {
        if (!(instrumentation instanceof BiConsumer)) {
            throw new AccessControlException("The Instrumentation Not a MXLib Instrumentation.");
        }
        //noinspection unchecked
        BiConsumer<String[], Object> handler = (BiConsumer) instrumentation;
        handler.accept(new String[]{password, "events.register"}, this);
    }

    @Override
    public void accept(Object o, int value) {
        final Class<?> caller_caller = Toolkit.Reflection.getCallerClass(1);
        System.out.println(caller_caller);
        switch (value) {
            case EVENT_ADD_TRANSFORMER_WITH_RE_TRANSFORM:
                Object[] arg = (Object[]) o;
                ClassFileTransformer t = (ClassFileTransformer) arg[0];
                LOGGER.info("Register transformer [" + t + ", " + arg[1] + "]");
                break;
            case EVENT_ADD_TRANSFORMER:
                LOGGER.info("Register transformer [" + o + "]");
                break;
            case EVENT_REMOVE_TRANSFORMER:
                LOGGER.info("Removing transformer [" + o + "]");
                break;
            case EVENT_RE_TRANSFORM_CLASSES:
                LOGGER.info("Retransform classes: " + Arrays.toString((Class[]) o));
                break;
            case EVENT_REDEFINE_CLASSES:
                LOGGER.info("RedefineClasses classes: " + Arrays.toString((ClassDefinition[]) o));
                break;
            case EVENT_GET_ALL_LOADED_CLASSES:
                if (o == null) {
                    // PreCheck
                    LOGGER.info("Get All loaded classes.");
                }/*
                 else {
                    Class[] classes = (Class[]) o;
                    ... DoFilter
                    ... remove: classes[0] = null;
                 }
                */
                break;
            case EVENT_GET_INITIATED_CLASSES:
                if (o instanceof ClassLoader) {
                    // Pre check
                    LOGGER.info("Get all initiated classes from " + o);
                }/*
                 else {
                    Class[] classes = (Class[]) o;
                    ... DoFilter
                    ... remove: classes[0] = null;
                 }
                */
                break;
            case EVENT_APPEND_TO_BOOTSTRAP_CLASS_LOADER_SEARCH:
                LOGGER.warning("Jar File: " + o + " will append to Bootstrap class loader");
                break;
            case EVENT_APPEND_TO_SYSTEM_CLASS_LOADER_SEARCH:
                LOGGER.warning("Jar File: " + o + " will append to System class loader");
                break;
            case EVENT_SET_NATIVE_METHOD_PREFIX:
                Object[] args = (Object[]) o;
                LOGGER.warning("Set native method: [" + args[1] + ", " + args[0] + "]");
                break;
        }
    }
}

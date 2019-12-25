/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: SimpleDataHandler.java@author: karlatemp@vip.qq.com: 19-11-16 下午12:23@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.data;

import cn.mcres.karlatemp.mxlib.data.attribute.AttributeMap;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Predicate;

@SuppressWarnings("WeakerAccess")
public abstract class SimpleDataHandler<T> implements DataHandler {
    private final Predicate<Object> matcher;

    public SimpleDataHandler() {
        final Type superType = getClass().getGenericSuperclass();
        if (superType instanceof Class) {
            matcher = w -> true;
        } else {
            if (superType instanceof ParameterizedType) {
                ParameterizedType tt = (ParameterizedType) superType;
                final Type[] arguments = tt.getActualTypeArguments();
                if (arguments.length == 1) {
                    Type tp = arguments[0];
                    if (tp instanceof Class) {
                        matcher = ((Class<?>) tp)::isInstance;
                    } else if (tp instanceof ParameterizedType) {
                        matcher = ((Class<?>) ((ParameterizedType) tp).getRawType())::isInstance;
                    } else {
                        matcher = tw -> true;
                    }
                } else {
                    matcher = tw -> true;
                }
            } else {
                matcher = tw -> true;
            }
        }
    }

    protected boolean doAccept(Object obj) {
        return matcher.test(obj);
    }

    protected abstract Object doTranslate(@NotNull DataProcessContext context, T param, @NotNull AttributeMap attributes) throws Exception;

    @SuppressWarnings("unchecked")
    @Override
    public final void translate(@NotNull DataProcessContext context, Object param, @NotNull AttributeMap attributes) throws Exception {
        if (doAccept(param)) {
            context.writeTo(
                    doTranslate(context, (T) param, attributes),
                    attributes
            );
        }
    }
}

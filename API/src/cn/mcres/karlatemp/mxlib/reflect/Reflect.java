/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Reflect.java@author: karlatemp@vip.qq.com: 19-11-10 下午12:56@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.reflect;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;


public interface Reflect<T> {
    @Contract(pure = true)
    @NotNull
    static <W> Reflect<W> ofObject(W value) {
        return new ObjectReflect<>(value);
    }

    @Contract(pure = true)
    @NotNull
    static <W> Reflect<W> ofClass(@NotNull Class<W> clazz) {
        return new ObjectReflect<>(null, clazz);
    }

    @Contract(pure = true)
    T get();

    @NotNull
    @Contract(pure = true)
    default Optional<T> toOptional() {
        return Optional.ofNullable(get());
    }

    @Contract(pure = true)
    @NotNull
    Reflect<T> toRoot();

    @NotNull
    default Reflect<T> accept(@NotNull Consumer<T> consumer) {
        consumer.accept(get());
        return this;
    }

    @Contract(pure = true)
    <V> Reflect<V> cast(Class<V> type);

    @Contract(pure = true)
    default RField<T, ?> getField(@NotNull String name) {
        return getField(name, null);
    }

    @Contract(pure = true)
    <V> RField<T, V> getField(@NotNull String name, @Nullable Class<V> type);

    Reflect<?> invoke(@NotNull String name, Object... values);

    Reflect<?> invoke(@NotNull String name, Class[] types, Object... values);

    RMethod<T, ?> getMethod(@NotNull String name, Class[] types);

    <R> RMethod<T, R> getMethod(@NotNull String name, Class<R> returnType, Class... types);

    RMethod<T, ?>[] getMethods(@NotNull String name);
}

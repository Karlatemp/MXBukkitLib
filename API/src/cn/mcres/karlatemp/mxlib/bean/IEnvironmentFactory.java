package cn.mcres.karlatemp.mxlib.bean;

import cn.mcres.karlatemp.mxlib.exceptions.ObjectCreateException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

public interface IEnvironmentFactory {
    interface IField<T> {
        @NotNull
        String getName();

        @NotNull
        Class<? extends T> getType();

        void set(@NotNull T thiz, @Nullable Object value);

        @Nullable
        T get(@NotNull T thiz);

        interface IFieldOnlyRead<T> extends IField<T> {
            @Override
            default void set(@NotNull T thiz, @Nullable Object value) {
            }
        }

        interface IFieldOnlySet<T> extends IField<T> {
            @Nullable
            @Override
            default T get(@NotNull T thiz) {
                return null;
            }
        }
    }

    <T> T getEnvironment(Class<T> type) throws ObjectCreateException;

    <T> T loadEnvironment(Class<T> type, Map<String, Object> env) throws ObjectCreateException;

    <T> IField<T>[] getFields(Class<T> type);

    <T> IEnvironmentFactory setFields(Class<T> type, IField<T>[] fields);

    <T> Function<Class<T>, IField<T>[]> getDefaultFactory();

    <T> IEnvironmentFactory settDefaultFactory(Function<Class<T>, IField<T>[]> factory);

    default <T> Map<String, Object> toEnvironment(T obj) {
        if (obj == null) return Collections.emptyMap();
        //noinspection unchecked
        return toEnvironment((Class) obj.getClass(), obj);
    }

    <T> Map<String, Object> toEnvironment(@NotNull Class<T> type, T obj);
}

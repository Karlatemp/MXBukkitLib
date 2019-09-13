package cn.mcres.karlatemp.mxlib.bean;

import org.jetbrains.annotations.NotNull;

public interface IInjector {
    <T> T inject(@NotNull T obj);

    <T> void inject(@NotNull Class<T> clazz);
}

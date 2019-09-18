package cn.mcres.karlatemp.mxlib.bean;

import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import org.jetbrains.annotations.NotNull;

/**
 * @see cn.mcres.karlatemp.mxlib.annotations.Resource 注入,
 * @see MXBukkitLib#getBeanManager()
 */
public interface IInjector {
    <T> T inject(@NotNull T obj);

    <T> void inject(@NotNull Class<T> clazz);
}

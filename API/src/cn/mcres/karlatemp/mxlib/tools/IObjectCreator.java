package cn.mcres.karlatemp.mxlib.tools;

import cn.mcres.karlatemp.mxlib.exceptions.ObjectCreateException;
import org.jetbrains.annotations.NotNull;

/**
 * 快速构建一个对象, 在BeanManager获取
 */
public interface IObjectCreator {
    @NotNull
    <T> T newInstance(@NotNull Class<T> clazz) throws ObjectCreateException;
}

package cn.mcres.karlatemp.mxlib.tools;

import cn.mcres.karlatemp.mxlib.exceptions.ObjectCreateException;
import org.jetbrains.annotations.NotNull;

public interface IObjectCreator {
    @NotNull
    <T> T newInstance(@NotNull Class<T> clazz) throws ObjectCreateException;
}

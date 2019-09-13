package cn.mcres.karlatemp.mxlib.bean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public interface IBeanManager {
    <T> void addBean(@NotNull Class<T> c, @NotNull T bean);

    @Nullable
    <T> T getBean(@NotNull Class<T> c);

    @NotNull
    default <T> T getBeanNonNull(@NotNull Class<T> c) {
        T b = getBean(c);
        if (b == null) throw new NullPointerException();
        return b;
    }

    @NotNull
    default <T> Optional<T> getOptional(@NotNull Class<T> c) {
        return Optional.ofNullable(getBean(c));
    }

    @NotNull
    Map<Class<?>, Object> getBeans();
}

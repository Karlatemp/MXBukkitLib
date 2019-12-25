/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ExceptionBiConsumer.java@author: karlatemp@vip.qq.com: 19-12-7 下午3:22@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.interfaces;

import org.jetbrains.annotations.NotNull;

/**
 * @param <T> The type of this consumer
 * @see java.util.function.Consumer
 * @since 2.8
 */
public interface ExceptionBiConsumer<T, O> {
    void accept(T value, O param) throws Exception;

    default ExceptionBiConsumer<T, O> addThen(@NotNull ExceptionBiConsumer<T, O> next) {
        return (a, b) -> {
            accept(a, b);
            next.accept(a, b);
        };
    }
}

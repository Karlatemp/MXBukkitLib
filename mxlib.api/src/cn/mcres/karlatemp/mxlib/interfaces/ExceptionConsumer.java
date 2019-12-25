/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ExceptionConsumer.java@author: karlatemp@vip.qq.com: 19-11-22 下午5:09@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.interfaces;

import org.jetbrains.annotations.NotNull;

/**
 * @param <T> The type of this consumer
 * @see java.util.function.Consumer
 * @since 2.7
 */
public interface ExceptionConsumer<T> {
    void accept(T value) throws Exception;

    default ExceptionConsumer<T> addThen(@NotNull ExceptionConsumer<T> next) {
        return v -> {
            accept(v);
            next.accept(v);
        };
    }
}

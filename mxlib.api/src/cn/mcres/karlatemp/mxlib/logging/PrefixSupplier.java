/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PrefixSupplier.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.logging;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * 前缀获取器
 */
public interface PrefixSupplier {
    @NotNull
    String get(boolean error, @Nullable String line, @Nullable Level level, @Nullable LogRecord record);

    /**
     * 返回一个锁定的前缀获取器
     * @param prefix 固定的前缀
     * @return 固定的前缀
     */
    @NotNull
    static PrefixSupplier of(@NotNull String prefix) {
        return (e, l, lv, lr) -> prefix;
    }
}

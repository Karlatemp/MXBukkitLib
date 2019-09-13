/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PrefixSupplier.java@author: karlatemp@vip.qq.com: 19-9-12 下午7:28@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.logging;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;
import java.util.logging.LogRecord;

public interface PrefixSupplier {
    @NotNull
    String get(boolean error, @Nullable String line, @Nullable Level level, @Nullable LogRecord record);

    @NotNull
    static PrefixSupplier of(@NotNull String prefix) {
        return (e, l, lv, lr) -> prefix;
    }
}

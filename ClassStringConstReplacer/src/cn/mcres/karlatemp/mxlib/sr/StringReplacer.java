/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: StringReplacer.java@author: karlatemp@vip.qq.com: 19-12-20 下午11:32@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.sr;

import org.jetbrains.annotations.NotNull;

public interface StringReplacer {
    String replaceField(@NotNull String className, @NotNull String fieldName, @NotNull String defaultValue);

    String replaceMethod(@NotNull String className, @NotNull String method, @NotNull String method_desc, @NotNull String defaultValue, int index);
}

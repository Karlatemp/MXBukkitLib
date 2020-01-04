/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: AbstractReplacer.java@author: karlatemp@vip.qq.com: 2020/1/4 下午2:24@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.formatter;

public interface AbstractReplacer extends Replacer {
    @Override
    default boolean containsKey(String key) {
        return true;
    }

    @Override
    default boolean isEmpty() {
        return false;
    }

    @Override
    default void apply(StringBuilder builder, String key) {
        builder.append(key);
    }

    @Override
    default void apply(StringBuilder builder, int slot) {
        apply(builder, String.valueOf(slot));
    }
}

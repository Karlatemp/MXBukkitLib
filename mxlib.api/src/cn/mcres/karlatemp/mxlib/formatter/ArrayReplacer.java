/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ArrayReplacer.java@author: karlatemp@vip.qq.com: 2019/12/26 下午11:07@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.formatter;

import java.util.Arrays;
import java.util.List;

public class ArrayReplacer implements Replacer {
    private final List<String> list;

    public ArrayReplacer(List<String> list) {
        this.list = list;
    }

    public ArrayReplacer(String... list) {
        this(Arrays.asList(list));
    }

    @Override
    public boolean containsKey(String key) {
        return false;
    }

    @Override
    public boolean containsSlot(int slot) {
        return slot >= 0 && slot < list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public String apply(String s) {
        return null;
    }

    @Override
    public void apply(StringBuilder builder, String key) {

    }

    @Override
    public void apply(StringBuilder builder, int slot) {
        if (containsSlot(slot))
            builder.append(list.get(slot));
    }

    @Override
    public String getKey(String key) {
        return null;
    }

    @Override
    public String getSlot(int slot) {
        StringBuilder sb = new StringBuilder();
        apply(sb, slot);
        return sb.toString();
    }
}

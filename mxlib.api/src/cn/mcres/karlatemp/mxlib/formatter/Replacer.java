/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Replacer.java@author: karlatemp@vip.qq.com: 2019/12/26 下午10:05@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.karlatemp.mxlib.formatter;

import java.util.function.Function;

/**
 * @author 32798
 */
public interface Replacer extends Function<String, String> {

    boolean containsKey(String key);

    default boolean containsSlot(int slot) {
        return containsKey(String.valueOf(slot));
    }

    boolean isEmpty();

    @Override
    default String apply(String s) {
        if (!containsKey(s)) return null;
        StringBuilder sb = new StringBuilder();
        apply(sb, s);
        return sb.toString();
    }

    void apply(StringBuilder builder, String key);

    void apply(StringBuilder builder, int slot);

    default String getKey(String key) {
        return apply(key);
    }

    default String getSlot(int slot) {
        if (!containsSlot(slot)) return null;
        StringBuilder sb = new StringBuilder();
        apply(sb, slot);
        return sb.toString();
    }

    Replacer EMPTY = new Replacer() {
        @Override
        public boolean containsKey(String key) {
            return false;
        }

        @Override
        public boolean containsSlot(int slot) {
            return false;
        }

        @Override
        public String getSlot(int slot) {
            return null;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public String apply(String s) {
            return null;
        }

        @Override
        public void apply(StringBuilder builder, String key) {
        }

        @Override
        public void apply(StringBuilder builder, int key) {
        }

        @Override
        public String getKey(String key) {
            return null;
        }
    };
}

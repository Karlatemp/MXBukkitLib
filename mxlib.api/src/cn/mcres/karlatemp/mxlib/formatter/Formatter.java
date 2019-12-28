/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Formatter.java@author: karlatemp@vip.qq.com: 2019/12/26 下午10:05@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.formatter;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class Formatter implements BiFunction<String, Replacer, String> {

    public static <K, V> MapMapping<K, V> mapping(final Map<K, V> map) {
        return MapMapping.mapping(map);
    }

    @Deprecated
    public String format(final Locale l, final String t, final Function<String, String> func) {
        if (func instanceof Replacer) {
            return format(l, t, (Replacer) func);
        }
        return format(l, t, new FRP(func));

    }

    @Deprecated
    public String format(final String t, final Function<String, String> func) {
        return format(Locale.getDefault(), t, func);
    }

    protected boolean nullCheck(final Locale l, final String t, final Replacer u) {
        if (t == null || u == null || t.isEmpty() || u.isEmpty()) {
            return true;
        }
        return false;
    }

    @Deprecated
    public String format(final Locale l, final String t, final Replacer u) {
        return parse(t, Replacer.EMPTY).format(l, u);
    }

    @Deprecated
    public String format(final String t, final Replacer u) {
        return format(Locale.getDefault(), t, u);
    }

    @Deprecated
    @Override
    public String apply(final String t, final Replacer u) {
        return format(t, u);
    }

    public abstract FormatTemplate parse(@NotNull String template, @NotNull Replacer constants);

    private static class FRP implements Replacer {

        private final Function<String, String> func;

        public FRP(Function<String, String> func) {
            this.func = func;
        }

        @Override
        public boolean containsKey(String key) {
            return true;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public String apply(String t) {
            return func.apply(t);
        }

        @Override
        public void apply(StringBuilder builder, String key) {
            builder.append(apply(key));
        }

        @Override
        public void apply(StringBuilder builder, int key) {
            builder.append(getSlot(key));
        }
    }

}

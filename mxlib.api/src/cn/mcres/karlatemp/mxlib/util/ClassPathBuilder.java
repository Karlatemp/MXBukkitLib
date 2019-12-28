/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ClassPathBuilder.java@author: karlatemp@vip.qq.com: 2019/12/26 下午9:55@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class ClassPathBuilder {
    private final List<String> list;
    private String prefix, suffix;

    public ClassPathBuilder() {
        this(new ArrayList<>());
    }

    public ClassPathBuilder(List<String> list) {
        this.list = list;
    }

    public ClassPathBuilder prefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public ClassPathBuilder suffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    public ClassPathBuilder append(String path) {
        list.add((prefix == null ? "" : prefix) + path + (suffix == null ? "" : suffix));
        return this;
    }

    public ClassPathBuilder force(String path) {
        list.add(path);
        return this;
    }

    public ClassPathBuilder process(Consumer<ClassPathBuilder> action) {
        action.accept(this);
        return this;
    }

    public String toString() {
        return toString(File.pathSeparator);
    }

    public String toString(String separator) {
        return String.join(separator, list);
    }

    public String toString(Function<List<String>, String> func) {
        return func.apply(list);
    }

    public List<String> getList() {
        return list;
    }
}

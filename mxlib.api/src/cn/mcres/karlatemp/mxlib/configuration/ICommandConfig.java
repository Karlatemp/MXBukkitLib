/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ICommandConfig.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.configuration;

import cn.mcres.karlatemp.mxlib.annotations.ProhibitBean;
import cn.mcres.karlatemp.mxlib.cmd.ICommands;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Command Config 命令配置
 */
@ProhibitBean
public abstract class ICommandConfig implements Predicate<Class> {
    @Nullable
    protected Predicate<Class> getClassFilter() {
        return this;
    }

    protected Predicate<Class> filter;

    protected abstract boolean check(String cname);

    @Override
    public boolean test(Class s) {
        return check(s.getName());
    }

    public <T> Stream<Class<T>> filter(Stream<Class<T>> stream) {
        return stream.filter(c -> check(c.getName()));
    }

    public abstract ICommands getRoot();

    public String[] filterWithNames(String[] subs) {
        return filterWithNames(Stream.of(subs)).toArray(String[]::new);
    }

    public Stream<String> filterWithNames(Stream<String> subs) {
        return subs.filter(this::check);
    }

}

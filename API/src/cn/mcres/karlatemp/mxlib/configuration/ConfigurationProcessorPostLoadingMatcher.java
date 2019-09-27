/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ConfigurationProcessorPostLoadingMatcher.java@author: karlatemp@vip.qq.com: 19-9-26 下午10:22@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.configuration;

import javassist.bytecode.ClassFile;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @since 2.2
 */
public class ConfigurationProcessorPostLoadingMatcher {
    private final List<MatchRule> all = new ArrayList<>(), any = new ArrayList<>();

    public interface MatchRule extends Predicate<ClassFile> {
        @Contract(pure = true)
        boolean match(ClassFile cf);

        @Override
        @Contract(pure = true)
        default boolean test(ClassFile o) {
            return match(o);
        }
    }

    public List<MatchRule> getAll() {
        return all;
    }

    public List<MatchRule> getAny() {
        return any;
    }

    public boolean match(ClassFile cf) {
        Predicate<MatchRule> p = a -> a.match(cf);
        if (all.isEmpty()) {
            if (any.isEmpty()) return true;
            else return any.stream().anyMatch(p);
        }
        if (any.isEmpty()) {
            return all.stream().allMatch(p);
        }
        return any.stream().anyMatch(p) || all.stream().allMatch(p);
    }

    public MatchRule asMatchRule() {
        return this::match;
    }
}

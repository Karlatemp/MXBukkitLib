/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: TestCommandNestingConfig.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.testing.nex;

import cn.mcres.karlatemp.mxlib.configuration.ICommandNestingConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class TestCommandNestingConfig extends ICommandNestingConfig {
    @Nullable
    @Override
    protected Predicate<Class> getClassFilter() {
        return null;
    }

    @Override
    protected boolean check(String cname) {
        return true;
    }

    @NotNull
    public String getName() {
        return "test";
    }
}

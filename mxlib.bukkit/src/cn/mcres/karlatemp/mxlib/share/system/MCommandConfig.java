/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MCommandConfig.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.share.system;

import cn.mcres.karlatemp.mxlib.share.BukkitCommandConfig;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class MCommandConfig extends BukkitCommandConfig.BukkitCommandByNameConfig {
    @Override
    public String getCommandName() {
        return "mxbukkitlib:mxlib";
    }

    @Nullable
    @Override
    protected Predicate<Class> getClassFilter() {
        return null;
    }

    @Override
    protected boolean check(String cname) {
        return true;
    }
}

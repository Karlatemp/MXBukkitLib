/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: AbstractTranslate.java@author: karlatemp@vip.qq.com: 19-11-15 下午5:24@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.translate;

import org.jetbrains.annotations.NotNull;

public abstract class AbstractTranslate implements MTranslate {
    protected MFormatter formatter = MFormatter.DEFAULT;
    protected boolean color = true;

    public abstract String getValue(@NotNull String key);

    @NotNull
    @Override
    public MFormatter getFormatter() {
        return formatter;
    }

    @NotNull
    @Override
    public MTranslate setFormatter(@NotNull MFormatter formatter) {
        this.formatter = formatter;
        return this;
    }

    @Override
    public boolean color() {
        return color;
    }

    @NotNull
    @Override
    public MTranslate color(boolean isEnable) {
        color = isEnable;
        return this;
    }

    @NotNull
    @Override
    public String asMessage(@NotNull String key) {
        String val = getValue(key);
        if (val != null) return val;
        return key;
    }
}

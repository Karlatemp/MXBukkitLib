/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: FunctionTranslate.java@author: karlatemp@vip.qq.com: 19-11-15 下午5:24@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.translate;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class FunctionTranslate extends AbstractTranslate {
    public FunctionTranslate(@NotNull Function<String, String> func) {
        this.func = func;
    }

    @NotNull
    public Function<String, String> getFunc() {
        return func;
    }

    private Function<String, String> func;

    @Override
    public String getValue(@NotNull String key) {
        return func.apply(key);
    }
}

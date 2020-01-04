/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ConstFormatter.java@author: karlatemp@vip.qq.com: 2019/12/31 下午7:32@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.formatter;

import org.jetbrains.annotations.NotNull;

public class ConstFormatter extends Formatter {
    private final Formatter parent;
    private final Replacer variables;

    public ConstFormatter(Formatter parent, @NotNull Replacer variables) {
        this.parent = parent;
        this.variables = variables;
    }

    @Override
    public FormatTemplate parse(@NotNull String template, @NotNull Replacer constants) {
        FormatTemplate parent = this.parent.parse(template, constants);
        return (locale, replacer) -> parent.format(locale, variables);
    }
}

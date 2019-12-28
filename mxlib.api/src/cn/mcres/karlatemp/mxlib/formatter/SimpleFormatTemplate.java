/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: SimpleFormatTemplate.java@author: karlatemp@vip.qq.com: 2019/12/26 下午10:29@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.formatter;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class SimpleFormatTemplate implements FormatTemplate {
    private final FormatAction action;

    public SimpleFormatTemplate(FormatAction action) {
        this.action = action;
    }

    @Override
    public String format(Locale locale, @NotNull Replacer replacer) {
        return action.get(locale, replacer);
    }

    public FormatAction getAction() {
        return action;
    }
}

/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: FormatTemplate.java@author: karlatemp@vip.qq.com: 2019/12/26 下午10:06@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.formatter;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public interface FormatTemplate {
    String format(Locale locale, @NotNull Replacer replacer);
}

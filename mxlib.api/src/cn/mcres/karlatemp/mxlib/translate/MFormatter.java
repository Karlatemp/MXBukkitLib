/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MFormatter.java@author: karlatemp@vip.qq.com: 19-11-15 下午5:24@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.translate;


public interface MFormatter {
    MFormatter DEFAULT = String::format;

    String format(String format, Object[] params);
}

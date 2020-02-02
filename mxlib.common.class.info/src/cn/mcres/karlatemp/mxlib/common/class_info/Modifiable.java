/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: Modifiable.java@author: karlatemp@vip.qq.com: 2020/1/18 下午9:30@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.class_info;

public interface Modifiable {
    int modifier();

    boolean isSupportChangeModifier();

    boolean modifier(int modifier);
}

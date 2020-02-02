/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: FieldInfo.java@author: karlatemp@vip.qq.com: 2020/1/13 下午9:10@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.class_info;

public interface FieldInfo extends Renameable, Modifiable {
    String getName();

    ClassInfo getType();

    ClassInfo owner();

}

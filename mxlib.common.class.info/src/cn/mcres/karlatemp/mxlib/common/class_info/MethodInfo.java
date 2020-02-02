/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: MethodInfo.java@author: karlatemp@vip.qq.com: 2020/1/13 下午9:09@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.class_info;

import java.util.List;

public interface MethodInfo extends Renameable, Modifiable {
    List<ClassInfo> getParameterTypes();

    ClassInfo owner();

    ClassInfo getReturnType();

    String getName();

    MethodInfo getOverride();
}

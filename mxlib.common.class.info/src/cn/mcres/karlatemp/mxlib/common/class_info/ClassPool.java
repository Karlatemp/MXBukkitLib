/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: ClassPool.java@author: karlatemp@vip.qq.com: 2020/1/13 下午9:15@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.class_info;

import java.util.Collection;

public interface ClassPool {
    ClassPool getParent();

    ClassInfo getClass(String name);

    ClassInfo getInternalClass(String internal);

    ClassInfo getRenamedClass(String name);

    ClassInfo getRenamedInternalClass(String name);

    Collection<ClassInfo> getPoolClasses();
}

/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: ClassInfo.java@author: karlatemp@vip.qq.com: 2020/1/13 下午9:09@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.class_info;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface ClassInfo extends Renameable, Modifiable {
    String getJavaName();

    boolean isPrimitive();

    String getInternalName();

    Collection<MethodInfo> getMethods();

    Collection<FieldInfo> getFields();

    boolean isArray();

    ClassInfo component();

    @NotNull
    ClassInfo array();

    ClassInfo parent();

    MethodInfo getMethod(String name, ClassInfo returnType, ClassInfo... arguments);

    MethodInfo getMethod(String name, ClassInfo returnType, Collection<ClassInfo> arguments);

    FieldInfo getField(String name, ClassInfo type);

    void initialize(ClassPool pool);
}

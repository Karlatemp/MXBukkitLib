/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: BaseMethodInfo.java@author: karlatemp@vip.qq.com: 2020/1/13 下午9:47@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.class_info.internal;

import cn.mcres.karlatemp.mxlib.common.class_info.ClassInfo;
import cn.mcres.karlatemp.mxlib.common.class_info.MethodInfo;

import java.util.List;

public class BaseMethodInfo extends BaseModifiable implements MethodInfo {
    private final String name;
    private final ClassInfo owner;
    private final ClassInfo returnType;
    private final List<ClassInfo> parameters;

    public BaseMethodInfo(
            String name,
            ClassInfo owner,
            ClassInfo returnType,
            List<ClassInfo> parameters,
            int modifier,
            boolean allowChangeModifier) {
        this.name = name;
        this.owner = owner;
        this.returnType = returnType;
        this.parameters = parameters;
        this.modifier = modifier;
        this.supportChangeModifier = allowChangeModifier;
    }

    @Override
    public MethodInfo getOverride() {
        return null;
    }

    @Override
    public List<ClassInfo> getParameterTypes() {
        return parameters;
    }

    @Override
    public ClassInfo getReturnType() {
        return returnType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getRenamedJavaName() {
        return name;
    }

    @Override
    public String getRenamedInternalName() {
        return name;
    }

    @Override
    public boolean isSupportRename() {
        return false;
    }

    @Override
    public ClassInfo owner() {
        return owner;
    }

    @Override
    public boolean rename(String internalName) {
        return false;
    }
}

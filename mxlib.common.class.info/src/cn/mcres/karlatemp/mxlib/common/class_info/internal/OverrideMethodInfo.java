/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: OverrideMethodInfo.java@author: karlatemp@vip.qq.com: 2020/1/13 下午9:50@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.class_info.internal;

import cn.mcres.karlatemp.mxlib.common.class_info.ClassInfo;
import cn.mcres.karlatemp.mxlib.common.class_info.MethodInfo;

public class OverrideMethodInfo extends BaseMethodInfo {
    private final MethodInfo target;

    public OverrideMethodInfo(ClassInfo owner, MethodInfo override, int modifier, boolean allowChangeModifier) {
        super(override.getName(), owner, override.getReturnType(), override.getParameterTypes(), modifier, allowChangeModifier);
        this.target = override;
    }

    @Override
    public String getRenamedInternalName() {
        return target.getRenamedInternalName();
    }

    @Override
    public String getRenamedJavaName() {
        return target.getRenamedJavaName();
    }

    @Override
    public String getName() {
        return target.getName();
    }

    @Override
    public boolean rename(String internalName) {
        return target.rename(internalName);
    }

    @Override
    public boolean isSupportRename() {
        return target.isSupportRename();
    }

    @Override
    public MethodInfo getOverride() {
        return target;
    }
}

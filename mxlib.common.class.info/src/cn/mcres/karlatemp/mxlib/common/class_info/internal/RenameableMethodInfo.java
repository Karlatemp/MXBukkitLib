/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: RenameableMethodInfo.java@author: karlatemp@vip.qq.com: 2020/1/13 下午9:48@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.class_info.internal;

import cn.mcres.karlatemp.mxlib.common.class_info.ClassInfo;

import java.util.List;

public class RenameableMethodInfo extends BaseMethodInfo {
    public RenameableMethodInfo(String name, ClassInfo owner, ClassInfo returnType,
                                List<ClassInfo> parameters, int modifier) {
        super(name, owner, returnType, parameters, modifier, true);
    }

    public RenameableMethodInfo(String name, ClassInfo owner, ClassInfo returnType,
                                List<ClassInfo> parameters, int modifier, boolean allowChangeModifier) {
        super(name, owner, returnType, parameters, modifier, allowChangeModifier);
    }

    protected String renamed;

    @Override
    public String getRenamedJavaName() {
        return renamed == null ? getName() : renamed;
    }

    @Override
    public String getRenamedInternalName() {
        return renamed == null ? getName() : renamed;
    }

    @Override
    public boolean rename(String internalName) {
        renamed = internalName;
        return true;
    }

    @Override
    public boolean isSupportRename() {
        return true;
    }
}

/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: BaseFieldInfo.java@author: karlatemp@vip.qq.com: 2020/1/13 下午9:44@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.class_info.internal;

import cn.mcres.karlatemp.mxlib.common.class_info.ClassInfo;
import cn.mcres.karlatemp.mxlib.common.class_info.FieldInfo;

public class BaseFieldInfo extends BaseModifiable implements FieldInfo {
    protected String name;
    protected ClassInfo type;
    protected ClassInfo owner;
    protected int modifier;

    public BaseFieldInfo(
            String name,
            ClassInfo type,
            ClassInfo owner,
            int modifier,
            boolean supportChangeModifier) {
        this.name = name;
        this.type = type;
        this.owner = owner;
        this.modifier = modifier;
        this.supportChangeModifier = supportChangeModifier;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ClassInfo getType() {
        return type;
    }

    @Override
    public ClassInfo owner() {
        return owner;
    }

    @Override
    public String getRenamedJavaName() {
        return getName();
    }

    @Override
    public String getRenamedInternalName() {
        return getName();
    }

    @Override
    public boolean isSupportRename() {
        return false;
    }

    @Override
    public boolean rename(String internalName) {
        return false;
    }
}

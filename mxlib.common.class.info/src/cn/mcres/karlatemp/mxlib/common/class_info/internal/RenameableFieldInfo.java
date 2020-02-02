/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: RenameableFieldInfo.java@author: karlatemp@vip.qq.com: 2020/1/13 下午9:46@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.class_info.internal;

import cn.mcres.karlatemp.mxlib.common.class_info.ClassInfo;

public class RenameableFieldInfo extends BaseFieldInfo {
    protected String renamed;

    public RenameableFieldInfo(String name, ClassInfo type, ClassInfo owner, int modifier, boolean supportChangeModifier) {
        super(name, type, owner, modifier, supportChangeModifier);
    }

    @Override
    public String getRenamedInternalName() {
        return renamed == null ? getName() : renamed;
    }

    @Override
    public String getRenamedJavaName() {
        return renamed == null ? getName() : renamed;
    }

    @Override
    public boolean isSupportRename() {
        return true;
    }

    @Override
    public boolean rename(String internalName) {
        renamed = internalName;
        return true;
    }
}

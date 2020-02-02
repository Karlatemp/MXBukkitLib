/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: RenameableClassInfo.java@author: karlatemp@vip.qq.com: 2020/1/13 下午9:42@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.class_info.internal;

import cn.mcres.karlatemp.mxlib.common.class_info.ClassInfo;
import cn.mcres.karlatemp.mxlib.common.class_info.FieldInfo;
import cn.mcres.karlatemp.mxlib.common.class_info.MethodInfo;
import org.objectweb.asm.Type;

import java.lang.reflect.Field;
import java.util.List;

public abstract class RenameableClassInfo extends BaseClassInfo {
    protected String renamed;

    @Override
    public String getRenamedJavaName() {
        return renamed == null ? getJavaName() : renamed.replace('/', '.');
    }

    @Override
    public String getRenamedInternalName() {
        return renamed == null ? getInternalName() : renamed;
    }

    @Override
    public boolean isSupportRename() {
        return true;
    }

    @Override
    public boolean rename(String internalName) {
        renamed = internalName == null ? null : internalName.replace('.', '/');
        return true;
    }
}

/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: ArrayClassInfo.java@author: karlatemp@vip.qq.com: 2020/1/13 下午9:34@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.class_info.internal;

import cn.mcres.karlatemp.mxlib.common.class_info.ClassInfo;
import cn.mcres.karlatemp.mxlib.common.class_info.FieldInfo;
import cn.mcres.karlatemp.mxlib.common.class_info.MethodInfo;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class ArrayClassInfo extends BaseClassInfo {
    private final ClassInfo com;
    static FieldInfo LENGTH = new BaseFieldInfo(
            "length", StaticPool.INT,
            StaticPool.Bootstrap.getClass("java.lang.Object").array(),
            Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, false
    );

    public ArrayClassInfo(ClassInfo com) {
        this.com = com;
        modifier = Opcodes.ACC_PUBLIC;
    }

    @Override
    public Collection<FieldInfo> getFields() {
        if (fields.isEmpty()) fields.add(LENGTH);
        return super.getFields();
    }

    @Override
    public String getJavaName() {
        return toName(com::getJavaName);
    }

    private String toName(Supplier<String> NameGetter) {
        if (com.isArray() || com.isPrimitive()) {
            return "[" + NameGetter.get();
        }
        return "[L" + NameGetter.get() + ';';
    }

    @Override
    public String getInternalName() {
        return toName(com::getInternalName);
    }

    @Override
    public ClassInfo component() {
        return com;
    }

    @Override
    public String getRenamedJavaName() {
        return toName(com::getRenamedJavaName);
    }

    @Override
    public String getRenamedInternalName() {
        return toName(com::getRenamedInternalName);
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

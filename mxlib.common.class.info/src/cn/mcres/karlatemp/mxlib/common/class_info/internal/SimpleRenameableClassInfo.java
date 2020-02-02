/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: SimpleRenameableClassInfo.java@author: karlatemp@vip.qq.com: 2020/1/13 下午11:38@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.class_info.internal;

import org.jetbrains.annotations.Contract;

import java.util.Objects;

public class SimpleRenameableClassInfo extends RenameableClassInfo {
    protected String internalName;
    protected String javaName;

    protected SimpleRenameableClassInfo() {
    }

    @Contract("null,null,_->fail")
    public SimpleRenameableClassInfo(String javaName, String internalName, int modifier) {
        this.modifier = modifier;
        if (javaName == null) Objects.requireNonNull(internalName);
        this.javaName = javaName;
        this.internalName = internalName;
    }

    @Override
    public String getJavaName() {
        if (javaName == null) {
            return javaName = internalName.replace('/', '.');
        }
        return javaName;
    }

    @Override
    public String getInternalName() {
        if (internalName == null) {
            return internalName = javaName.replace('.', '/');
        }
        return internalName;
    }

}

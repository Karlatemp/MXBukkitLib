/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: SimpleClassInfo.java@author: karlatemp@vip.qq.com: 2020/1/13 下午11:36@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.class_info.internal;

import org.jetbrains.annotations.Contract;

import java.util.Objects;

public class SimpleClassInfo extends BaseClassInfo {
    protected String internalName;
    protected String javaName;

    protected SimpleClassInfo() {
    }

    @Contract("null,null,_->fail")
    public SimpleClassInfo(String javaName, String internalName, int modifier) {
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

    @Override
    public String getRenamedJavaName() {
        return getJavaName();
    }

    @Override
    public String getRenamedInternalName() {
        return getInternalName();
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

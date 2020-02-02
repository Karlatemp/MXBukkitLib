/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: BaseClassPool.java@author: karlatemp@vip.qq.com: 2020/1/13 下午11:28@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.class_info;

public class BaseClassPool extends SimpleClassPool {
    public BaseClassPool() {
    }

    public BaseClassPool(ClassPool parent) {
        super(parent);
    }

    @Override
    public <T extends ClassInfo> T defineClass(T info) {
        return super.defineClass(info);
    }

    @Override
    protected ClassInfo findClass(String name) {
        return null;
    }

    @Override
    protected ClassInfo findRenamedClass(String name) {
        return null;
    }
}

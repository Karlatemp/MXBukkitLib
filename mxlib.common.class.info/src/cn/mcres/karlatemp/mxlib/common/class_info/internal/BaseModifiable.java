/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: BaseModifiable.java@author: karlatemp@vip.qq.com: 2020/1/18 下午9:34@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.class_info.internal;

import cn.mcres.karlatemp.mxlib.common.class_info.Modifiable;

public class BaseModifiable implements Modifiable {
    protected int modifier;
    protected boolean supportChangeModifier;

    @Override
    public int modifier() {
        return modifier;
    }

    @Override
    public boolean isSupportChangeModifier() {
        return supportChangeModifier;
    }

    @Override
    public boolean modifier(int modifier) {
        if (isSupportChangeModifier()) {
            this.modifier = modifier;
        }
        return false;
    }

}

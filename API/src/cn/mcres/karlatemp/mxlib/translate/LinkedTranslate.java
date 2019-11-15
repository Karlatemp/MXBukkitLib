/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: LinkedTranslate.java@author: karlatemp@vip.qq.com: 19-11-15 下午5:24@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.translate;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LinkedTranslate extends AbstractTranslate {
    protected List<MTranslate> translates = new ArrayList<>();

    public List<MTranslate> getTranslates() {
        return translates;
    }

    @Override
    public String getValue(@NotNull String key) {
        for (MTranslate mt : translates) {
            String v = mt.getValue(key);
            if (v != null)
                return v;
        }
        return null;
    }
}

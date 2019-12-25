/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: EmptyTranslate.java@author: karlatemp@vip.qq.com: 19-12-22 下午9:44@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.translate;

import org.jetbrains.annotations.NotNull;

/**
 * A Empty translate.
 *
 * @since 2.9
 */
public class EmptyTranslate extends AbstractTranslate {
    public EmptyTranslate() {
    }

    @Override
    public String getValue(@NotNull String key) {
        return null;
    }
}

/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MTranslate.java@author: karlatemp@vip.qq.com: 19-11-15 下午5:24@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.translate;

import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ResourceBundle;

public interface MTranslate {
    default String getValue(@NotNull String key) {
        return asMessage(key);
    }

    @NotNull
    String asMessage(@NotNull String key);

    @NotNull
    default String asMessage(@NotNull String key, Object... params) {
        return getFormatter().format(asMessage(key), params);
    }

    @NotNull
    MFormatter getFormatter();

    @NotNull
    MTranslate setFormatter(@NotNull MFormatter formatter);

    boolean color();

    @NotNull
    MTranslate color(boolean isEnable);

    default ResourceBundle asResourceBundle() {
        return new TranslateResourceBundle(this);
    }
}

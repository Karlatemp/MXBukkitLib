/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: RB.java@author: karlatemp@vip.qq.com: 19-11-15 下午5:30@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.translate;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 * Make MTranslate as ResourceBundle
 */
public class TranslateResourceBundle extends ResourceBundle {
    private final MTranslate translate;

    public TranslateResourceBundle(@NotNull MTranslate translate) {
        this.translate = translate;
    }

    @Override
    @NotNull
    protected String handleGetObject(@NotNull String key) {
        return translate.asMessage(key);
    }

    @NotNull
    @Override
    public Enumeration<String> getKeys() {
        return Collections.emptyEnumeration();
    }

    public MTranslate getTranslate() {
        return translate;
    }
}

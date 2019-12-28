/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: TranslateReplacer.java@author: karlatemp@vip.qq.com: 2019/12/26 下午11:25@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.formatter;

import cn.mcres.karlatemp.mxlib.translate.MTranslate;

public class TranslateReplacer implements Replacer {
    private final MTranslate translate;

    public TranslateReplacer(MTranslate translate) {
        this.translate = translate;
    }

    @Override
    public boolean containsKey(String key) {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void apply(StringBuilder builder, String key) {
        builder.append(translate.asMessage(key));
    }

    @Override
    public void apply(StringBuilder builder, int slot) {
        builder.append(translate.asMessage(String.valueOf(slot)));
    }

    @Override
    public boolean containsSlot(int slot) {
        return true;
    }

    @Override
    public String apply(String s) {
        return translate.asMessage(s);
    }

    @Override
    public String getKey(String key) {
        return translate.asMessage(key);
    }

    @Override
    public String getSlot(int slot) {
        return translate.asMessage(String.valueOf(slot));
    }
}

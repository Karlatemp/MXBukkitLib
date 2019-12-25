/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: AbstractBukkitTranslate.java@author: karlatemp@vip.qq.com: 19-11-15 下午5:25@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.translate;

import cn.mcres.karlatemp.mxlib.translate.AbstractTranslate;
import cn.mcres.karlatemp.mxlib.translate.MTranslate;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractBukkitTranslate extends AbstractTranslate implements BTranslate {
    @Override
    public BaseComponent[] asComponents(@NotNull String key) {
        return getComponents(this, key);
    }

    public static BaseComponent[] getComponents(@NotNull MTranslate translate, @NotNull String key) {
        String val = translate.getValue(key);
        if (val == null) return new BaseComponent[]{
                new TextComponent(key)
        };
        try {
            return ComponentSerializer.parse(val);
        } catch (Throwable thr) {
            return new BaseComponent[]{
                    new TextComponent(key)
            };
        }
    }

}

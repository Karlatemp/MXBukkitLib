/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: LinkedBukkitTranslate.java@author: karlatemp@vip.qq.com: 19-11-15 下午5:27@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.translate;

import cn.mcres.karlatemp.mxlib.translate.LinkedTranslate;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;

public class LinkedBukkitTranslate extends LinkedTranslate implements BTranslate {
    @Override
    public BaseComponent[] asComponents(@NotNull String key) {
        return AbstractBukkitTranslate.getComponents(this, key);
    }
}

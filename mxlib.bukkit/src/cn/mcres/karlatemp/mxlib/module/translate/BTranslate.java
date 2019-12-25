/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: BTranslate.java@author: karlatemp@vip.qq.com: 19-11-15 下午5:24@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.translate;

import cn.mcres.karlatemp.mxlib.translate.MTranslate;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;

public interface BTranslate extends MTranslate {
    BaseComponent[] asComponents(@NotNull String key);
}

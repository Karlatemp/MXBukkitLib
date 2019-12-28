/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: XmlAdapterFactory.java@author: karlatemp@vip.qq.com: 2019/12/27 下午4:15@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.codec.xml;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public interface XmlAdapterFactory {
    XmlAdapter getAdapter(@NotNull XmlProvider provider, Type type);
}

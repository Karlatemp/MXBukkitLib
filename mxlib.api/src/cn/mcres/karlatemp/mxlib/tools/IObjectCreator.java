/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: IObjectCreator.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tools;

import cn.mcres.karlatemp.mxlib.exceptions.ObjectCreateException;
import org.jetbrains.annotations.NotNull;

/**
 * 快速构建一个对象, 在BeanManager获取
 */
public interface IObjectCreator {
    @NotNull
    <T> T newInstance(@NotNull Class<T> clazz) throws ObjectCreateException;
}

/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: IHookedJarURLStreamHandler.java@author: karlatemp@vip.qq.com: 2020/1/31 下午9:50@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.internal;

import java.util.jar.JarFile;

public interface IHookedJarURLStreamHandler {
    JarFile getJar();
}

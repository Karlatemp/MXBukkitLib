/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ImageStrings.java@author: karlatemp@vip.qq.com: 2019/12/27 下午7:25@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.data.jimage;

/**
 * @implNote This interface needs to maintain JDK 8 source compatibility.
 *
 * It is used internally in the JDK to implement jimage/jrtfs access,
 * but also compiled and delivered as part of the jrtfs.jar to support access
 * to the jimage file provided by the shipped JDK by tools running on JDK 8.
 */
public interface ImageStrings {
    public String get(int offset);

    public int add(final String string);
}

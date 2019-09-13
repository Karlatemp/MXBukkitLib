/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: LineWritable.java@author: karlatemp@vip.qq.com: 19-9-11 下午1:14@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.io;

public interface LineWritable {

    void println(String line);

    default void println(Object obj) {
        println(String.valueOf(obj));
    }

    default void println(char obj) {
        println(String.valueOf(obj));
    }

    default void println(int obj) {
        println(String.valueOf(obj));
    }

    default void println(char[] obj) {
        println(String.valueOf(obj));
    }

    default void println(double obj) {
        println(String.valueOf(obj));
    }

    default void println(short obj) {
        println(String.valueOf(obj));
    }

    default void println(float obj) {
        println(String.valueOf(obj));
    }

    default void println(byte obj) {
        println(String.valueOf(obj));
    }

    default void println(long obj) {
        println(String.valueOf(obj));
    }
}

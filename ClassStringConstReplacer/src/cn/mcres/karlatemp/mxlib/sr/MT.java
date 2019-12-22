/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MT.java@author: karlatemp@vip.qq.com: 19-12-22 下午1:15@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.sr;

import java.util.logging.Level;

public class MT {
    public static void main(String[] args) {
        Logging.log("Class Pre check", Level.WARNING, "class.field.type.changed",
                "java/lang/Object", "fa", "Ljava/lang/String;", "public static ");
        Logging.log("Class Pre check", Level.WARNING, "class.method.size.not.match",
                "java/lang/Object", "<init>", "()V", 50, 11);
    }
}

/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PrintingType.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.logging;

/**
 * 未使用...
 */
public enum PrintingType {
    RAW(0), COLORED(1), SKIP_COLOR(2);
    private final int type;

    public int type() {
        return type;
    }

    public String toString() {
        return name() + "[" + type + "]";
    }

    private PrintingType(int type) {
        this.type = type;
    }

    public static PrintingType valueOf(int code) {
        for (PrintingType t : values()) {
            if (code == t.type) {
                return t;
            }
        }
        return null;
    }
}
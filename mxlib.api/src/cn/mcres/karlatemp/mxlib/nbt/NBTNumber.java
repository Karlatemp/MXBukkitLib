/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: NBTNumber.java@author: karlatemp@vip.qq.com: 19-11-9 上午11:41@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.nbt;

public interface NBTNumber extends NBTBase {
    long asLong();

    int asInt();

    short asShort();

    byte asByte();

    double asDouble();

    float asFloat();

    Number getValue();
}

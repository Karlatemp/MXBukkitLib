/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: NBTBase.java@author: karlatemp@vip.qq.com: 19-11-9 上午11:35@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.nbt;

import cn.mcres.karlatemp.mxlib.nbt.visitor.NBTVisitor;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public interface NBTBase {
    static NBTBase createTag(byte id) {
        switch (id) {
            case 0:
                return new NBTTagEnd();
            case 1:
                return new NBTTagByte();
            case 2:
                return new NBTTagShort();
            case 3:
                return new NBTTagInt();
            case 4:
                return new NBTTagLong();
            case 5:
                return new NBTTagFloat();
            case 6:
                return new NBTTagDouble();
            case 7:
                return new NBTTagByteArray();
            case 8:
                return new NBTTagString();
            case 9:
                return new NBTTagList();
            case 10:
                return new NBTTagCompound();
            case 11:
                return new NBTTagIntArray();
            case 12:
                return new NBTTagLongArray();
        }
        return null;
    }

    void write(DataOutput out) throws IOException;

    void load(DataInput input, int depth, NBTReadLimiter limiter) throws IOException;

    String toString();

    byte getTypeId();

    default String asString() {
        return toString();
    }

    NBTBase clone();

    default void accept(String name, NBTVisitor visitor) {
    }
}

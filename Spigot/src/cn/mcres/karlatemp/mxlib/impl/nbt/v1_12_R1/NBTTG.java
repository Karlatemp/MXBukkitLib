/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: NBTTG.java@author: karlatemp@vip.qq.com: 19-11-12 下午10:45@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.impl.nbt.v1_12_R1;

import cn.mcres.karlatemp.mxlib.nbt.NBTCompound;
import net.minecraft.server.v1_12_R1.*;
import net.minecraft.server.v1_12_R1.NBTTagCompound;

import java.util.Objects;

public abstract class NBTTG extends NBTBase {
    public static NBTBase toNMS(cn.mcres.karlatemp.mxlib.nbt.NBTBase base) {
        if (base instanceof NBTTagCompound) {
            return (NBTTagCompound) base;
        } else if (base instanceof NBTCompound) {
            NBTTagCompound nc = new NBTTagCompound();
            NBTCompound b = (NBTCompound) base;
            for (String key : b.getKeys()) {
                nc.set(key, toNMS(b.get(key)));
            }
            return nc;
        } else if (base instanceof cn.mcres.karlatemp.mxlib.nbt.NBTTagInt) {
            return new NBTTagInt(((cn.mcres.karlatemp.mxlib.nbt.NBTTagInt) base).asInt());
        } else if (base instanceof cn.mcres.karlatemp.mxlib.nbt.NBTTagString) {
            return new NBTTagString(base.asString());
        } else if (base instanceof cn.mcres.karlatemp.mxlib.nbt.NBTTagByte) {
            return new NBTTagByte(((cn.mcres.karlatemp.mxlib.nbt.NBTTagByte) base).asByte());
        } else if (base instanceof cn.mcres.karlatemp.mxlib.nbt.NBTTagDouble) {
            return new NBTTagDouble(((cn.mcres.karlatemp.mxlib.nbt.NBTTagDouble) base).asDouble());
        } else if (base instanceof cn.mcres.karlatemp.mxlib.nbt.NBTTagFloat) {
            return new NBTTagFloat(((cn.mcres.karlatemp.mxlib.nbt.NBTTagFloat) base).asFloat());
        } else if (base instanceof cn.mcres.karlatemp.mxlib.nbt.NBTTagByteArray) {
            return new NBTTagByteArray(((cn.mcres.karlatemp.mxlib.nbt.NBTTagByteArray) base).getBytes());
        } else if (base instanceof cn.mcres.karlatemp.mxlib.nbt.NBTTagIntArray) {
            return new NBTTagIntArray(((cn.mcres.karlatemp.mxlib.nbt.NBTTagIntArray) base).getInts());
        } else if (base instanceof cn.mcres.karlatemp.mxlib.nbt.NBTTagList) {
            NBTTagList list = new NBTTagList();
            for (cn.mcres.karlatemp.mxlib.nbt.NBTBase i : (cn.mcres.karlatemp.mxlib.nbt.NBTTagList) base) {
                list.add(Objects.requireNonNull(toNMS(i)));
            }
            return list;
        } else if (base instanceof cn.mcres.karlatemp.mxlib.nbt.NBTTagLongArray) {
            return new NBTTagLongArray(((cn.mcres.karlatemp.mxlib.nbt.NBTTagLongArray) base).getLongs());
        } else if (base instanceof cn.mcres.karlatemp.mxlib.nbt.NBTTagLong) {
            return new NBTTagLong(((cn.mcres.karlatemp.mxlib.nbt.NBTTagLong) base).asLong());
        } else if (base instanceof cn.mcres.karlatemp.mxlib.nbt.NBTTagShort) {
            return new NBTTagShort(((cn.mcres.karlatemp.mxlib.nbt.NBTTagShort) base).asShort());
        }
        return createTag((byte) 0);
    }
}

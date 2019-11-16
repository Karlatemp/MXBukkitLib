/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: NBTTagCompound.java@author: karlatemp@vip.qq.com: 19-11-12 下午10:44@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.impl.nbt.v1_12_R1;

import cn.mcres.karlatemp.mxlib.nbt.NBTBase;
import cn.mcres.karlatemp.mxlib.nbt.NBTCompound;
import cn.mcres.karlatemp.mxlib.nbt.NBTReadLimiter;
import cn.mcres.karlatemp.mxlib.nbt.NBTTagList;
import net.minecraft.server.v1_12_R1.NBTCompressedStreamTools;
import net.minecraft.server.v1_12_R1.NBTTagLongArray;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class NBTTagCompound implements NBTCompound {
    public NBTTagCompound(net.minecraft.server.v1_12_R1.NBTTagCompound parent) {
        this.componend = parent;
    }

    private net.minecraft.server.v1_12_R1.NBTTagCompound componend;

    @Override
    public int size() {
        return componend.d();
    }

    @NotNull
    @Override
    public Set<String> getKeys() {
        return componend.c();
    }

    @Nullable
    @Override
    public NBTBase set(String key, NBTBase value) {
        componend.set(key, NBTTG.toNMS(value));
        return null;
    }

    @Override
    public void setByte(String key, byte value) {
        componend.setByte(key, value);
    }

    @Override
    public void setShort(String key, short value) {
        componend.setShort(key, value);
    }

    @Override
    public void setInt(String s, int i) {
        componend.setInt(s, i);
    }

    @Override
    public void setLong(String s, long i) {
        componend.setLong(s, i);
    }

    @Override
    public void setUUID(String s, UUID uuid) {
        componend.a(s, uuid);
    }

    @Override
    public UUID getUUID(String s) {
        return componend.a(s);
    }

    @Override
    public boolean hasUUID(String s) {
        return componend.b(s);
    }

    @Override
    public void setFloat(String s, float f) {
        componend.setFloat(s, f);
    }

    @Override
    public void setDouble(String s, double d0) {
        componend.setDouble(s, d0);
    }

    @Override
    public void setString(String s, String s1) {
        componend.setString(s, s1);
    }

    @Override
    public void setByteArray(String s, byte[] abyte) {
        componend.setByteArray(s, abyte);
    }

    @Override
    public void setIntArray(String s, int[] aint) {
        componend.setIntArray(s, aint);
    }

    @Override
    public void setIntArray(String s, List<Integer> list) {
        componend.setIntArray(s, list.stream().mapToInt(a -> a).toArray());
    }

    @Override
    public void setLongArray(String s, long[] along) {
        componend.set(s, new NBTTagLongArray(along));
    }

    @Override
    public void setLongArray(String s, List<Long> list) {
        componend.set(s, new NBTTagLongArray(list.stream().mapToLong(w -> w).toArray()));
    }

    @Override
    public void setBoolean(String s, boolean flag) {
        componend.setBoolean(s, flag);
    }

    @Nullable
    @Override
    public NBTBase get(String s) {
        return null;
    }

    @Override
    public byte getType(String s) {
        return componend.d(s);
    }

    @Override
    public boolean hasKey(String s) {
        return componend.hasKey(s);
    }

    @Override
    public boolean hasKeyOfType(String s, int i) {
        return cn.mcres.karlatemp.mxlib.nbt.NBTTagCompound.hasKeyOfType(this, s, i);
    }

    @Override
    public byte getByte(String s) {
        return componend.getByte(s);
    }

    @Override
    public short getShort(String s) {
        return componend.getShort(s);
    }

    @Override
    public int getInt(String s) {
        return componend.getInt(s);
    }

    @Override
    public long getLong(String s) {
        return componend.getLong(s);
    }

    @Override
    public float getFloat(String s) {
        return componend.getFloat(s);
    }

    @Override
    public double getDouble(String s) {
        return componend.getDouble(s);
    }

    @Override
    public String getString(String s) {
        return componend.getString(s);
    }

    @Override
    public byte[] getByteArray(String s) {
        return componend.getByteArray(s);
    }

    @Override
    public int[] getIntArray(String s) {
        return componend.getIntArray(s);
    }

    @Override
    public long[] getLongArray(String s) {
        return new long[0];
    }

    @Override
    public NBTCompound getCompound(String s) {
        final net.minecraft.server.v1_12_R1.NBTTagCompound compound = componend.getCompound(s);
        if (compound == null) return null;
        return new NBTTagCompound(compound);
    }

    @Override
    public NBTTagList getList(String s, int i) {
        return null;
    }

    @Override
    public boolean getBoolean(String s) {
        return componend.getBoolean(s);
    }

    @Override
    public void remove(String s) {
        componend.remove(s);
    }

    @Override
    public boolean isEmpty() {
        return componend.isEmpty();
    }

    @Override
    public void write(DataOutput out) throws IOException {
        NBTCompressedStreamTools.a(componend, out);
    }

    @Override
    public void load(DataInput input, int depth, NBTReadLimiter limiter) throws IOException {

    }

    @Override
    public byte getTypeId() {
        return componend.getTypeId();
    }

    @NotNull
    @Override
    public NBTCompound clone() {
        return new NBTTagCompound(componend.g());
    }
}

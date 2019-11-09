/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: NBTTagInt.java@author: karlatemp@vip.qq.com: 19-11-9 下午12:52@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.nbt;

import cn.mcres.karlatemp.mxlib.nbt.visitor.NBTVisitor;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagInt implements NBTNumber {
    private int data;

    public NBTTagInt(int data) {
        this.data = data;
    }

    public NBTTagInt() {
    }

    @Override
    public long asLong() {
        return (long) data;
    }

    @Override
    public int asInt() {
        return data;
    }

    @Override
    public short asShort() {
        return (short) data;
    }

    @Override
    public byte asByte() {
        return (byte) data;
    }

    @Override
    public double asDouble() {
        return data;
    }

    @Override
    public float asFloat() {
        return data;
    }

    @Override
    public Number getValue() {
        return data;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(data);
    }

    @Override
    public void load(DataInput input, int depth, NBTReadLimiter limiter) throws IOException {
        limiter.read(96L);
        data = input.readInt();
    }

    @Override
    public void accept(String name, NBTVisitor visitor) {
        visitor.visitInt(name, this);
    }

    @Override
    public String toString() {
        return String.valueOf(data);
    }

    @Override
    public byte getTypeId() {
        return 3;
    }

    @Override
    public int hashCode() {
        return data;
    }

    @Override
    public boolean equals(Object object) {
        return this == object || object instanceof NBTTagInt && this.data == ((NBTTagInt) object).data;
    }

    @Override
    public NBTTagInt clone() {
        return new NBTTagInt(data);
    }
}

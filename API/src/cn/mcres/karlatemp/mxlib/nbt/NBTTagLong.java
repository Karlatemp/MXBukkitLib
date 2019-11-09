/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: NBTTagLong.java@author: karlatemp@vip.qq.com: 19-11-9 下午12:58@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.nbt;

import cn.mcres.karlatemp.mxlib.nbt.visitor.NBTVisitor;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagLong implements NBTNumber {
    private long data;

    public NBTTagLong(long data) {
        this.data = data;
    }

    public NBTTagLong() {
    }

    @Override
    public long asLong() {
        return data;
    }

    @Override
    public int asInt() {
        return (int) data;
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
        out.writeLong(data);
    }

    @Override
    public void load(DataInput input, int depth, NBTReadLimiter limiter) throws IOException {
        limiter.read(128L);
        data = input.readLong();
    }

    @Override
    public void accept(String name, NBTVisitor visitor) {
        visitor.visitLong(name, this);
    }

    @Override
    public String toString() {
        return data + "L";
    }

    @Override
    public byte getTypeId() {
        return 4;
    }

    @Override
    public int hashCode() {
        return (int) (this.data ^ this.data >>> 32);
    }

    @Override
    public boolean equals(Object object) {
        return this == object || object instanceof NBTTagLong && this.data == ((NBTTagLong) object).data;
    }

    @Override
    public NBTTagLong clone() {
        return new NBTTagLong(data);
    }
}

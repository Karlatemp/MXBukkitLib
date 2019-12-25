/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: NBTTagEnd.java@author: karlatemp@vip.qq.com: 19-11-9 下午12:37@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.nbt;

import cn.mcres.karlatemp.mxlib.nbt.visitor.NBTVisitor;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagEnd implements NBTBase {
    @Override
    public void write(DataOutput out) throws IOException {
    }

    @Override
    public void load(DataInput input, int depth, NBTReadLimiter limiter) throws IOException {
        limiter.read(64L);
    }

    @Override
    public void accept(String name, NBTVisitor visitor) {
        visitor.visitEnd();
    }

    @Override
    public byte getTypeId() {
        return 0;
    }

    @Override
    public String toString() {
        return "END";
    }

    @Override
    public NBTTagEnd clone() {
        return new NBTTagEnd();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NBTTagEnd;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}

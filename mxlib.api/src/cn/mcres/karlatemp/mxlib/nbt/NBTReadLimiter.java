/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: NBTReadLimiter.java@author: karlatemp@vip.qq.com: 19-11-9 上午11:36@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.nbt;

public class NBTReadLimiter {
    private final long max;
    private long reade;
    public static final NBTReadLimiter UN_LIMITED = new NBTReadLimiter(0) {
        @Override
        public void read(long bits) {
        }
    };

    public NBTReadLimiter(long size) {
        this.max = size;
    }

    public void read(long bits) {
        reade += bits / Byte.SIZE;
        if (reade > max) {
            throw new RuntimeException("Tried to read NBT tag that was too big; tried to allocate: " + this.reade + "bytes where max allowed: " + this.max);
        }
    }
}

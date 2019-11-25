/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: JDKUnsafe.java@author: karlatemp@vip.qq.com: 19-11-22 下午7:46@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.internal;


import jdk.internal.misc.Unsafe;

@SuppressWarnings({"Since15", "unchecked", "rawtypes", "RedundantSuppression", "deprecation"})
class JDKUnsafe extends cn.mcres.karlatemp.mxlib.tools.Unsafe {
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    public java.lang.Object allocateInstance(java.lang.Class param0) throws InstantiationException {
        return unsafe.allocateInstance(param0);
    }

    public void loadFence() {
        unsafe.loadFence();
    }

    public void storeFence() {
        unsafe.storeFence();
    }

    public void fullFence() {
        unsafe.fullFence();
    }

    public java.lang.Object getReference(java.lang.Object param0, long param1) {
        return unsafe.getReference(param0, param1);
    }

    public void putReference(java.lang.Object param0, long param1, java.lang.Object param2) {
        unsafe.putReference(param0, param1, param2);
    }

    public boolean getBoolean(java.lang.Object param0, long param1) {
        return unsafe.getBoolean(param0, param1);
    }

    public void putBoolean(java.lang.Object param0, long param1, boolean param2) {
        unsafe.putBoolean(param0, param1, param2);
    }

    public byte getByte(long param0) {
        return unsafe.getByte(param0);
    }

    public byte getByte(java.lang.Object param0, long param1) {
        return unsafe.getByte(param0, param1);
    }

    public void putByte(java.lang.Object param0, long param1, byte param2) {
        unsafe.putByte(param0, param1, param2);
    }

    public void putByte(long param0, byte param1) {
        unsafe.putByte(param0, param1);
    }

    public short getShort(java.lang.Object param0, long param1) {
        return unsafe.getShort(param0, param1);
    }

    public short getShort(long param0) {
        return unsafe.getShort(param0);
    }

    public void putShort(java.lang.Object param0, long param1, short param2) {
        unsafe.putShort(param0, param1, param2);
    }

    public void putShort(long param0, short param1) {
        unsafe.putShort(param0, param1);
    }

    public char getChar(java.lang.Object param0, long param1) {
        return unsafe.getChar(param0, param1);
    }

    public char getChar(long param0) {
        return unsafe.getChar(param0);
    }

    public void putChar(long param0, char param1) {
        unsafe.putChar(param0, param1);
    }

    public void putChar(java.lang.Object param0, long param1, char param2) {
        unsafe.putChar(param0, param1, param2);
    }

    public int getInt(long param0) {
        return unsafe.getInt(param0);
    }

    public int getInt(java.lang.Object param0, long param1) {
        return unsafe.getInt(param0, param1);
    }

    public void putInt(long param0, int param1) {
        unsafe.putInt(param0, param1);
    }

    public void putInt(java.lang.Object param0, long param1, int param2) {
        unsafe.putInt(param0, param1, param2);
    }

    public long getLong(java.lang.Object param0, long param1) {
        return unsafe.getLong(param0, param1);
    }

    public long getLong(long param0) {
        return unsafe.getLong(param0);
    }

    public void putLong(java.lang.Object param0, long param1, long param2) {
        unsafe.putLong(param0, param1, param2);
    }

    public void putLong(long param0, long param1) {
        unsafe.putLong(param0, param1);
    }

    public float getFloat(java.lang.Object param0, long param1) {
        return unsafe.getFloat(param0, param1);
    }

    public float getFloat(long param0) {
        return unsafe.getFloat(param0);
    }

    public void putFloat(long param0, float param1) {
        unsafe.putFloat(param0, param1);
    }

    public void putFloat(java.lang.Object param0, long param1, float param2) {
        unsafe.putFloat(param0, param1, param2);
    }

    public double getDouble(java.lang.Object param0, long param1) {
        return unsafe.getDouble(param0, param1);
    }

    public double getDouble(long param0) {
        return unsafe.getDouble(param0);
    }

    public void putDouble(java.lang.Object param0, long param1, double param2) {
        unsafe.putDouble(param0, param1, param2);
    }

    public void putDouble(long param0, double param1) {
        unsafe.putDouble(param0, param1);
    }

    public java.lang.Object getReferenceVolatile(java.lang.Object param0, long param1) {
        return unsafe.getReferenceVolatile(param0, param1);
    }

    public void putReferenceVolatile(java.lang.Object param0, long param1, java.lang.Object param2) {
        unsafe.putReferenceVolatile(param0, param1, param2);
    }

    public boolean getBooleanVolatile(java.lang.Object param0, long param1) {
        return unsafe.getBooleanVolatile(param0, param1);
    }

    public void putBooleanVolatile(java.lang.Object param0, long param1, boolean param2) {
        unsafe.putBooleanVolatile(param0, param1, param2);
    }

    public byte getByteVolatile(java.lang.Object param0, long param1) {
        return unsafe.getByteVolatile(param0, param1);
    }

    public void putByteVolatile(java.lang.Object param0, long param1, byte param2) {
        unsafe.putByteVolatile(param0, param1, param2);
    }

    public short getShortVolatile(java.lang.Object param0, long param1) {
        return unsafe.getShortVolatile(param0, param1);
    }

    public void putShortVolatile(java.lang.Object param0, long param1, short param2) {
        unsafe.putShortVolatile(param0, param1, param2);
    }

    public char getCharVolatile(java.lang.Object param0, long param1) {
        return unsafe.getCharVolatile(param0, param1);
    }

    public void putCharVolatile(java.lang.Object param0, long param1, char param2) {
        unsafe.putCharVolatile(param0, param1, param2);
    }

    public int getIntVolatile(java.lang.Object param0, long param1) {
        return unsafe.getIntVolatile(param0, param1);
    }

    public void putIntVolatile(java.lang.Object param0, long param1, int param2) {
        unsafe.putIntVolatile(param0, param1, param2);
    }

    public long getLongVolatile(java.lang.Object param0, long param1) {
        return unsafe.getLongVolatile(param0, param1);
    }

    public void putLongVolatile(java.lang.Object param0, long param1, long param2) {
        unsafe.putLongVolatile(param0, param1, param2);
    }

    public float getFloatVolatile(java.lang.Object param0, long param1) {
        return unsafe.getFloatVolatile(param0, param1);
    }

    public void putFloatVolatile(java.lang.Object param0, long param1, float param2) {
        unsafe.putFloatVolatile(param0, param1, param2);
    }

    public double getDoubleVolatile(java.lang.Object param0, long param1) {
        return unsafe.getDoubleVolatile(param0, param1);
    }

    public void putDoubleVolatile(java.lang.Object param0, long param1, double param2) {
        unsafe.putDoubleVolatile(param0, param1, param2);
    }

    public java.lang.Object getReferenceOpaque(java.lang.Object param0, long param1) {
        return unsafe.getReferenceOpaque(param0, param1);
    }

    public void putReferenceOpaque(java.lang.Object param0, long param1, java.lang.Object param2) {
        unsafe.putReferenceOpaque(param0, param1, param2);
    }

    public boolean getBooleanOpaque(java.lang.Object param0, long param1) {
        return unsafe.getBooleanOpaque(param0, param1);
    }

    public void putBooleanOpaque(java.lang.Object param0, long param1, boolean param2) {
        unsafe.putBooleanOpaque(param0, param1, param2);
    }

    public byte getByteOpaque(java.lang.Object param0, long param1) {
        return unsafe.getByteOpaque(param0, param1);
    }

    public void putByteOpaque(java.lang.Object param0, long param1, byte param2) {
        unsafe.putByteOpaque(param0, param1, param2);
    }

    public short getShortOpaque(java.lang.Object param0, long param1) {
        return unsafe.getShortOpaque(param0, param1);
    }

    public void putShortOpaque(java.lang.Object param0, long param1, short param2) {
        unsafe.putShortOpaque(param0, param1, param2);
    }

    public char getCharOpaque(java.lang.Object param0, long param1) {
        return unsafe.getCharOpaque(param0, param1);
    }

    public void putCharOpaque(java.lang.Object param0, long param1, char param2) {
        unsafe.putCharOpaque(param0, param1, param2);
    }

    public int getIntOpaque(java.lang.Object param0, long param1) {
        return unsafe.getIntOpaque(param0, param1);
    }

    public void putIntOpaque(java.lang.Object param0, long param1, int param2) {
        unsafe.putIntOpaque(param0, param1, param2);
    }

    public long getLongOpaque(java.lang.Object param0, long param1) {
        return unsafe.getLongOpaque(param0, param1);
    }

    public void putLongOpaque(java.lang.Object param0, long param1, long param2) {
        unsafe.putLongOpaque(param0, param1, param2);
    }

    public float getFloatOpaque(java.lang.Object param0, long param1) {
        return unsafe.getFloatOpaque(param0, param1);
    }

    public void putFloatOpaque(java.lang.Object param0, long param1, float param2) {
        unsafe.putFloatOpaque(param0, param1, param2);
    }

    public double getDoubleOpaque(java.lang.Object param0, long param1) {
        return unsafe.getDoubleOpaque(param0, param1);
    }

    public void putDoubleOpaque(java.lang.Object param0, long param1, double param2) {
        unsafe.putDoubleOpaque(param0, param1, param2);
    }

    public java.lang.Object getReferenceAcquire(java.lang.Object param0, long param1) {
        return unsafe.getReferenceAcquire(param0, param1);
    }

    public void putReferenceRelease(java.lang.Object param0, long param1, java.lang.Object param2) {
        unsafe.putReferenceRelease(param0, param1, param2);
    }

    public boolean getBooleanAcquire(java.lang.Object param0, long param1) {
        return unsafe.getBooleanAcquire(param0, param1);
    }

    public void putBooleanRelease(java.lang.Object param0, long param1, boolean param2) {
        unsafe.putBooleanRelease(param0, param1, param2);
    }

    public byte getByteAcquire(java.lang.Object param0, long param1) {
        return unsafe.getByteAcquire(param0, param1);
    }

    public void putByteRelease(java.lang.Object param0, long param1, byte param2) {
        unsafe.putByteRelease(param0, param1, param2);
    }

    public short getShortAcquire(java.lang.Object param0, long param1) {
        return unsafe.getShortAcquire(param0, param1);
    }

    public void putShortRelease(java.lang.Object param0, long param1, short param2) {
        unsafe.putShortRelease(param0, param1, param2);
    }

    public char getCharAcquire(java.lang.Object param0, long param1) {
        return unsafe.getCharAcquire(param0, param1);
    }

    public void putCharRelease(java.lang.Object param0, long param1, char param2) {
        unsafe.putCharRelease(param0, param1, param2);
    }

    public int getIntAcquire(java.lang.Object param0, long param1) {
        return unsafe.getIntAcquire(param0, param1);
    }

    public void putIntRelease(java.lang.Object param0, long param1, int param2) {
        unsafe.putIntRelease(param0, param1, param2);
    }

    public long getLongAcquire(java.lang.Object param0, long param1) {
        return unsafe.getLongAcquire(param0, param1);
    }

    public void putLongRelease(java.lang.Object param0, long param1, long param2) {
        unsafe.putLongRelease(param0, param1, param2);
    }

    public float getFloatAcquire(java.lang.Object param0, long param1) {
        return unsafe.getFloatAcquire(param0, param1);
    }

    public void putFloatRelease(java.lang.Object param0, long param1, float param2) {
        unsafe.putFloatRelease(param0, param1, param2);
    }

    public double getDoubleAcquire(java.lang.Object param0, long param1) {
        return unsafe.getDoubleAcquire(param0, param1);
    }

    public void putDoubleRelease(java.lang.Object param0, long param1, double param2) {
        unsafe.putDoubleRelease(param0, param1, param2);
    }

    public short getShortUnaligned(java.lang.Object param0, long param1, boolean param2) {
        return unsafe.getShortUnaligned(param0, param1, param2);
    }

    public short getShortUnaligned(java.lang.Object param0, long param1) {
        return unsafe.getShortUnaligned(param0, param1);
    }

    public void putShortUnaligned(java.lang.Object param0, long param1, short param2) {
        unsafe.putShortUnaligned(param0, param1, param2);
    }

    public void putShortUnaligned(java.lang.Object param0, long param1, short param2, boolean param3) {
        unsafe.putShortUnaligned(param0, param1, param2, param3);
    }

    public char getCharUnaligned(java.lang.Object param0, long param1) {
        return unsafe.getCharUnaligned(param0, param1);
    }

    public char getCharUnaligned(java.lang.Object param0, long param1, boolean param2) {
        return unsafe.getCharUnaligned(param0, param1, param2);
    }

    public void putCharUnaligned(java.lang.Object param0, long param1, char param2, boolean param3) {
        unsafe.putCharUnaligned(param0, param1, param2, param3);
    }

    public void putCharUnaligned(java.lang.Object param0, long param1, char param2) {
        unsafe.putCharUnaligned(param0, param1, param2);
    }

    public int getIntUnaligned(java.lang.Object param0, long param1, boolean param2) {
        return unsafe.getIntUnaligned(param0, param1, param2);
    }

    public int getIntUnaligned(java.lang.Object param0, long param1) {
        return unsafe.getIntUnaligned(param0, param1);
    }

    public void putIntUnaligned(java.lang.Object param0, long param1, int param2) {
        unsafe.putIntUnaligned(param0, param1, param2);
    }

    public void putIntUnaligned(java.lang.Object param0, long param1, int param2, boolean param3) {
        unsafe.putIntUnaligned(param0, param1, param2, param3);
    }

    public long getLongUnaligned(java.lang.Object param0, long param1) {
        return unsafe.getLongUnaligned(param0, param1);
    }

    public long getLongUnaligned(java.lang.Object param0, long param1, boolean param2) {
        return unsafe.getLongUnaligned(param0, param1, param2);
    }

    public void putLongUnaligned(java.lang.Object param0, long param1, long param2, boolean param3) {
        unsafe.putLongUnaligned(param0, param1, param2, param3);
    }

    public void putLongUnaligned(java.lang.Object param0, long param1, long param2) {
        unsafe.putLongUnaligned(param0, param1, param2);
    }

    public boolean compareAndSetReference(java.lang.Object param0, long param1, java.lang.Object param2, java.lang.Object param3) {
        return unsafe.compareAndSetReference(param0, param1, param2, param3);
    }

    public java.lang.Object compareAndExchangeReference(java.lang.Object param0, long param1, java.lang.Object param2, java.lang.Object param3) {
        return unsafe.compareAndExchangeReference(param0, param1, param2, param3);
    }

    public java.lang.Object compareAndExchangeReferenceAcquire(java.lang.Object param0, long param1, java.lang.Object param2, java.lang.Object param3) {
        return unsafe.compareAndExchangeReferenceAcquire(param0, param1, param2, param3);
    }

    public java.lang.Object compareAndExchangeReferenceRelease(java.lang.Object param0, long param1, java.lang.Object param2, java.lang.Object param3) {
        return unsafe.compareAndExchangeReferenceRelease(param0, param1, param2, param3);
    }

    public boolean compareAndSetLong(java.lang.Object param0, long param1, long param2, long param3) {
        return unsafe.compareAndSetLong(param0, param1, param2, param3);
    }

    public long compareAndExchangeLong(java.lang.Object param0, long param1, long param2, long param3) {
        return unsafe.compareAndExchangeLong(param0, param1, param2, param3);
    }

    public long compareAndExchangeLongAcquire(java.lang.Object param0, long param1, long param2, long param3) {
        return unsafe.compareAndExchangeLongAcquire(param0, param1, param2, param3);
    }

    public long compareAndExchangeLongRelease(java.lang.Object param0, long param1, long param2, long param3) {
        return unsafe.compareAndExchangeLongRelease(param0, param1, param2, param3);
    }

    public boolean compareAndSetInt(java.lang.Object param0, long param1, int param2, int param3) {
        return unsafe.compareAndSetInt(param0, param1, param2, param3);
    }

    public int compareAndExchangeInt(java.lang.Object param0, long param1, int param2, int param3) {
        return unsafe.compareAndExchangeInt(param0, param1, param2, param3);
    }

    public int compareAndExchangeIntAcquire(java.lang.Object param0, long param1, int param2, int param3) {
        return unsafe.compareAndExchangeIntAcquire(param0, param1, param2, param3);
    }

    public int compareAndExchangeIntRelease(java.lang.Object param0, long param1, int param2, int param3) {
        return unsafe.compareAndExchangeIntRelease(param0, param1, param2, param3);
    }

    public boolean compareAndSetByte(java.lang.Object param0, long param1, byte param2, byte param3) {
        return unsafe.compareAndSetByte(param0, param1, param2, param3);
    }

    public byte compareAndExchangeByte(java.lang.Object param0, long param1, byte param2, byte param3) {
        return unsafe.compareAndExchangeByte(param0, param1, param2, param3);
    }

    public byte compareAndExchangeByteAcquire(java.lang.Object param0, long param1, byte param2, byte param3) {
        return unsafe.compareAndExchangeByteAcquire(param0, param1, param2, param3);
    }

    public byte compareAndExchangeByteRelease(java.lang.Object param0, long param1, byte param2, byte param3) {
        return unsafe.compareAndExchangeByteRelease(param0, param1, param2, param3);
    }

    public boolean compareAndSetShort(java.lang.Object param0, long param1, short param2, short param3) {
        return unsafe.compareAndSetShort(param0, param1, param2, param3);
    }

    public short compareAndExchangeShort(java.lang.Object param0, long param1, short param2, short param3) {
        return unsafe.compareAndExchangeShort(param0, param1, param2, param3);
    }

    public short compareAndExchangeShortAcquire(java.lang.Object param0, long param1, short param2, short param3) {
        return unsafe.compareAndExchangeShortAcquire(param0, param1, param2, param3);
    }

    public short compareAndExchangeShortRelease(java.lang.Object param0, long param1, short param2, short param3) {
        return unsafe.compareAndExchangeShortRelease(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetReferencePlain(java.lang.Object param0, long param1, java.lang.Object param2, java.lang.Object param3) {
        return unsafe.weakCompareAndSetReferencePlain(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetReferenceAcquire(java.lang.Object param0, long param1, java.lang.Object param2, java.lang.Object param3) {
        return unsafe.weakCompareAndSetReferenceAcquire(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetReferenceRelease(java.lang.Object param0, long param1, java.lang.Object param2, java.lang.Object param3) {
        return unsafe.weakCompareAndSetReferenceRelease(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetReference(java.lang.Object param0, long param1, java.lang.Object param2, java.lang.Object param3) {
        return unsafe.weakCompareAndSetReference(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetLongPlain(java.lang.Object param0, long param1, long param2, long param3) {
        return unsafe.weakCompareAndSetLongPlain(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetLongAcquire(java.lang.Object param0, long param1, long param2, long param3) {
        return unsafe.weakCompareAndSetLongAcquire(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetLongRelease(java.lang.Object param0, long param1, long param2, long param3) {
        return unsafe.weakCompareAndSetLongRelease(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetLong(java.lang.Object param0, long param1, long param2, long param3) {
        return unsafe.weakCompareAndSetLong(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetIntPlain(java.lang.Object param0, long param1, int param2, int param3) {
        return unsafe.weakCompareAndSetIntPlain(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetIntAcquire(java.lang.Object param0, long param1, int param2, int param3) {
        return unsafe.weakCompareAndSetIntAcquire(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetIntRelease(java.lang.Object param0, long param1, int param2, int param3) {
        return unsafe.weakCompareAndSetIntRelease(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetInt(java.lang.Object param0, long param1, int param2, int param3) {
        return unsafe.weakCompareAndSetInt(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetBytePlain(java.lang.Object param0, long param1, byte param2, byte param3) {
        return unsafe.weakCompareAndSetBytePlain(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetByteAcquire(java.lang.Object param0, long param1, byte param2, byte param3) {
        return unsafe.weakCompareAndSetByteAcquire(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetByteRelease(java.lang.Object param0, long param1, byte param2, byte param3) {
        return unsafe.weakCompareAndSetByteRelease(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetByte(java.lang.Object param0, long param1, byte param2, byte param3) {
        return unsafe.weakCompareAndSetByte(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetShortPlain(java.lang.Object param0, long param1, short param2, short param3) {
        return unsafe.weakCompareAndSetShortPlain(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetShortAcquire(java.lang.Object param0, long param1, short param2, short param3) {
        return unsafe.weakCompareAndSetShortAcquire(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetShortRelease(java.lang.Object param0, long param1, short param2, short param3) {
        return unsafe.weakCompareAndSetShortRelease(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetShort(java.lang.Object param0, long param1, short param2, short param3) {
        return unsafe.weakCompareAndSetShort(param0, param1, param2, param3);
    }

    public int getAndAddInt(java.lang.Object param0, long param1, int param2) {
        return unsafe.getAndAddInt(param0, param1, param2);
    }

    public long getAndAddLong(java.lang.Object param0, long param1, long param2) {
        return unsafe.getAndAddLong(param0, param1, param2);
    }

    public byte getAndAddByte(java.lang.Object param0, long param1, byte param2) {
        return unsafe.getAndAddByte(param0, param1, param2);
    }

    public short getAndAddShort(java.lang.Object param0, long param1, short param2) {
        return unsafe.getAndAddShort(param0, param1, param2);
    }

    public int getAndSetInt(java.lang.Object param0, long param1, int param2) {
        return unsafe.getAndSetInt(param0, param1, param2);
    }

    public long getAndSetLong(java.lang.Object param0, long param1, long param2) {
        return unsafe.getAndSetLong(param0, param1, param2);
    }

    public byte getAndSetByte(java.lang.Object param0, long param1, byte param2) {
        return unsafe.getAndSetByte(param0, param1, param2);
    }

    public short getAndSetShort(java.lang.Object param0, long param1, short param2) {
        return unsafe.getAndSetShort(param0, param1, param2);
    }

    public java.lang.Object getAndSetReference(java.lang.Object param0, long param1, java.lang.Object param2) {
        return unsafe.getAndSetReference(param0, param1, param2);
    }

    public void park(boolean param0, long param1) {
        unsafe.park(param0, param1);
    }

    public void unpark(java.lang.Object param0) {
        unsafe.unpark(param0);
    }

    public void throwException(java.lang.Throwable param0) {
        unsafe.throwException(param0);
    }

    public java.lang.Class defineClass(java.lang.String param0, byte[] param1, int param2, int param3, java.lang.ClassLoader param4, java.security.ProtectionDomain param5) {
        return unsafe.defineClass(param0, param1, param2, param3, param4, param5);
    }

    public long objectFieldOffset(java.lang.Class param0, java.lang.String param1) {
        return unsafe.objectFieldOffset(param0, param1);
    }

    public long objectFieldOffset(java.lang.reflect.Field param0) {
        return unsafe.objectFieldOffset(param0);
    }

    public java.lang.Object staticFieldBase(java.lang.reflect.Field param0) {
        return unsafe.staticFieldBase(param0);
    }

    public long staticFieldOffset(java.lang.reflect.Field param0) {
        return unsafe.staticFieldOffset(param0);
    }

    public boolean shouldBeInitialized(java.lang.Class param0) {
        return unsafe.shouldBeInitialized(param0);
    }

    public void ensureClassInitialized(java.lang.Class param0) {
        unsafe.ensureClassInitialized(param0);
    }

    public void loadLoadFence() {
        unsafe.loadLoadFence();
    }

    public void storeStoreFence() {
        unsafe.storeStoreFence();
    }

    public boolean unalignedAccess() {
        return unsafe.unalignedAccess();
    }

    public long getAddress(long param0) {
        return unsafe.getAddress(param0);
    }

    public long getAddress(java.lang.Object param0, long param1) {
        return unsafe.getAddress(param0, param1);
    }

    public void putAddress(long param0, long param1) {
        unsafe.putAddress(param0, param1);
    }

    public void putAddress(java.lang.Object param0, long param1, long param2) {
        unsafe.putAddress(param0, param1, param2);
    }

    public java.lang.Object getUncompressedObject(long param0) {
        return unsafe.getUncompressedObject(param0);
    }

    public long allocateMemory(long param0) {
        return unsafe.allocateMemory(param0);
    }

    public long reallocateMemory(long param0, long param1) {
        return unsafe.reallocateMemory(param0, param1);
    }

    public void setMemory(java.lang.Object param0, long param1, long param2, byte param3) {
        unsafe.setMemory(param0, param1, param2, param3);
    }

    public void setMemory(long param0, long param1, byte param2) {
        unsafe.setMemory(param0, param1, param2);
    }

    public void copyMemory(long param0, long param1, long param2) {
        unsafe.copyMemory(param0, param1, param2);
    }

    public void copyMemory(java.lang.Object param0, long param1, java.lang.Object param2, long param3, long param4) {
        unsafe.copyMemory(param0, param1, param2, param3, param4);
    }

    public void copySwapMemory(java.lang.Object param0, long param1, java.lang.Object param2, long param3, long param4, long param5) {
        unsafe.copySwapMemory(param0, param1, param2, param3, param4, param5);
    }

    public void copySwapMemory(long param0, long param1, long param2, long param3) {
        unsafe.copySwapMemory(param0, param1, param2, param3);
    }

    public void freeMemory(long param0) {
        unsafe.freeMemory(param0);
    }

    public int arrayBaseOffset(java.lang.Class param0) {
        return unsafe.arrayBaseOffset(param0);
    }

    public int arrayIndexScale(java.lang.Class param0) {
        return unsafe.arrayIndexScale(param0);
    }

    public int addressSize() {
        return unsafe.addressSize();
    }

    public int pageSize() {
        return unsafe.pageSize();
    }

    public java.lang.Class defineClass0(java.lang.String param0, byte[] param1, int param2, int param3, java.lang.ClassLoader param4, java.security.ProtectionDomain param5) {
        return unsafe.defineClass0(param0, param1, param2, param3, param4, param5);
    }

    public java.lang.Class defineAnonymousClass(java.lang.Class param0, byte[] param1, java.lang.Object[] param2) {
        return unsafe.defineAnonymousClass(param0, param1, param2);
    }

    public java.lang.Object allocateUninitializedArray(java.lang.Class param0, int param1) {
        return unsafe.allocateUninitializedArray(param0, param1);
    }

    public boolean compareAndSetChar(java.lang.Object param0, long param1, char param2, char param3) {
        return unsafe.compareAndSetChar(param0, param1, param2, param3);
    }

    public char compareAndExchangeChar(java.lang.Object param0, long param1, char param2, char param3) {
        return unsafe.compareAndExchangeChar(param0, param1, param2, param3);
    }

    public char compareAndExchangeCharAcquire(java.lang.Object param0, long param1, char param2, char param3) {
        return unsafe.compareAndExchangeCharAcquire(param0, param1, param2, param3);
    }

    public char compareAndExchangeCharRelease(java.lang.Object param0, long param1, char param2, char param3) {
        return unsafe.compareAndExchangeCharRelease(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetChar(java.lang.Object param0, long param1, char param2, char param3) {
        return unsafe.weakCompareAndSetChar(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetCharAcquire(java.lang.Object param0, long param1, char param2, char param3) {
        return unsafe.weakCompareAndSetCharAcquire(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetCharRelease(java.lang.Object param0, long param1, char param2, char param3) {
        return unsafe.weakCompareAndSetCharRelease(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetCharPlain(java.lang.Object param0, long param1, char param2, char param3) {
        return unsafe.weakCompareAndSetCharPlain(param0, param1, param2, param3);
    }

    public boolean compareAndSetBoolean(java.lang.Object param0, long param1, boolean param2, boolean param3) {
        return unsafe.compareAndSetBoolean(param0, param1, param2, param3);
    }

    public boolean compareAndExchangeBoolean(java.lang.Object param0, long param1, boolean param2, boolean param3) {
        return unsafe.compareAndExchangeBoolean(param0, param1, param2, param3);
    }

    public boolean compareAndExchangeBooleanAcquire(java.lang.Object param0, long param1, boolean param2, boolean param3) {
        return unsafe.compareAndExchangeBooleanAcquire(param0, param1, param2, param3);
    }

    public boolean compareAndExchangeBooleanRelease(java.lang.Object param0, long param1, boolean param2, boolean param3) {
        return unsafe.compareAndExchangeBooleanRelease(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetBoolean(java.lang.Object param0, long param1, boolean param2, boolean param3) {
        return unsafe.weakCompareAndSetBoolean(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetBooleanAcquire(java.lang.Object param0, long param1, boolean param2, boolean param3) {
        return unsafe.weakCompareAndSetBooleanAcquire(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetBooleanRelease(java.lang.Object param0, long param1, boolean param2, boolean param3) {
        return unsafe.weakCompareAndSetBooleanRelease(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetBooleanPlain(java.lang.Object param0, long param1, boolean param2, boolean param3) {
        return unsafe.weakCompareAndSetBooleanPlain(param0, param1, param2, param3);
    }

    public boolean compareAndSetFloat(java.lang.Object param0, long param1, float param2, float param3) {
        return unsafe.compareAndSetFloat(param0, param1, param2, param3);
    }

    public float compareAndExchangeFloat(java.lang.Object param0, long param1, float param2, float param3) {
        return unsafe.compareAndExchangeFloat(param0, param1, param2, param3);
    }

    public float compareAndExchangeFloatAcquire(java.lang.Object param0, long param1, float param2, float param3) {
        return unsafe.compareAndExchangeFloatAcquire(param0, param1, param2, param3);
    }

    public float compareAndExchangeFloatRelease(java.lang.Object param0, long param1, float param2, float param3) {
        return unsafe.compareAndExchangeFloatRelease(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetFloatPlain(java.lang.Object param0, long param1, float param2, float param3) {
        return unsafe.weakCompareAndSetFloatPlain(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetFloatAcquire(java.lang.Object param0, long param1, float param2, float param3) {
        return unsafe.weakCompareAndSetFloatAcquire(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetFloatRelease(java.lang.Object param0, long param1, float param2, float param3) {
        return unsafe.weakCompareAndSetFloatRelease(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetFloat(java.lang.Object param0, long param1, float param2, float param3) {
        return unsafe.weakCompareAndSetFloat(param0, param1, param2, param3);
    }

    public boolean compareAndSetDouble(java.lang.Object param0, long param1, double param2, double param3) {
        return unsafe.compareAndSetDouble(param0, param1, param2, param3);
    }

    public double compareAndExchangeDouble(java.lang.Object param0, long param1, double param2, double param3) {
        return unsafe.compareAndExchangeDouble(param0, param1, param2, param3);
    }

    public double compareAndExchangeDoubleAcquire(java.lang.Object param0, long param1, double param2, double param3) {
        return unsafe.compareAndExchangeDoubleAcquire(param0, param1, param2, param3);
    }

    public double compareAndExchangeDoubleRelease(java.lang.Object param0, long param1, double param2, double param3) {
        return unsafe.compareAndExchangeDoubleRelease(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetDoublePlain(java.lang.Object param0, long param1, double param2, double param3) {
        return unsafe.weakCompareAndSetDoublePlain(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetDoubleAcquire(java.lang.Object param0, long param1, double param2, double param3) {
        return unsafe.weakCompareAndSetDoubleAcquire(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetDoubleRelease(java.lang.Object param0, long param1, double param2, double param3) {
        return unsafe.weakCompareAndSetDoubleRelease(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetDouble(java.lang.Object param0, long param1, double param2, double param3) {
        return unsafe.weakCompareAndSetDouble(param0, param1, param2, param3);
    }

    public int getLoadAverage(double[] param0, int param1) {
        return unsafe.getLoadAverage(param0, param1);
    }

    public int getAndAddIntRelease(java.lang.Object param0, long param1, int param2) {
        return unsafe.getAndAddIntRelease(param0, param1, param2);
    }

    public int getAndAddIntAcquire(java.lang.Object param0, long param1, int param2) {
        return unsafe.getAndAddIntAcquire(param0, param1, param2);
    }

    public long getAndAddLongRelease(java.lang.Object param0, long param1, long param2) {
        return unsafe.getAndAddLongRelease(param0, param1, param2);
    }

    public long getAndAddLongAcquire(java.lang.Object param0, long param1, long param2) {
        return unsafe.getAndAddLongAcquire(param0, param1, param2);
    }

    public byte getAndAddByteRelease(java.lang.Object param0, long param1, byte param2) {
        return unsafe.getAndAddByteRelease(param0, param1, param2);
    }

    public byte getAndAddByteAcquire(java.lang.Object param0, long param1, byte param2) {
        return unsafe.getAndAddByteAcquire(param0, param1, param2);
    }

    public short getAndAddShortRelease(java.lang.Object param0, long param1, short param2) {
        return unsafe.getAndAddShortRelease(param0, param1, param2);
    }

    public short getAndAddShortAcquire(java.lang.Object param0, long param1, short param2) {
        return unsafe.getAndAddShortAcquire(param0, param1, param2);
    }

    public char getAndAddChar(java.lang.Object param0, long param1, char param2) {
        return unsafe.getAndAddChar(param0, param1, param2);
    }

    public char getAndAddCharRelease(java.lang.Object param0, long param1, char param2) {
        return unsafe.getAndAddCharRelease(param0, param1, param2);
    }

    public char getAndAddCharAcquire(java.lang.Object param0, long param1, char param2) {
        return unsafe.getAndAddCharAcquire(param0, param1, param2);
    }

    public float getAndAddFloat(java.lang.Object param0, long param1, float param2) {
        return unsafe.getAndAddFloat(param0, param1, param2);
    }

    public float getAndAddFloatRelease(java.lang.Object param0, long param1, float param2) {
        return unsafe.getAndAddFloatRelease(param0, param1, param2);
    }

    public float getAndAddFloatAcquire(java.lang.Object param0, long param1, float param2) {
        return unsafe.getAndAddFloatAcquire(param0, param1, param2);
    }

    public double getAndAddDouble(java.lang.Object param0, long param1, double param2) {
        return unsafe.getAndAddDouble(param0, param1, param2);
    }

    public double getAndAddDoubleRelease(java.lang.Object param0, long param1, double param2) {
        return unsafe.getAndAddDoubleRelease(param0, param1, param2);
    }

    public double getAndAddDoubleAcquire(java.lang.Object param0, long param1, double param2) {
        return unsafe.getAndAddDoubleAcquire(param0, param1, param2);
    }

    public int getAndSetIntRelease(java.lang.Object param0, long param1, int param2) {
        return unsafe.getAndSetIntRelease(param0, param1, param2);
    }

    public int getAndSetIntAcquire(java.lang.Object param0, long param1, int param2) {
        return unsafe.getAndSetIntAcquire(param0, param1, param2);
    }

    public long getAndSetLongRelease(java.lang.Object param0, long param1, long param2) {
        return unsafe.getAndSetLongRelease(param0, param1, param2);
    }

    public long getAndSetLongAcquire(java.lang.Object param0, long param1, long param2) {
        return unsafe.getAndSetLongAcquire(param0, param1, param2);
    }

    public java.lang.Object getAndSetReferenceRelease(java.lang.Object param0, long param1, java.lang.Object param2) {
        return unsafe.getAndSetReferenceRelease(param0, param1, param2);
    }

    public java.lang.Object getAndSetReferenceAcquire(java.lang.Object param0, long param1, java.lang.Object param2) {
        return unsafe.getAndSetReferenceAcquire(param0, param1, param2);
    }

    public byte getAndSetByteRelease(java.lang.Object param0, long param1, byte param2) {
        return unsafe.getAndSetByteRelease(param0, param1, param2);
    }

    public byte getAndSetByteAcquire(java.lang.Object param0, long param1, byte param2) {
        return unsafe.getAndSetByteAcquire(param0, param1, param2);
    }

    public boolean getAndSetBoolean(java.lang.Object param0, long param1, boolean param2) {
        return unsafe.getAndSetBoolean(param0, param1, param2);
    }

    public boolean getAndSetBooleanRelease(java.lang.Object param0, long param1, boolean param2) {
        return unsafe.getAndSetBooleanRelease(param0, param1, param2);
    }

    public boolean getAndSetBooleanAcquire(java.lang.Object param0, long param1, boolean param2) {
        return unsafe.getAndSetBooleanAcquire(param0, param1, param2);
    }

    public short getAndSetShortRelease(java.lang.Object param0, long param1, short param2) {
        return unsafe.getAndSetShortRelease(param0, param1, param2);
    }

    public short getAndSetShortAcquire(java.lang.Object param0, long param1, short param2) {
        return unsafe.getAndSetShortAcquire(param0, param1, param2);
    }

    public char getAndSetChar(java.lang.Object param0, long param1, char param2) {
        return unsafe.getAndSetChar(param0, param1, param2);
    }

    public char getAndSetCharRelease(java.lang.Object param0, long param1, char param2) {
        return unsafe.getAndSetCharRelease(param0, param1, param2);
    }

    public char getAndSetCharAcquire(java.lang.Object param0, long param1, char param2) {
        return unsafe.getAndSetCharAcquire(param0, param1, param2);
    }

    public float getAndSetFloat(java.lang.Object param0, long param1, float param2) {
        return unsafe.getAndSetFloat(param0, param1, param2);
    }

    public float getAndSetFloatRelease(java.lang.Object param0, long param1, float param2) {
        return unsafe.getAndSetFloatRelease(param0, param1, param2);
    }

    public float getAndSetFloatAcquire(java.lang.Object param0, long param1, float param2) {
        return unsafe.getAndSetFloatAcquire(param0, param1, param2);
    }

    public double getAndSetDouble(java.lang.Object param0, long param1, double param2) {
        return unsafe.getAndSetDouble(param0, param1, param2);
    }

    public double getAndSetDoubleRelease(java.lang.Object param0, long param1, double param2) {
        return unsafe.getAndSetDoubleRelease(param0, param1, param2);
    }

    public double getAndSetDoubleAcquire(java.lang.Object param0, long param1, double param2) {
        return unsafe.getAndSetDoubleAcquire(param0, param1, param2);
    }

    public boolean getAndBitwiseOrBoolean(java.lang.Object param0, long param1, boolean param2) {
        return unsafe.getAndBitwiseOrBoolean(param0, param1, param2);
    }

    public boolean getAndBitwiseOrBooleanRelease(java.lang.Object param0, long param1, boolean param2) {
        return unsafe.getAndBitwiseOrBooleanRelease(param0, param1, param2);
    }

    public boolean getAndBitwiseOrBooleanAcquire(java.lang.Object param0, long param1, boolean param2) {
        return unsafe.getAndBitwiseOrBooleanAcquire(param0, param1, param2);
    }

    public boolean getAndBitwiseAndBoolean(java.lang.Object param0, long param1, boolean param2) {
        return unsafe.getAndBitwiseAndBoolean(param0, param1, param2);
    }

    public boolean getAndBitwiseAndBooleanRelease(java.lang.Object param0, long param1, boolean param2) {
        return unsafe.getAndBitwiseAndBooleanRelease(param0, param1, param2);
    }

    public boolean getAndBitwiseAndBooleanAcquire(java.lang.Object param0, long param1, boolean param2) {
        return unsafe.getAndBitwiseAndBooleanAcquire(param0, param1, param2);
    }

    public boolean getAndBitwiseXorBoolean(java.lang.Object param0, long param1, boolean param2) {
        return unsafe.getAndBitwiseXorBoolean(param0, param1, param2);
    }

    public boolean getAndBitwiseXorBooleanRelease(java.lang.Object param0, long param1, boolean param2) {
        return unsafe.getAndBitwiseXorBooleanRelease(param0, param1, param2);
    }

    public boolean getAndBitwiseXorBooleanAcquire(java.lang.Object param0, long param1, boolean param2) {
        return unsafe.getAndBitwiseXorBooleanAcquire(param0, param1, param2);
    }

    public byte getAndBitwiseOrByte(java.lang.Object param0, long param1, byte param2) {
        return unsafe.getAndBitwiseOrByte(param0, param1, param2);
    }

    public byte getAndBitwiseOrByteRelease(java.lang.Object param0, long param1, byte param2) {
        return unsafe.getAndBitwiseOrByteRelease(param0, param1, param2);
    }

    public byte getAndBitwiseOrByteAcquire(java.lang.Object param0, long param1, byte param2) {
        return unsafe.getAndBitwiseOrByteAcquire(param0, param1, param2);
    }

    public byte getAndBitwiseAndByte(java.lang.Object param0, long param1, byte param2) {
        return unsafe.getAndBitwiseAndByte(param0, param1, param2);
    }

    public byte getAndBitwiseAndByteRelease(java.lang.Object param0, long param1, byte param2) {
        return unsafe.getAndBitwiseAndByteRelease(param0, param1, param2);
    }

    public byte getAndBitwiseAndByteAcquire(java.lang.Object param0, long param1, byte param2) {
        return unsafe.getAndBitwiseAndByteAcquire(param0, param1, param2);
    }

    public byte getAndBitwiseXorByte(java.lang.Object param0, long param1, byte param2) {
        return unsafe.getAndBitwiseXorByte(param0, param1, param2);
    }

    public byte getAndBitwiseXorByteRelease(java.lang.Object param0, long param1, byte param2) {
        return unsafe.getAndBitwiseXorByteRelease(param0, param1, param2);
    }

    public byte getAndBitwiseXorByteAcquire(java.lang.Object param0, long param1, byte param2) {
        return unsafe.getAndBitwiseXorByteAcquire(param0, param1, param2);
    }

    public char getAndBitwiseOrChar(java.lang.Object param0, long param1, char param2) {
        return unsafe.getAndBitwiseOrChar(param0, param1, param2);
    }

    public char getAndBitwiseOrCharRelease(java.lang.Object param0, long param1, char param2) {
        return unsafe.getAndBitwiseOrCharRelease(param0, param1, param2);
    }

    public char getAndBitwiseOrCharAcquire(java.lang.Object param0, long param1, char param2) {
        return unsafe.getAndBitwiseOrCharAcquire(param0, param1, param2);
    }

    public char getAndBitwiseAndChar(java.lang.Object param0, long param1, char param2) {
        return unsafe.getAndBitwiseAndChar(param0, param1, param2);
    }

    public char getAndBitwiseAndCharRelease(java.lang.Object param0, long param1, char param2) {
        return unsafe.getAndBitwiseAndCharRelease(param0, param1, param2);
    }

    public char getAndBitwiseAndCharAcquire(java.lang.Object param0, long param1, char param2) {
        return unsafe.getAndBitwiseAndCharAcquire(param0, param1, param2);
    }

    public char getAndBitwiseXorChar(java.lang.Object param0, long param1, char param2) {
        return unsafe.getAndBitwiseXorChar(param0, param1, param2);
    }

    public char getAndBitwiseXorCharRelease(java.lang.Object param0, long param1, char param2) {
        return unsafe.getAndBitwiseXorCharRelease(param0, param1, param2);
    }

    public char getAndBitwiseXorCharAcquire(java.lang.Object param0, long param1, char param2) {
        return unsafe.getAndBitwiseXorCharAcquire(param0, param1, param2);
    }

    public short getAndBitwiseOrShort(java.lang.Object param0, long param1, short param2) {
        return unsafe.getAndBitwiseOrShort(param0, param1, param2);
    }

    public short getAndBitwiseOrShortRelease(java.lang.Object param0, long param1, short param2) {
        return unsafe.getAndBitwiseOrShortRelease(param0, param1, param2);
    }

    public short getAndBitwiseOrShortAcquire(java.lang.Object param0, long param1, short param2) {
        return unsafe.getAndBitwiseOrShortAcquire(param0, param1, param2);
    }

    public short getAndBitwiseAndShort(java.lang.Object param0, long param1, short param2) {
        return unsafe.getAndBitwiseAndShort(param0, param1, param2);
    }

    public short getAndBitwiseAndShortRelease(java.lang.Object param0, long param1, short param2) {
        return unsafe.getAndBitwiseAndShortRelease(param0, param1, param2);
    }

    public short getAndBitwiseAndShortAcquire(java.lang.Object param0, long param1, short param2) {
        return unsafe.getAndBitwiseAndShortAcquire(param0, param1, param2);
    }

    public short getAndBitwiseXorShort(java.lang.Object param0, long param1, short param2) {
        return unsafe.getAndBitwiseXorShort(param0, param1, param2);
    }

    public short getAndBitwiseXorShortRelease(java.lang.Object param0, long param1, short param2) {
        return unsafe.getAndBitwiseXorShortRelease(param0, param1, param2);
    }

    public short getAndBitwiseXorShortAcquire(java.lang.Object param0, long param1, short param2) {
        return unsafe.getAndBitwiseXorShortAcquire(param0, param1, param2);
    }

    public int getAndBitwiseOrInt(java.lang.Object param0, long param1, int param2) {
        return unsafe.getAndBitwiseOrInt(param0, param1, param2);
    }

    public int getAndBitwiseOrIntRelease(java.lang.Object param0, long param1, int param2) {
        return unsafe.getAndBitwiseOrIntRelease(param0, param1, param2);
    }

    public int getAndBitwiseOrIntAcquire(java.lang.Object param0, long param1, int param2) {
        return unsafe.getAndBitwiseOrIntAcquire(param0, param1, param2);
    }

    public int getAndBitwiseAndInt(java.lang.Object param0, long param1, int param2) {
        return unsafe.getAndBitwiseAndInt(param0, param1, param2);
    }

    public int getAndBitwiseAndIntRelease(java.lang.Object param0, long param1, int param2) {
        return unsafe.getAndBitwiseAndIntRelease(param0, param1, param2);
    }

    public int getAndBitwiseAndIntAcquire(java.lang.Object param0, long param1, int param2) {
        return unsafe.getAndBitwiseAndIntAcquire(param0, param1, param2);
    }

    public int getAndBitwiseXorInt(java.lang.Object param0, long param1, int param2) {
        return unsafe.getAndBitwiseXorInt(param0, param1, param2);
    }

    public int getAndBitwiseXorIntRelease(java.lang.Object param0, long param1, int param2) {
        return unsafe.getAndBitwiseXorIntRelease(param0, param1, param2);
    }

    public int getAndBitwiseXorIntAcquire(java.lang.Object param0, long param1, int param2) {
        return unsafe.getAndBitwiseXorIntAcquire(param0, param1, param2);
    }

    public long getAndBitwiseOrLong(java.lang.Object param0, long param1, long param2) {
        return unsafe.getAndBitwiseOrLong(param0, param1, param2);
    }

    public long getAndBitwiseOrLongRelease(java.lang.Object param0, long param1, long param2) {
        return unsafe.getAndBitwiseOrLongRelease(param0, param1, param2);
    }

    public long getAndBitwiseOrLongAcquire(java.lang.Object param0, long param1, long param2) {
        return unsafe.getAndBitwiseOrLongAcquire(param0, param1, param2);
    }

    public long getAndBitwiseAndLong(java.lang.Object param0, long param1, long param2) {
        return unsafe.getAndBitwiseAndLong(param0, param1, param2);
    }

    public long getAndBitwiseAndLongRelease(java.lang.Object param0, long param1, long param2) {
        return unsafe.getAndBitwiseAndLongRelease(param0, param1, param2);
    }

    public long getAndBitwiseAndLongAcquire(java.lang.Object param0, long param1, long param2) {
        return unsafe.getAndBitwiseAndLongAcquire(param0, param1, param2);
    }

    public long getAndBitwiseXorLong(java.lang.Object param0, long param1, long param2) {
        return unsafe.getAndBitwiseXorLong(param0, param1, param2);
    }

    public long getAndBitwiseXorLongRelease(java.lang.Object param0, long param1, long param2) {
        return unsafe.getAndBitwiseXorLongRelease(param0, param1, param2);
    }

    public long getAndBitwiseXorLongAcquire(java.lang.Object param0, long param1, long param2) {
        return unsafe.getAndBitwiseXorLongAcquire(param0, param1, param2);
    }

    public boolean isBigEndian() {
        return unsafe.isBigEndian();
    }

    public void invokeCleaner(java.nio.ByteBuffer param0) {
        unsafe.invokeCleaner(param0);
    }
    /*
    public java.lang.Object getObject(java.lang.Object param0, long param1) {
        return unsafe.getObject(param0, param1);
    }

    public java.lang.Object getObjectVolatile(java.lang.Object param0, long param1) {
        return unsafe.getObjectVolatile(param0, param1);
    }

    public java.lang.Object getObjectAcquire(java.lang.Object param0, long param1) {
        return unsafe.getObjectAcquire(param0, param1);
    }

    public java.lang.Object getObjectOpaque(java.lang.Object param0, long param1) {
        return unsafe.getObjectOpaque(param0, param1);
    }

    public void putObject(java.lang.Object param0, long param1, java.lang.Object param2) {
        unsafe.putObject(param0, param1, param2);
    }

    public void putObjectVolatile(java.lang.Object param0, long param1, java.lang.Object param2) {
        unsafe.putObjectVolatile(param0, param1, param2);
    }

    public void putObjectOpaque(java.lang.Object param0, long param1, java.lang.Object param2) {
        unsafe.putObjectOpaque(param0, param1, param2);
    }

    public void putObjectRelease(java.lang.Object param0, long param1, java.lang.Object param2) {
        unsafe.putObjectRelease(param0, param1, param2);
    }

    public java.lang.Object getAndSetObject(java.lang.Object param0, long param1, java.lang.Object param2) {
        return unsafe.getAndSetObject(param0, param1, param2);
    }

    public java.lang.Object getAndSetObjectAcquire(java.lang.Object param0, long param1, java.lang.Object param2) {
        return unsafe.getAndSetObjectAcquire(param0, param1, param2);
    }

    public java.lang.Object getAndSetObjectRelease(java.lang.Object param0, long param1, java.lang.Object param2) {
        return unsafe.getAndSetObjectRelease(param0, param1, param2);
    }

    public boolean compareAndSetObject(java.lang.Object param0, long param1, java.lang.Object param2, java.lang.Object param3) {
        return unsafe.compareAndSetObject(param0, param1, param2, param3);
    }

    public java.lang.Object compareAndExchangeObject(java.lang.Object param0, long param1, java.lang.Object param2, java.lang.Object param3) {
        return unsafe.compareAndExchangeObject(param0, param1, param2, param3);
    }

    public java.lang.Object compareAndExchangeObjectAcquire(java.lang.Object param0, long param1, java.lang.Object param2, java.lang.Object param3) {
        return unsafe.compareAndExchangeObjectAcquire(param0, param1, param2, param3);
    }

    public java.lang.Object compareAndExchangeObjectRelease(java.lang.Object param0, long param1, java.lang.Object param2, java.lang.Object param3) {
        return unsafe.compareAndExchangeObjectRelease(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetObject(java.lang.Object param0, long param1, java.lang.Object param2, java.lang.Object param3) {
        return unsafe.weakCompareAndSetObject(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetObjectAcquire(java.lang.Object param0, long param1, java.lang.Object param2, java.lang.Object param3) {
        return unsafe.weakCompareAndSetObjectAcquire(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetObjectPlain(java.lang.Object param0, long param1, java.lang.Object param2, java.lang.Object param3) {
        return unsafe.weakCompareAndSetObjectPlain(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetObjectRelease(java.lang.Object param0, long param1, java.lang.Object param2, java.lang.Object param3) {
        return unsafe.weakCompareAndSetObjectRelease(param0, param1, param2, param3);
    }
    */
}

/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: JDKUnsafe.java@author: karlatemp@vip.qq.com: 19-11-22 下午7:46@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.internal;


import jdk.internal.misc.Unsafe;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"Since15", "unchecked", "rawtypes", "RedundantSuppression", "deprecation"})
class JDKUnsafe extends cn.mcres.karlatemp.mxlib.tools.Unsafe {
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    @NotNull
    public Object allocateInstance(@NotNull Class param0) throws InstantiationException {
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

    public Object getReference(Object param0, long param1) {
        return unsafe.getReference(param0, param1);
    }

    public void putReference(Object param0, long param1, Object param2) {
        unsafe.putReference(param0, param1, param2);
    }

    public boolean getBoolean(Object param0, long param1) {
        return unsafe.getBoolean(param0, param1);
    }

    public void putBoolean(Object param0, long param1, boolean param2) {
        unsafe.putBoolean(param0, param1, param2);
    }

    public byte getByte(long param0) {
        return unsafe.getByte(param0);
    }

    public byte getByte(Object param0, long param1) {
        return unsafe.getByte(param0, param1);
    }

    public void putByte(Object param0, long param1, byte param2) {
        unsafe.putByte(param0, param1, param2);
    }

    public void putByte(long param0, byte param1) {
        unsafe.putByte(param0, param1);
    }

    public short getShort(Object param0, long param1) {
        return unsafe.getShort(param0, param1);
    }

    public short getShort(long param0) {
        return unsafe.getShort(param0);
    }

    public void putShort(Object param0, long param1, short param2) {
        unsafe.putShort(param0, param1, param2);
    }

    public void putShort(long param0, short param1) {
        unsafe.putShort(param0, param1);
    }

    public char getChar(Object param0, long param1) {
        return unsafe.getChar(param0, param1);
    }

    public char getChar(long param0) {
        return unsafe.getChar(param0);
    }

    public void putChar(long param0, char param1) {
        unsafe.putChar(param0, param1);
    }

    public void putChar(Object param0, long param1, char param2) {
        unsafe.putChar(param0, param1, param2);
    }

    public int getInt(long param0) {
        return unsafe.getInt(param0);
    }

    public int getInt(Object param0, long param1) {
        return unsafe.getInt(param0, param1);
    }

    public void putInt(long param0, int param1) {
        unsafe.putInt(param0, param1);
    }

    public void putInt(Object param0, long param1, int param2) {
        unsafe.putInt(param0, param1, param2);
    }

    public long getLong(Object param0, long param1) {
        return unsafe.getLong(param0, param1);
    }

    public long getLong(long param0) {
        return unsafe.getLong(param0);
    }

    public void putLong(Object param0, long param1, long param2) {
        unsafe.putLong(param0, param1, param2);
    }

    public void putLong(long param0, long param1) {
        unsafe.putLong(param0, param1);
    }

    public float getFloat(Object param0, long param1) {
        return unsafe.getFloat(param0, param1);
    }

    public float getFloat(long param0) {
        return unsafe.getFloat(param0);
    }

    public void putFloat(long param0, float param1) {
        unsafe.putFloat(param0, param1);
    }

    public void putFloat(Object param0, long param1, float param2) {
        unsafe.putFloat(param0, param1, param2);
    }

    public double getDouble(Object param0, long param1) {
        return unsafe.getDouble(param0, param1);
    }

    public double getDouble(long param0) {
        return unsafe.getDouble(param0);
    }

    public void putDouble(Object param0, long param1, double param2) {
        unsafe.putDouble(param0, param1, param2);
    }

    public void putDouble(long param0, double param1) {
        unsafe.putDouble(param0, param1);
    }

    public Object getReferenceVolatile(Object param0, long param1) {
        return unsafe.getReferenceVolatile(param0, param1);
    }

    public void putReferenceVolatile(Object param0, long param1, Object param2) {
        unsafe.putReferenceVolatile(param0, param1, param2);
    }

    public boolean getBooleanVolatile(Object param0, long param1) {
        return unsafe.getBooleanVolatile(param0, param1);
    }

    public void putBooleanVolatile(Object param0, long param1, boolean param2) {
        unsafe.putBooleanVolatile(param0, param1, param2);
    }

    public byte getByteVolatile(Object param0, long param1) {
        return unsafe.getByteVolatile(param0, param1);
    }

    public void putByteVolatile(Object param0, long param1, byte param2) {
        unsafe.putByteVolatile(param0, param1, param2);
    }

    public short getShortVolatile(Object param0, long param1) {
        return unsafe.getShortVolatile(param0, param1);
    }

    public void putShortVolatile(Object param0, long param1, short param2) {
        unsafe.putShortVolatile(param0, param1, param2);
    }

    public char getCharVolatile(Object param0, long param1) {
        return unsafe.getCharVolatile(param0, param1);
    }

    public void putCharVolatile(Object param0, long param1, char param2) {
        unsafe.putCharVolatile(param0, param1, param2);
    }

    public int getIntVolatile(Object param0, long param1) {
        return unsafe.getIntVolatile(param0, param1);
    }

    public void putIntVolatile(Object param0, long param1, int param2) {
        unsafe.putIntVolatile(param0, param1, param2);
    }

    public long getLongVolatile(Object param0, long param1) {
        return unsafe.getLongVolatile(param0, param1);
    }

    public void putLongVolatile(Object param0, long param1, long param2) {
        unsafe.putLongVolatile(param0, param1, param2);
    }

    public float getFloatVolatile(Object param0, long param1) {
        return unsafe.getFloatVolatile(param0, param1);
    }

    public void putFloatVolatile(Object param0, long param1, float param2) {
        unsafe.putFloatVolatile(param0, param1, param2);
    }

    public double getDoubleVolatile(Object param0, long param1) {
        return unsafe.getDoubleVolatile(param0, param1);
    }

    public void putDoubleVolatile(Object param0, long param1, double param2) {
        unsafe.putDoubleVolatile(param0, param1, param2);
    }

    public Object getReferenceOpaque(Object param0, long param1) {
        return unsafe.getReferenceOpaque(param0, param1);
    }

    public void putReferenceOpaque(Object param0, long param1, Object param2) {
        unsafe.putReferenceOpaque(param0, param1, param2);
    }

    public boolean getBooleanOpaque(Object param0, long param1) {
        return unsafe.getBooleanOpaque(param0, param1);
    }

    public void putBooleanOpaque(Object param0, long param1, boolean param2) {
        unsafe.putBooleanOpaque(param0, param1, param2);
    }

    public byte getByteOpaque(Object param0, long param1) {
        return unsafe.getByteOpaque(param0, param1);
    }

    public void putByteOpaque(Object param0, long param1, byte param2) {
        unsafe.putByteOpaque(param0, param1, param2);
    }

    public short getShortOpaque(Object param0, long param1) {
        return unsafe.getShortOpaque(param0, param1);
    }

    public void putShortOpaque(Object param0, long param1, short param2) {
        unsafe.putShortOpaque(param0, param1, param2);
    }

    public char getCharOpaque(Object param0, long param1) {
        return unsafe.getCharOpaque(param0, param1);
    }

    public void putCharOpaque(Object param0, long param1, char param2) {
        unsafe.putCharOpaque(param0, param1, param2);
    }

    public int getIntOpaque(Object param0, long param1) {
        return unsafe.getIntOpaque(param0, param1);
    }

    public void putIntOpaque(Object param0, long param1, int param2) {
        unsafe.putIntOpaque(param0, param1, param2);
    }

    public long getLongOpaque(Object param0, long param1) {
        return unsafe.getLongOpaque(param0, param1);
    }

    public void putLongOpaque(Object param0, long param1, long param2) {
        unsafe.putLongOpaque(param0, param1, param2);
    }

    public float getFloatOpaque(Object param0, long param1) {
        return unsafe.getFloatOpaque(param0, param1);
    }

    public void putFloatOpaque(Object param0, long param1, float param2) {
        unsafe.putFloatOpaque(param0, param1, param2);
    }

    public double getDoubleOpaque(Object param0, long param1) {
        return unsafe.getDoubleOpaque(param0, param1);
    }

    public void putDoubleOpaque(Object param0, long param1, double param2) {
        unsafe.putDoubleOpaque(param0, param1, param2);
    }

    public Object getReferenceAcquire(Object param0, long param1) {
        return unsafe.getReferenceAcquire(param0, param1);
    }

    public void putReferenceRelease(Object param0, long param1, Object param2) {
        unsafe.putReferenceRelease(param0, param1, param2);
    }

    public boolean getBooleanAcquire(Object param0, long param1) {
        return unsafe.getBooleanAcquire(param0, param1);
    }

    public void putBooleanRelease(Object param0, long param1, boolean param2) {
        unsafe.putBooleanRelease(param0, param1, param2);
    }

    public byte getByteAcquire(Object param0, long param1) {
        return unsafe.getByteAcquire(param0, param1);
    }

    public void putByteRelease(Object param0, long param1, byte param2) {
        unsafe.putByteRelease(param0, param1, param2);
    }

    public short getShortAcquire(Object param0, long param1) {
        return unsafe.getShortAcquire(param0, param1);
    }

    public void putShortRelease(Object param0, long param1, short param2) {
        unsafe.putShortRelease(param0, param1, param2);
    }

    public char getCharAcquire(Object param0, long param1) {
        return unsafe.getCharAcquire(param0, param1);
    }

    public void putCharRelease(Object param0, long param1, char param2) {
        unsafe.putCharRelease(param0, param1, param2);
    }

    public int getIntAcquire(Object param0, long param1) {
        return unsafe.getIntAcquire(param0, param1);
    }

    public void putIntRelease(Object param0, long param1, int param2) {
        unsafe.putIntRelease(param0, param1, param2);
    }

    public long getLongAcquire(Object param0, long param1) {
        return unsafe.getLongAcquire(param0, param1);
    }

    public void putLongRelease(Object param0, long param1, long param2) {
        unsafe.putLongRelease(param0, param1, param2);
    }

    public float getFloatAcquire(Object param0, long param1) {
        return unsafe.getFloatAcquire(param0, param1);
    }

    public void putFloatRelease(Object param0, long param1, float param2) {
        unsafe.putFloatRelease(param0, param1, param2);
    }

    public double getDoubleAcquire(Object param0, long param1) {
        return unsafe.getDoubleAcquire(param0, param1);
    }

    public void putDoubleRelease(Object param0, long param1, double param2) {
        unsafe.putDoubleRelease(param0, param1, param2);
    }

    public short getShortUnaligned(Object param0, long param1, boolean param2) {
        return unsafe.getShortUnaligned(param0, param1, param2);
    }

    public short getShortUnaligned(Object param0, long param1) {
        return unsafe.getShortUnaligned(param0, param1);
    }

    public void putShortUnaligned(Object param0, long param1, short param2) {
        unsafe.putShortUnaligned(param0, param1, param2);
    }

    public void putShortUnaligned(Object param0, long param1, short param2, boolean param3) {
        unsafe.putShortUnaligned(param0, param1, param2, param3);
    }

    public char getCharUnaligned(Object param0, long param1) {
        return unsafe.getCharUnaligned(param0, param1);
    }

    public char getCharUnaligned(Object param0, long param1, boolean param2) {
        return unsafe.getCharUnaligned(param0, param1, param2);
    }

    public void putCharUnaligned(Object param0, long param1, char param2, boolean param3) {
        unsafe.putCharUnaligned(param0, param1, param2, param3);
    }

    public void putCharUnaligned(Object param0, long param1, char param2) {
        unsafe.putCharUnaligned(param0, param1, param2);
    }

    public int getIntUnaligned(Object param0, long param1, boolean param2) {
        return unsafe.getIntUnaligned(param0, param1, param2);
    }

    public int getIntUnaligned(Object param0, long param1) {
        return unsafe.getIntUnaligned(param0, param1);
    }

    public void putIntUnaligned(Object param0, long param1, int param2) {
        unsafe.putIntUnaligned(param0, param1, param2);
    }

    public void putIntUnaligned(Object param0, long param1, int param2, boolean param3) {
        unsafe.putIntUnaligned(param0, param1, param2, param3);
    }

    public long getLongUnaligned(Object param0, long param1) {
        return unsafe.getLongUnaligned(param0, param1);
    }

    public long getLongUnaligned(Object param0, long param1, boolean param2) {
        return unsafe.getLongUnaligned(param0, param1, param2);
    }

    public void putLongUnaligned(Object param0, long param1, long param2, boolean param3) {
        unsafe.putLongUnaligned(param0, param1, param2, param3);
    }

    public void putLongUnaligned(Object param0, long param1, long param2) {
        unsafe.putLongUnaligned(param0, param1, param2);
    }

    public boolean compareAndSetReference(Object param0, long param1, Object param2, Object param3) {
        return unsafe.compareAndSetReference(param0, param1, param2, param3);
    }

    public Object compareAndExchangeReference(Object param0, long param1, Object param2, Object param3) {
        return unsafe.compareAndExchangeReference(param0, param1, param2, param3);
    }

    public Object compareAndExchangeReferenceAcquire(Object param0, long param1, Object param2, Object param3) {
        return unsafe.compareAndExchangeReferenceAcquire(param0, param1, param2, param3);
    }

    public Object compareAndExchangeReferenceRelease(Object param0, long param1, Object param2, Object param3) {
        return unsafe.compareAndExchangeReferenceRelease(param0, param1, param2, param3);
    }

    public boolean compareAndSetLong(Object param0, long param1, long param2, long param3) {
        return unsafe.compareAndSetLong(param0, param1, param2, param3);
    }

    public long compareAndExchangeLong(Object param0, long param1, long param2, long param3) {
        return unsafe.compareAndExchangeLong(param0, param1, param2, param3);
    }

    public long compareAndExchangeLongAcquire(Object param0, long param1, long param2, long param3) {
        return unsafe.compareAndExchangeLongAcquire(param0, param1, param2, param3);
    }

    public long compareAndExchangeLongRelease(Object param0, long param1, long param2, long param3) {
        return unsafe.compareAndExchangeLongRelease(param0, param1, param2, param3);
    }

    public boolean compareAndSetInt(Object param0, long param1, int param2, int param3) {
        return unsafe.compareAndSetInt(param0, param1, param2, param3);
    }

    public int compareAndExchangeInt(Object param0, long param1, int param2, int param3) {
        return unsafe.compareAndExchangeInt(param0, param1, param2, param3);
    }

    public int compareAndExchangeIntAcquire(Object param0, long param1, int param2, int param3) {
        return unsafe.compareAndExchangeIntAcquire(param0, param1, param2, param3);
    }

    public int compareAndExchangeIntRelease(Object param0, long param1, int param2, int param3) {
        return unsafe.compareAndExchangeIntRelease(param0, param1, param2, param3);
    }

    public boolean compareAndSetByte(Object param0, long param1, byte param2, byte param3) {
        return unsafe.compareAndSetByte(param0, param1, param2, param3);
    }

    public byte compareAndExchangeByte(Object param0, long param1, byte param2, byte param3) {
        return unsafe.compareAndExchangeByte(param0, param1, param2, param3);
    }

    public byte compareAndExchangeByteAcquire(Object param0, long param1, byte param2, byte param3) {
        return unsafe.compareAndExchangeByteAcquire(param0, param1, param2, param3);
    }

    public byte compareAndExchangeByteRelease(Object param0, long param1, byte param2, byte param3) {
        return unsafe.compareAndExchangeByteRelease(param0, param1, param2, param3);
    }

    public boolean compareAndSetShort(Object param0, long param1, short param2, short param3) {
        return unsafe.compareAndSetShort(param0, param1, param2, param3);
    }

    public short compareAndExchangeShort(Object param0, long param1, short param2, short param3) {
        return unsafe.compareAndExchangeShort(param0, param1, param2, param3);
    }

    public short compareAndExchangeShortAcquire(Object param0, long param1, short param2, short param3) {
        return unsafe.compareAndExchangeShortAcquire(param0, param1, param2, param3);
    }

    public short compareAndExchangeShortRelease(Object param0, long param1, short param2, short param3) {
        return unsafe.compareAndExchangeShortRelease(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetReferencePlain(Object param0, long param1, Object param2, Object param3) {
        return unsafe.weakCompareAndSetReferencePlain(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetReferenceAcquire(Object param0, long param1, Object param2, Object param3) {
        return unsafe.weakCompareAndSetReferenceAcquire(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetReferenceRelease(Object param0, long param1, Object param2, Object param3) {
        return unsafe.weakCompareAndSetReferenceRelease(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetReference(Object param0, long param1, Object param2, Object param3) {
        return unsafe.weakCompareAndSetReference(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetLongPlain(Object param0, long param1, long param2, long param3) {
        return unsafe.weakCompareAndSetLongPlain(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetLongAcquire(Object param0, long param1, long param2, long param3) {
        return unsafe.weakCompareAndSetLongAcquire(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetLongRelease(Object param0, long param1, long param2, long param3) {
        return unsafe.weakCompareAndSetLongRelease(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetLong(Object param0, long param1, long param2, long param3) {
        return unsafe.weakCompareAndSetLong(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetIntPlain(Object param0, long param1, int param2, int param3) {
        return unsafe.weakCompareAndSetIntPlain(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetIntAcquire(Object param0, long param1, int param2, int param3) {
        return unsafe.weakCompareAndSetIntAcquire(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetIntRelease(Object param0, long param1, int param2, int param3) {
        return unsafe.weakCompareAndSetIntRelease(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetInt(Object param0, long param1, int param2, int param3) {
        return unsafe.weakCompareAndSetInt(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetBytePlain(Object param0, long param1, byte param2, byte param3) {
        return unsafe.weakCompareAndSetBytePlain(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetByteAcquire(Object param0, long param1, byte param2, byte param3) {
        return unsafe.weakCompareAndSetByteAcquire(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetByteRelease(Object param0, long param1, byte param2, byte param3) {
        return unsafe.weakCompareAndSetByteRelease(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetByte(Object param0, long param1, byte param2, byte param3) {
        return unsafe.weakCompareAndSetByte(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetShortPlain(Object param0, long param1, short param2, short param3) {
        return unsafe.weakCompareAndSetShortPlain(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetShortAcquire(Object param0, long param1, short param2, short param3) {
        return unsafe.weakCompareAndSetShortAcquire(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetShortRelease(Object param0, long param1, short param2, short param3) {
        return unsafe.weakCompareAndSetShortRelease(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetShort(Object param0, long param1, short param2, short param3) {
        return unsafe.weakCompareAndSetShort(param0, param1, param2, param3);
    }

    public int getAndAddInt(Object param0, long param1, int param2) {
        return unsafe.getAndAddInt(param0, param1, param2);
    }

    public long getAndAddLong(Object param0, long param1, long param2) {
        return unsafe.getAndAddLong(param0, param1, param2);
    }

    public byte getAndAddByte(Object param0, long param1, byte param2) {
        return unsafe.getAndAddByte(param0, param1, param2);
    }

    public short getAndAddShort(Object param0, long param1, short param2) {
        return unsafe.getAndAddShort(param0, param1, param2);
    }

    public int getAndSetInt(Object param0, long param1, int param2) {
        return unsafe.getAndSetInt(param0, param1, param2);
    }

    public long getAndSetLong(Object param0, long param1, long param2) {
        return unsafe.getAndSetLong(param0, param1, param2);
    }

    public byte getAndSetByte(Object param0, long param1, byte param2) {
        return unsafe.getAndSetByte(param0, param1, param2);
    }

    public short getAndSetShort(Object param0, long param1, short param2) {
        return unsafe.getAndSetShort(param0, param1, param2);
    }

    public Object getAndSetReference(Object param0, long param1, Object param2) {
        return unsafe.getAndSetReference(param0, param1, param2);
    }

    public void park(boolean param0, long param1) {
        unsafe.park(param0, param1);
    }

    public void unpark(Object param0) {
        unsafe.unpark(param0);
    }

    public void throwException(Throwable param0) {
        unsafe.throwException(param0);
    }

    public Class defineClass(String param0, @NotNull byte[] param1, int param2, int param3, ClassLoader param4, java.security.ProtectionDomain param5) {
        return unsafe.defineClass(param0, param1, param2, param3, param4, param5);
    }

    public long objectFieldOffset(Class param0, String param1) {
        return unsafe.objectFieldOffset(param0, param1);
    }

    public long objectFieldOffset(@NotNull java.lang.reflect.Field param0) {
        return unsafe.objectFieldOffset(param0);
    }

    public Object staticFieldBase(@NotNull java.lang.reflect.Field param0) {
        return unsafe.staticFieldBase(param0);
    }

    public long staticFieldOffset(@NotNull java.lang.reflect.Field param0) {
        return unsafe.staticFieldOffset(param0);
    }

    public boolean shouldBeInitialized(@NotNull Class param0) {
        return unsafe.shouldBeInitialized(param0);
    }

    public void ensureClassInitialized(@NotNull Class param0) {
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

    public long getAddress(Object param0, long param1) {
        return unsafe.getAddress(param0, param1);
    }

    public void putAddress(long param0, long param1) {
        unsafe.putAddress(param0, param1);
    }

    public void putAddress(Object param0, long param1, long param2) {
        unsafe.putAddress(param0, param1, param2);
    }

    public Object getUncompressedObject(long param0) {
        return unsafe.getUncompressedObject(param0);
    }

    public long allocateMemory(long param0) {
        return unsafe.allocateMemory(param0);
    }

    public long reallocateMemory(long param0, long param1) {
        return unsafe.reallocateMemory(param0, param1);
    }

    public void setMemory(Object param0, long param1, long param2, byte param3) {
        unsafe.setMemory(param0, param1, param2, param3);
    }

    public void setMemory(long param0, long param1, byte param2) {
        unsafe.setMemory(param0, param1, param2);
    }

    public void copyMemory(long param0, long param1, long param2) {
        unsafe.copyMemory(param0, param1, param2);
    }

    public void copyMemory(Object param0, long param1, Object param2, long param3, long param4) {
        unsafe.copyMemory(param0, param1, param2, param3, param4);
    }

    public void copySwapMemory(Object param0, long param1, Object param2, long param3, long param4, long param5) {
        unsafe.copySwapMemory(param0, param1, param2, param3, param4, param5);
    }

    public void copySwapMemory(long param0, long param1, long param2, long param3) {
        unsafe.copySwapMemory(param0, param1, param2, param3);
    }

    public void freeMemory(long param0) {
        unsafe.freeMemory(param0);
    }

    public int arrayBaseOffset(@NotNull Class param0) {
        return unsafe.arrayBaseOffset(param0);
    }

    public int arrayIndexScale(@NotNull Class param0) {
        return unsafe.arrayIndexScale(param0);
    }

    public int addressSize() {
        return unsafe.addressSize();
    }

    public int pageSize() {
        return unsafe.pageSize();
    }

    public Class defineClass0(String param0, @NotNull byte[] param1, int param2, int param3, ClassLoader param4, java.security.ProtectionDomain param5) {
        return unsafe.defineClass0(param0, param1, param2, param3, param4, param5);
    }

    public Class defineAnonymousClass(@NotNull Class param0, @NotNull byte[] param1, Object[] param2) {
        return unsafe.defineAnonymousClass(param0, param1, param2);
    }

    public Object allocateUninitializedArray(@NotNull Class param0, int param1) {
        return unsafe.allocateUninitializedArray(param0, param1);
    }

    public boolean compareAndSetChar(Object param0, long param1, char param2, char param3) {
        return unsafe.compareAndSetChar(param0, param1, param2, param3);
    }

    public char compareAndExchangeChar(Object param0, long param1, char param2, char param3) {
        return unsafe.compareAndExchangeChar(param0, param1, param2, param3);
    }

    public char compareAndExchangeCharAcquire(Object param0, long param1, char param2, char param3) {
        return unsafe.compareAndExchangeCharAcquire(param0, param1, param2, param3);
    }

    public char compareAndExchangeCharRelease(Object param0, long param1, char param2, char param3) {
        return unsafe.compareAndExchangeCharRelease(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetChar(Object param0, long param1, char param2, char param3) {
        return unsafe.weakCompareAndSetChar(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetCharAcquire(Object param0, long param1, char param2, char param3) {
        return unsafe.weakCompareAndSetCharAcquire(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetCharRelease(Object param0, long param1, char param2, char param3) {
        return unsafe.weakCompareAndSetCharRelease(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetCharPlain(Object param0, long param1, char param2, char param3) {
        return unsafe.weakCompareAndSetCharPlain(param0, param1, param2, param3);
    }

    public boolean compareAndSetBoolean(Object param0, long param1, boolean param2, boolean param3) {
        return unsafe.compareAndSetBoolean(param0, param1, param2, param3);
    }

    public boolean compareAndExchangeBoolean(Object param0, long param1, boolean param2, boolean param3) {
        return unsafe.compareAndExchangeBoolean(param0, param1, param2, param3);
    }

    public boolean compareAndExchangeBooleanAcquire(Object param0, long param1, boolean param2, boolean param3) {
        return unsafe.compareAndExchangeBooleanAcquire(param0, param1, param2, param3);
    }

    public boolean compareAndExchangeBooleanRelease(Object param0, long param1, boolean param2, boolean param3) {
        return unsafe.compareAndExchangeBooleanRelease(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetBoolean(Object param0, long param1, boolean param2, boolean param3) {
        return unsafe.weakCompareAndSetBoolean(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetBooleanAcquire(Object param0, long param1, boolean param2, boolean param3) {
        return unsafe.weakCompareAndSetBooleanAcquire(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetBooleanRelease(Object param0, long param1, boolean param2, boolean param3) {
        return unsafe.weakCompareAndSetBooleanRelease(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetBooleanPlain(Object param0, long param1, boolean param2, boolean param3) {
        return unsafe.weakCompareAndSetBooleanPlain(param0, param1, param2, param3);
    }

    public boolean compareAndSetFloat(Object param0, long param1, float param2, float param3) {
        return unsafe.compareAndSetFloat(param0, param1, param2, param3);
    }

    public float compareAndExchangeFloat(Object param0, long param1, float param2, float param3) {
        return unsafe.compareAndExchangeFloat(param0, param1, param2, param3);
    }

    public float compareAndExchangeFloatAcquire(Object param0, long param1, float param2, float param3) {
        return unsafe.compareAndExchangeFloatAcquire(param0, param1, param2, param3);
    }

    public float compareAndExchangeFloatRelease(Object param0, long param1, float param2, float param3) {
        return unsafe.compareAndExchangeFloatRelease(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetFloatPlain(Object param0, long param1, float param2, float param3) {
        return unsafe.weakCompareAndSetFloatPlain(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetFloatAcquire(Object param0, long param1, float param2, float param3) {
        return unsafe.weakCompareAndSetFloatAcquire(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetFloatRelease(Object param0, long param1, float param2, float param3) {
        return unsafe.weakCompareAndSetFloatRelease(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetFloat(Object param0, long param1, float param2, float param3) {
        return unsafe.weakCompareAndSetFloat(param0, param1, param2, param3);
    }

    public boolean compareAndSetDouble(Object param0, long param1, double param2, double param3) {
        return unsafe.compareAndSetDouble(param0, param1, param2, param3);
    }

    public double compareAndExchangeDouble(Object param0, long param1, double param2, double param3) {
        return unsafe.compareAndExchangeDouble(param0, param1, param2, param3);
    }

    public double compareAndExchangeDoubleAcquire(Object param0, long param1, double param2, double param3) {
        return unsafe.compareAndExchangeDoubleAcquire(param0, param1, param2, param3);
    }

    public double compareAndExchangeDoubleRelease(Object param0, long param1, double param2, double param3) {
        return unsafe.compareAndExchangeDoubleRelease(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetDoublePlain(Object param0, long param1, double param2, double param3) {
        return unsafe.weakCompareAndSetDoublePlain(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetDoubleAcquire(Object param0, long param1, double param2, double param3) {
        return unsafe.weakCompareAndSetDoubleAcquire(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetDoubleRelease(Object param0, long param1, double param2, double param3) {
        return unsafe.weakCompareAndSetDoubleRelease(param0, param1, param2, param3);
    }

    public boolean weakCompareAndSetDouble(Object param0, long param1, double param2, double param3) {
        return unsafe.weakCompareAndSetDouble(param0, param1, param2, param3);
    }

    public int getLoadAverage(double[] param0, int param1) {
        return unsafe.getLoadAverage(param0, param1);
    }

    public int getAndAddIntRelease(Object param0, long param1, int param2) {
        return unsafe.getAndAddIntRelease(param0, param1, param2);
    }

    public int getAndAddIntAcquire(Object param0, long param1, int param2) {
        return unsafe.getAndAddIntAcquire(param0, param1, param2);
    }

    public long getAndAddLongRelease(Object param0, long param1, long param2) {
        return unsafe.getAndAddLongRelease(param0, param1, param2);
    }

    public long getAndAddLongAcquire(Object param0, long param1, long param2) {
        return unsafe.getAndAddLongAcquire(param0, param1, param2);
    }

    public byte getAndAddByteRelease(Object param0, long param1, byte param2) {
        return unsafe.getAndAddByteRelease(param0, param1, param2);
    }

    public byte getAndAddByteAcquire(Object param0, long param1, byte param2) {
        return unsafe.getAndAddByteAcquire(param0, param1, param2);
    }

    public short getAndAddShortRelease(Object param0, long param1, short param2) {
        return unsafe.getAndAddShortRelease(param0, param1, param2);
    }

    public short getAndAddShortAcquire(Object param0, long param1, short param2) {
        return unsafe.getAndAddShortAcquire(param0, param1, param2);
    }

    public char getAndAddChar(Object param0, long param1, char param2) {
        return unsafe.getAndAddChar(param0, param1, param2);
    }

    public char getAndAddCharRelease(Object param0, long param1, char param2) {
        return unsafe.getAndAddCharRelease(param0, param1, param2);
    }

    public char getAndAddCharAcquire(Object param0, long param1, char param2) {
        return unsafe.getAndAddCharAcquire(param0, param1, param2);
    }

    public float getAndAddFloat(Object param0, long param1, float param2) {
        return unsafe.getAndAddFloat(param0, param1, param2);
    }

    public float getAndAddFloatRelease(Object param0, long param1, float param2) {
        return unsafe.getAndAddFloatRelease(param0, param1, param2);
    }

    public float getAndAddFloatAcquire(Object param0, long param1, float param2) {
        return unsafe.getAndAddFloatAcquire(param0, param1, param2);
    }

    public double getAndAddDouble(Object param0, long param1, double param2) {
        return unsafe.getAndAddDouble(param0, param1, param2);
    }

    public double getAndAddDoubleRelease(Object param0, long param1, double param2) {
        return unsafe.getAndAddDoubleRelease(param0, param1, param2);
    }

    public double getAndAddDoubleAcquire(Object param0, long param1, double param2) {
        return unsafe.getAndAddDoubleAcquire(param0, param1, param2);
    }

    public int getAndSetIntRelease(Object param0, long param1, int param2) {
        return unsafe.getAndSetIntRelease(param0, param1, param2);
    }

    public int getAndSetIntAcquire(Object param0, long param1, int param2) {
        return unsafe.getAndSetIntAcquire(param0, param1, param2);
    }

    public long getAndSetLongRelease(Object param0, long param1, long param2) {
        return unsafe.getAndSetLongRelease(param0, param1, param2);
    }

    public long getAndSetLongAcquire(Object param0, long param1, long param2) {
        return unsafe.getAndSetLongAcquire(param0, param1, param2);
    }

    public Object getAndSetReferenceRelease(Object param0, long param1, Object param2) {
        return unsafe.getAndSetReferenceRelease(param0, param1, param2);
    }

    public Object getAndSetReferenceAcquire(Object param0, long param1, Object param2) {
        return unsafe.getAndSetReferenceAcquire(param0, param1, param2);
    }

    public byte getAndSetByteRelease(Object param0, long param1, byte param2) {
        return unsafe.getAndSetByteRelease(param0, param1, param2);
    }

    public byte getAndSetByteAcquire(Object param0, long param1, byte param2) {
        return unsafe.getAndSetByteAcquire(param0, param1, param2);
    }

    public boolean getAndSetBoolean(Object param0, long param1, boolean param2) {
        return unsafe.getAndSetBoolean(param0, param1, param2);
    }

    public boolean getAndSetBooleanRelease(Object param0, long param1, boolean param2) {
        return unsafe.getAndSetBooleanRelease(param0, param1, param2);
    }

    public boolean getAndSetBooleanAcquire(Object param0, long param1, boolean param2) {
        return unsafe.getAndSetBooleanAcquire(param0, param1, param2);
    }

    public short getAndSetShortRelease(Object param0, long param1, short param2) {
        return unsafe.getAndSetShortRelease(param0, param1, param2);
    }

    public short getAndSetShortAcquire(Object param0, long param1, short param2) {
        return unsafe.getAndSetShortAcquire(param0, param1, param2);
    }

    public char getAndSetChar(Object param0, long param1, char param2) {
        return unsafe.getAndSetChar(param0, param1, param2);
    }

    public char getAndSetCharRelease(Object param0, long param1, char param2) {
        return unsafe.getAndSetCharRelease(param0, param1, param2);
    }

    public char getAndSetCharAcquire(Object param0, long param1, char param2) {
        return unsafe.getAndSetCharAcquire(param0, param1, param2);
    }

    public float getAndSetFloat(Object param0, long param1, float param2) {
        return unsafe.getAndSetFloat(param0, param1, param2);
    }

    public float getAndSetFloatRelease(Object param0, long param1, float param2) {
        return unsafe.getAndSetFloatRelease(param0, param1, param2);
    }

    public float getAndSetFloatAcquire(Object param0, long param1, float param2) {
        return unsafe.getAndSetFloatAcquire(param0, param1, param2);
    }

    public double getAndSetDouble(Object param0, long param1, double param2) {
        return unsafe.getAndSetDouble(param0, param1, param2);
    }

    public double getAndSetDoubleRelease(Object param0, long param1, double param2) {
        return unsafe.getAndSetDoubleRelease(param0, param1, param2);
    }

    public double getAndSetDoubleAcquire(Object param0, long param1, double param2) {
        return unsafe.getAndSetDoubleAcquire(param0, param1, param2);
    }

    public boolean getAndBitwiseOrBoolean(Object param0, long param1, boolean param2) {
        return unsafe.getAndBitwiseOrBoolean(param0, param1, param2);
    }

    public boolean getAndBitwiseOrBooleanRelease(Object param0, long param1, boolean param2) {
        return unsafe.getAndBitwiseOrBooleanRelease(param0, param1, param2);
    }

    public boolean getAndBitwiseOrBooleanAcquire(Object param0, long param1, boolean param2) {
        return unsafe.getAndBitwiseOrBooleanAcquire(param0, param1, param2);
    }

    public boolean getAndBitwiseAndBoolean(Object param0, long param1, boolean param2) {
        return unsafe.getAndBitwiseAndBoolean(param0, param1, param2);
    }

    public boolean getAndBitwiseAndBooleanRelease(Object param0, long param1, boolean param2) {
        return unsafe.getAndBitwiseAndBooleanRelease(param0, param1, param2);
    }

    public boolean getAndBitwiseAndBooleanAcquire(Object param0, long param1, boolean param2) {
        return unsafe.getAndBitwiseAndBooleanAcquire(param0, param1, param2);
    }

    public boolean getAndBitwiseXorBoolean(Object param0, long param1, boolean param2) {
        return unsafe.getAndBitwiseXorBoolean(param0, param1, param2);
    }

    public boolean getAndBitwiseXorBooleanRelease(Object param0, long param1, boolean param2) {
        return unsafe.getAndBitwiseXorBooleanRelease(param0, param1, param2);
    }

    public boolean getAndBitwiseXorBooleanAcquire(Object param0, long param1, boolean param2) {
        return unsafe.getAndBitwiseXorBooleanAcquire(param0, param1, param2);
    }

    public byte getAndBitwiseOrByte(Object param0, long param1, byte param2) {
        return unsafe.getAndBitwiseOrByte(param0, param1, param2);
    }

    public byte getAndBitwiseOrByteRelease(Object param0, long param1, byte param2) {
        return unsafe.getAndBitwiseOrByteRelease(param0, param1, param2);
    }

    public byte getAndBitwiseOrByteAcquire(Object param0, long param1, byte param2) {
        return unsafe.getAndBitwiseOrByteAcquire(param0, param1, param2);
    }

    public byte getAndBitwiseAndByte(Object param0, long param1, byte param2) {
        return unsafe.getAndBitwiseAndByte(param0, param1, param2);
    }

    public byte getAndBitwiseAndByteRelease(Object param0, long param1, byte param2) {
        return unsafe.getAndBitwiseAndByteRelease(param0, param1, param2);
    }

    public byte getAndBitwiseAndByteAcquire(Object param0, long param1, byte param2) {
        return unsafe.getAndBitwiseAndByteAcquire(param0, param1, param2);
    }

    public byte getAndBitwiseXorByte(Object param0, long param1, byte param2) {
        return unsafe.getAndBitwiseXorByte(param0, param1, param2);
    }

    public byte getAndBitwiseXorByteRelease(Object param0, long param1, byte param2) {
        return unsafe.getAndBitwiseXorByteRelease(param0, param1, param2);
    }

    public byte getAndBitwiseXorByteAcquire(Object param0, long param1, byte param2) {
        return unsafe.getAndBitwiseXorByteAcquire(param0, param1, param2);
    }

    public char getAndBitwiseOrChar(Object param0, long param1, char param2) {
        return unsafe.getAndBitwiseOrChar(param0, param1, param2);
    }

    public char getAndBitwiseOrCharRelease(Object param0, long param1, char param2) {
        return unsafe.getAndBitwiseOrCharRelease(param0, param1, param2);
    }

    public char getAndBitwiseOrCharAcquire(Object param0, long param1, char param2) {
        return unsafe.getAndBitwiseOrCharAcquire(param0, param1, param2);
    }

    public char getAndBitwiseAndChar(Object param0, long param1, char param2) {
        return unsafe.getAndBitwiseAndChar(param0, param1, param2);
    }

    public char getAndBitwiseAndCharRelease(Object param0, long param1, char param2) {
        return unsafe.getAndBitwiseAndCharRelease(param0, param1, param2);
    }

    public char getAndBitwiseAndCharAcquire(Object param0, long param1, char param2) {
        return unsafe.getAndBitwiseAndCharAcquire(param0, param1, param2);
    }

    public char getAndBitwiseXorChar(Object param0, long param1, char param2) {
        return unsafe.getAndBitwiseXorChar(param0, param1, param2);
    }

    public char getAndBitwiseXorCharRelease(Object param0, long param1, char param2) {
        return unsafe.getAndBitwiseXorCharRelease(param0, param1, param2);
    }

    public char getAndBitwiseXorCharAcquire(Object param0, long param1, char param2) {
        return unsafe.getAndBitwiseXorCharAcquire(param0, param1, param2);
    }

    public short getAndBitwiseOrShort(Object param0, long param1, short param2) {
        return unsafe.getAndBitwiseOrShort(param0, param1, param2);
    }

    public short getAndBitwiseOrShortRelease(Object param0, long param1, short param2) {
        return unsafe.getAndBitwiseOrShortRelease(param0, param1, param2);
    }

    public short getAndBitwiseOrShortAcquire(Object param0, long param1, short param2) {
        return unsafe.getAndBitwiseOrShortAcquire(param0, param1, param2);
    }

    public short getAndBitwiseAndShort(Object param0, long param1, short param2) {
        return unsafe.getAndBitwiseAndShort(param0, param1, param2);
    }

    public short getAndBitwiseAndShortRelease(Object param0, long param1, short param2) {
        return unsafe.getAndBitwiseAndShortRelease(param0, param1, param2);
    }

    public short getAndBitwiseAndShortAcquire(Object param0, long param1, short param2) {
        return unsafe.getAndBitwiseAndShortAcquire(param0, param1, param2);
    }

    public short getAndBitwiseXorShort(Object param0, long param1, short param2) {
        return unsafe.getAndBitwiseXorShort(param0, param1, param2);
    }

    public short getAndBitwiseXorShortRelease(Object param0, long param1, short param2) {
        return unsafe.getAndBitwiseXorShortRelease(param0, param1, param2);
    }

    public short getAndBitwiseXorShortAcquire(Object param0, long param1, short param2) {
        return unsafe.getAndBitwiseXorShortAcquire(param0, param1, param2);
    }

    public int getAndBitwiseOrInt(Object param0, long param1, int param2) {
        return unsafe.getAndBitwiseOrInt(param0, param1, param2);
    }

    public int getAndBitwiseOrIntRelease(Object param0, long param1, int param2) {
        return unsafe.getAndBitwiseOrIntRelease(param0, param1, param2);
    }

    public int getAndBitwiseOrIntAcquire(Object param0, long param1, int param2) {
        return unsafe.getAndBitwiseOrIntAcquire(param0, param1, param2);
    }

    public int getAndBitwiseAndInt(Object param0, long param1, int param2) {
        return unsafe.getAndBitwiseAndInt(param0, param1, param2);
    }

    public int getAndBitwiseAndIntRelease(Object param0, long param1, int param2) {
        return unsafe.getAndBitwiseAndIntRelease(param0, param1, param2);
    }

    public int getAndBitwiseAndIntAcquire(Object param0, long param1, int param2) {
        return unsafe.getAndBitwiseAndIntAcquire(param0, param1, param2);
    }

    public int getAndBitwiseXorInt(Object param0, long param1, int param2) {
        return unsafe.getAndBitwiseXorInt(param0, param1, param2);
    }

    public int getAndBitwiseXorIntRelease(Object param0, long param1, int param2) {
        return unsafe.getAndBitwiseXorIntRelease(param0, param1, param2);
    }

    public int getAndBitwiseXorIntAcquire(Object param0, long param1, int param2) {
        return unsafe.getAndBitwiseXorIntAcquire(param0, param1, param2);
    }

    public long getAndBitwiseOrLong(Object param0, long param1, long param2) {
        return unsafe.getAndBitwiseOrLong(param0, param1, param2);
    }

    public long getAndBitwiseOrLongRelease(Object param0, long param1, long param2) {
        return unsafe.getAndBitwiseOrLongRelease(param0, param1, param2);
    }

    public long getAndBitwiseOrLongAcquire(Object param0, long param1, long param2) {
        return unsafe.getAndBitwiseOrLongAcquire(param0, param1, param2);
    }

    public long getAndBitwiseAndLong(Object param0, long param1, long param2) {
        return unsafe.getAndBitwiseAndLong(param0, param1, param2);
    }

    public long getAndBitwiseAndLongRelease(Object param0, long param1, long param2) {
        return unsafe.getAndBitwiseAndLongRelease(param0, param1, param2);
    }

    public long getAndBitwiseAndLongAcquire(Object param0, long param1, long param2) {
        return unsafe.getAndBitwiseAndLongAcquire(param0, param1, param2);
    }

    public long getAndBitwiseXorLong(Object param0, long param1, long param2) {
        return unsafe.getAndBitwiseXorLong(param0, param1, param2);
    }

    public long getAndBitwiseXorLongRelease(Object param0, long param1, long param2) {
        return unsafe.getAndBitwiseXorLongRelease(param0, param1, param2);
    }

    public long getAndBitwiseXorLongAcquire(Object param0, long param1, long param2) {
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

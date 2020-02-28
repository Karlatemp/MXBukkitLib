/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: SunUnsafe.java@author: karlatemp@vip.qq.com: 19-11-22 下午10:21@version: 2.0
 */
package cn.mcres.karlatemp.unsafe;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;
import java.util.Objects;
import java.util.Optional;

import sun.misc.Unsafe;

@SuppressWarnings({"unchecked", "RedundantSuppression", "rawtypes"})
class SunUnsafe extends cn.mcres.karlatemp.unsafe.Unsafe {
    private static final Unsafe unsafe;

    SunUnsafe() {

    }

    static {
        try {
            final Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            unsafe = (Unsafe) theUnsafe.get(null);
        } catch (Throwable throwable) {
            throw new ExceptionInInitializerError(throwable);
        }
    }

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

    @Override
    public void loadLoadFence() {
        loadFence();
    }

    @Override
    public void storeStoreFence() {
        storeFence();
    }

    @Override
    public boolean isBigEndian() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean unalignedAccess() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getLongUnaligned(Object o, long offset) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getLongUnaligned(Object o, long offset, boolean bigEndian) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getIntUnaligned(Object o, long offset) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getIntUnaligned(Object o, long offset, boolean bigEndian) {
        throw new UnsupportedOperationException();
    }

    @Override
    public short getShortUnaligned(Object o, long offset) {
        throw new UnsupportedOperationException();
    }

    @Override
    public short getShortUnaligned(Object o, long offset, boolean bigEndian) {
        throw new UnsupportedOperationException();
    }

    @Override
    public char getCharUnaligned(Object o, long offset) {
        throw new UnsupportedOperationException();
    }

    @Override
    public char getCharUnaligned(Object o, long offset, boolean bigEndian) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putLongUnaligned(Object o, long offset, long x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putLongUnaligned(Object o, long offset, long x, boolean bigEndian) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putIntUnaligned(Object o, long offset, int x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putIntUnaligned(Object o, long offset, int x, boolean bigEndian) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putShortUnaligned(Object o, long offset, short x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putShortUnaligned(Object o, long offset, short x, boolean bigEndian) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putCharUnaligned(Object o, long offset, char x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putCharUnaligned(Object o, long offset, char x, boolean bigEndian) {
        throw new UnsupportedOperationException();
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

    public void putShort(long param0, short param1) {
        unsafe.putShort(param0, param1);
    }

    public void putShort(Object param0, long param1, short param2) {
        unsafe.putShort(param0, param1, param2);
    }

    public char getChar(long param0) {
        return unsafe.getChar(param0);
    }

    public char getChar(Object param0, long param1) {
        return unsafe.getChar(param0, param1);
    }

    public void putChar(Object param0, long param1, char param2) {
        unsafe.putChar(param0, param1, param2);
    }

    public void putChar(long param0, char param1) {
        unsafe.putChar(param0, param1);
    }

    public int getInt(Object param0, long param1) {
        return unsafe.getInt(param0, param1);
    }

    public int getInt(long param0) {
        return unsafe.getInt(param0);
    }

    public void putInt(Object param0, long param1, int param2) {
        unsafe.putInt(param0, param1, param2);
    }

    public void putInt(long param0, int param1) {
        unsafe.putInt(param0, param1);
    }

    public long getLong(long param0) {
        return unsafe.getLong(param0);
    }

    public long getLong(Object param0, long param1) {
        return unsafe.getLong(param0, param1);
    }

    public void putLong(Object param0, long param1, long param2) {
        unsafe.putLong(param0, param1, param2);
    }

    public void putLong(long param0, long param1) {
        unsafe.putLong(param0, param1);
    }

    public float getFloat(long param0) {
        return unsafe.getFloat(param0);
    }

    public float getFloat(Object param0, long param1) {
        return unsafe.getFloat(param0, param1);
    }

    public void putFloat(long param0, float param1) {
        unsafe.putFloat(param0, param1);
    }

    public void putFloat(Object param0, long param1, float param2) {
        unsafe.putFloat(param0, param1, param2);
    }

    public double getDouble(long param0) {
        return unsafe.getDouble(param0);
    }

    public double getDouble(Object param0, long param1) {
        return unsafe.getDouble(param0, param1);
    }

    public void putDouble(long param0, double param1) {
        unsafe.putDouble(param0, param1);
    }

    public void putDouble(Object param0, long param1, double param2) {
        unsafe.putDouble(param0, param1, param2);
    }

    @Override
    public long getAddress(Object o, long offset) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAddress(Object o, long offset, long x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getUncompressedObject(long address) {
        return null;
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

    @Override
    public Object getReferenceAcquire(Object o, long offset) {
        return null;
    }

    @Override
    public boolean getBooleanAcquire(Object o, long offset) {
        return false;
    }

    @Override
    public byte getByteAcquire(Object o, long offset) {
        throw new UnsupportedOperationException();
    }

    @Override
    public short getShortAcquire(Object o, long offset) {
        throw new UnsupportedOperationException();
    }

    @Override
    public char getCharAcquire(Object o, long offset) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getIntAcquire(Object o, long offset) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getFloatAcquire(Object o, long offset) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getLongAcquire(Object o, long offset) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getDoubleAcquire(Object o, long offset) {
        throw new UnsupportedOperationException();
    }

    public int getAndAddInt(Object param0, long param1, int param2) {
        return unsafe.getAndAddInt(param0, param1, param2);
    }

    @Override
    public int getAndAddIntRelease(Object o, long offset, int delta) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getAndAddIntAcquire(Object o, long offset, int delta) {
        throw new UnsupportedOperationException();
    }

    public long getAndAddLong(Object param0, long param1, long param2) {
        return unsafe.getAndAddLong(param0, param1, param2);
    }

    @Override
    public long getAndAddLongRelease(Object o, long offset, long delta) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getAndAddLongAcquire(Object o, long offset, long delta) {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte getAndAddByte(Object o, long offset, byte delta) {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte getAndAddByteRelease(Object o, long offset, byte delta) {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte getAndAddByteAcquire(Object o, long offset, byte delta) {
        throw new UnsupportedOperationException();
    }

    @Override
    public short getAndAddShort(Object o, long offset, short delta) {
        throw new UnsupportedOperationException();
    }

    @Override
    public short getAndAddShortRelease(Object o, long offset, short delta) {
        throw new UnsupportedOperationException();
    }

    @Override
    public short getAndAddShortAcquire(Object o, long offset, short delta) {
        throw new UnsupportedOperationException();
    }

    @Override
    public char getAndAddChar(Object o, long offset, char delta) {
        throw new UnsupportedOperationException();
    }

    @Override
    public char getAndAddCharRelease(Object o, long offset, char delta) {
        throw new UnsupportedOperationException();
    }

    @Override
    public char getAndAddCharAcquire(Object o, long offset, char delta) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getAndAddFloat(Object o, long offset, float delta) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getAndAddFloatRelease(Object o, long offset, float delta) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getAndAddFloatAcquire(Object o, long offset, float delta) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getAndAddDouble(Object o, long offset, double delta) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getAndAddDoubleRelease(Object o, long offset, double delta) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getAndAddDoubleAcquire(Object o, long offset, double delta) {
        throw new UnsupportedOperationException();
    }

    public int getAndSetInt(Object param0, long param1, int param2) {
        return unsafe.getAndSetInt(param0, param1, param2);
    }

    @Override
    public int getAndSetIntRelease(Object o, long offset, int newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getAndSetIntAcquire(Object o, long offset, int newValue) {
        throw new UnsupportedOperationException();
    }

    public long getAndSetLong(Object param0, long param1, long param2) {
        return unsafe.getAndSetLong(param0, param1, param2);
    }

    @Override
    public long getAndSetLongRelease(Object o, long offset, long newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getAndSetLongAcquire(Object o, long offset, long newValue) {
        throw new UnsupportedOperationException();
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

    public long objectFieldOffset(@NotNull Field param0) {
        return unsafe.objectFieldOffset(param0);
    }

    @Override
    public long objectFieldOffset(Class<?> c, String name) {
        throw new UnsupportedOperationException();
    }

    public Object staticFieldBase(@NotNull Field param0) {
        return unsafe.staticFieldBase(param0);
    }

    public long staticFieldOffset(@NotNull Field param0) {
        return unsafe.staticFieldOffset(param0);
    }

    public boolean shouldBeInitialized(@NotNull Class param0) {
        return unsafe.shouldBeInitialized(param0);
    }

    public void ensureClassInitialized(@NotNull Class param0) {
        unsafe.ensureClassInitialized(param0);
    }

    public long getAddress(long param0) {
        return unsafe.getAddress(param0);
    }

    public void putAddress(long param0, long param1) {
        unsafe.putAddress(param0, param1);
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

    @Override
    public void copySwapMemory(Object srcBase, long srcOffset, Object destBase, long destOffset, long bytes, long elemSize) {
        throw new UnsupportedOperationException();
    }

    public void copyMemory(Object param0, long param1, Object param2, long param3, long param4) {
        unsafe.copyMemory(param0, param1, param2, param3, param4);
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

    @Override
    public Class<?> defineClass(String name, @NotNull byte[] b, int off, int len, ClassLoader loader, ProtectionDomain protectionDomain) {
        // This method exists in JDK8.
        // Does not exist in JDK12.
        return SR.defineClass.defineClass(unsafe, name, b, off, len, loader, protectionDomain);
    }

    @Override
    public Class<?> defineClass0(String name, @NotNull byte[] b, int off, int len, ClassLoader loader, ProtectionDomain protectionDomain) {
        // Same as above
        return SR.defineClass0.defineClass(unsafe, name, b, off, len, loader, protectionDomain);
    }

    @Override
    public Object allocateUninitializedArray(@NotNull Class<?> componentType, int length) {
        return null;
    }

    public Class defineAnonymousClass(@NotNull Class param0, @NotNull byte[] param1, Object[] param2) {
        return unsafe.defineAnonymousClass(param0, param1, param2);
    }

    public int getLoadAverage(double[] param0, int param1) {
        return unsafe.getLoadAverage(param0, param1);
    }

    @SuppressWarnings("Since15")
    public void invokeCleaner(java.nio.ByteBuffer param0) {
        unsafe.invokeCleaner(param0);
    }

    @Override
    public Object getReference(Object o, long offset) {
        return getObject(o, offset);
    }

    @Override
    public void putReference(Object o, long offset, Object x) {
        putObject(o, offset, x);
    }

    @Override
    public boolean compareAndSetReference(Object o, long offset, Object expected, Object x) {
        return compareAndSwapObject(o, offset, expected, x);
    }

    @Override
    public Object compareAndExchangeReference(Object o, long offset, Object expected, Object x) {
        return null;
    }

    @Override
    public Object compareAndExchangeReferenceAcquire(Object o, long offset, Object expected, Object x) {
        return null;
    }

    @Override
    public Object compareAndExchangeReferenceRelease(Object o, long offset, Object expected, Object x) {
        return null;
    }

    @Override
    public boolean weakCompareAndSetReferencePlain(Object o, long offset, Object expected, Object x) {
        return false;
    }

    @Override
    public boolean weakCompareAndSetReferenceAcquire(Object o, long offset, Object expected, Object x) {
        return false;
    }

    @Override
    public boolean weakCompareAndSetReferenceRelease(Object o, long offset, Object expected, Object x) {
        return false;
    }

    @Override
    public boolean weakCompareAndSetReference(Object o, long offset, Object expected, Object x) {
        return false;
    }

    @Override
    public boolean compareAndSetInt(Object o, long offset, int expected, int x) {
        return compareAndSwapInt(o, offset, expected, x);
    }

    @Override
    public int compareAndExchangeInt(Object o, long offset, int expected, int x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int compareAndExchangeIntAcquire(Object o, long offset, int expected, int x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int compareAndExchangeIntRelease(Object o, long offset, int expected, int x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean weakCompareAndSetIntPlain(Object o, long offset, int expected, int x) {
        return false;
    }

    @Override
    public boolean weakCompareAndSetIntAcquire(Object o, long offset, int expected, int x) {
        return false;
    }

    @Override
    public boolean weakCompareAndSetIntRelease(Object o, long offset, int expected, int x) {
        return false;
    }

    @Override
    public boolean weakCompareAndSetInt(Object o, long offset, int expected, int x) {
        return false;
    }

    @Override
    public byte compareAndExchangeByte(Object o, long offset, byte expected, byte x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean compareAndSetByte(Object o, long offset, byte expected, byte x) {
        return false;
    }

    @Override
    public boolean weakCompareAndSetByte(Object o, long offset, byte expected, byte x) {
        return false;
    }

    @Override
    public boolean weakCompareAndSetByteAcquire(Object o, long offset, byte expected, byte x) {
        return false;
    }

    @Override
    public boolean weakCompareAndSetByteRelease(Object o, long offset, byte expected, byte x) {
        return false;
    }

    @Override
    public boolean weakCompareAndSetBytePlain(Object o, long offset, byte expected, byte x) {
        return false;
    }

    @Override
    public byte compareAndExchangeByteAcquire(Object o, long offset, byte expected, byte x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte compareAndExchangeByteRelease(Object o, long offset, byte expected, byte x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public short compareAndExchangeShort(Object o, long offset, short expected, short x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean compareAndSetShort(Object o, long offset, short expected, short x) {
        return false;
    }

    @Override
    public boolean weakCompareAndSetShort(Object o, long offset, short expected, short x) {
        return false;
    }

    @Override
    public boolean weakCompareAndSetShortAcquire(Object o, long offset, short expected, short x) {
        return false;
    }

    @Override
    public boolean weakCompareAndSetShortRelease(Object o, long offset, short expected, short x) {
        return false;
    }

    @Override
    public boolean weakCompareAndSetShortPlain(Object o, long offset, short expected, short x) {
        return false;
    }

    @Override
    public short compareAndExchangeShortAcquire(Object o, long offset, short expected, short x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public short compareAndExchangeShortRelease(Object o, long offset, short expected, short x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean compareAndSetChar(Object o, long offset, char expected, char x) {
        return false;
    }

    @Override
    public char compareAndExchangeChar(Object o, long offset, char expected, char x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public char compareAndExchangeCharAcquire(Object o, long offset, char expected, char x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public char compareAndExchangeCharRelease(Object o, long offset, char expected, char x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean weakCompareAndSetChar(Object o, long offset, char expected, char x) {
        return false;
    }

    @Override
    public boolean weakCompareAndSetCharAcquire(Object o, long offset, char expected, char x) {
        return false;
    }

    @Override
    public boolean weakCompareAndSetCharRelease(Object o, long offset, char expected, char x) {
        return false;
    }

    @Override
    public boolean weakCompareAndSetCharPlain(Object o, long offset, char expected, char x) {
        return false;
    }

    @Override
    public boolean compareAndSetBoolean(Object o, long offset, boolean expected, boolean x) {
        return false;
    }

    @Override
    public boolean compareAndExchangeBoolean(Object o, long offset, boolean expected, boolean x) {
        return false;
    }

    @Override
    public boolean compareAndExchangeBooleanAcquire(Object o, long offset, boolean expected, boolean x) {
        return false;
    }

    @Override
    public boolean compareAndExchangeBooleanRelease(Object o, long offset, boolean expected, boolean x) {
        return false;
    }

    @Override
    public boolean weakCompareAndSetBoolean(Object o, long offset, boolean expected, boolean x) {
        return false;
    }

    @Override
    public boolean weakCompareAndSetBooleanAcquire(Object o, long offset, boolean expected, boolean x) {
        return false;
    }

    @Override
    public boolean weakCompareAndSetBooleanRelease(Object o, long offset, boolean expected, boolean x) {
        return false;
    }

    @Override
    public boolean weakCompareAndSetBooleanPlain(Object o, long offset, boolean expected, boolean x) {
        return false;
    }

    @Override
    public boolean compareAndSetFloat(Object o, long offset, float expected, float x) {
        return false;
    }

    @Override
    public float compareAndExchangeFloat(Object o, long offset, float expected, float x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float compareAndExchangeFloatAcquire(Object o, long offset, float expected, float x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float compareAndExchangeFloatRelease(Object o, long offset, float expected, float x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean weakCompareAndSetFloatPlain(Object o, long offset, float expected, float x) {
        return false;
    }

    @Override
    public boolean weakCompareAndSetFloatAcquire(Object o, long offset, float expected, float x) {
        return false;
    }

    @Override
    public boolean weakCompareAndSetFloatRelease(Object o, long offset, float expected, float x) {
        return false;
    }

    @Override
    public boolean weakCompareAndSetFloat(Object o, long offset, float expected, float x) {
        return false;
    }

    @Override
    public boolean compareAndSetDouble(Object o, long offset, double expected, double x) {
        return false;
    }

    @Override
    public double compareAndExchangeDouble(Object o, long offset, double expected, double x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double compareAndExchangeDoubleAcquire(Object o, long offset, double expected, double x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double compareAndExchangeDoubleRelease(Object o, long offset, double expected, double x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean weakCompareAndSetDoublePlain(Object o, long offset, double expected, double x) {
        return false;
    }

    @Override
    public boolean weakCompareAndSetDoubleAcquire(Object o, long offset, double expected, double x) {
        return false;
    }

    @Override
    public boolean weakCompareAndSetDoubleRelease(Object o, long offset, double expected, double x) {
        return false;
    }

    @Override
    public boolean weakCompareAndSetDouble(Object o, long offset, double expected, double x) {
        return false;
    }

    @Override
    public boolean compareAndSetLong(Object o, long offset, long expected, long x) {
        return compareAndSwapLong(o, offset, expected, x);
    }

    @Override
    public long compareAndExchangeLong(Object o, long offset, long expected, long x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long compareAndExchangeLongAcquire(Object o, long offset, long expected, long x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long compareAndExchangeLongRelease(Object o, long offset, long expected, long x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean weakCompareAndSetLongPlain(Object o, long offset, long expected, long x) {
        return false;
    }

    @Override
    public boolean weakCompareAndSetLongAcquire(Object o, long offset, long expected, long x) {
        return false;
    }

    @Override
    public boolean weakCompareAndSetLongRelease(Object o, long offset, long expected, long x) {
        return false;
    }

    @Override
    public boolean weakCompareAndSetLong(Object o, long offset, long expected, long x) {
        return false;
    }

    @Override
    public Object getReferenceVolatile(Object o, long offset) {
        return getObjectVolatile(o, offset);
    }

    @Override
    public void putReferenceVolatile(Object o, long offset, Object x) {
        putObjectVolatile(o, offset, x);
    }

    @Override
    public void putReferenceRelease(Object o, long offset, Object x) {
        putOrderedObject(o, offset, x);
    }

    @Override
    public void putBooleanRelease(Object o, long offset, boolean x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putByteRelease(Object o, long offset, byte x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putShortRelease(Object o, long offset, short x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putCharRelease(Object o, long offset, char x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putIntRelease(Object o, long offset, int x) {
        putOrderedInt(o, offset, x);
    }

    @Override
    public void putFloatRelease(Object o, long offset, float x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getAndSetReference(Object o, long offset, Object newValue) {
        return getAndSetObject(o, offset, newValue);
    }

    @Override
    public Object getAndSetReferenceRelease(Object o, long offset, Object newValue) {
        return null;
    }

    @Override
    public Object getAndSetReferenceAcquire(Object o, long offset, Object newValue) {
        return null;
    }

    @Override
    public byte getAndSetByte(Object o, long offset, byte newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte getAndSetByteRelease(Object o, long offset, byte newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte getAndSetByteAcquire(Object o, long offset, byte newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getAndSetBoolean(Object o, long offset, boolean newValue) {
        return false;
    }

    @Override
    public boolean getAndSetBooleanRelease(Object o, long offset, boolean newValue) {
        return false;
    }

    @Override
    public boolean getAndSetBooleanAcquire(Object o, long offset, boolean newValue) {
        return false;
    }

    @Override
    public short getAndSetShort(Object o, long offset, short newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public short getAndSetShortRelease(Object o, long offset, short newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public short getAndSetShortAcquire(Object o, long offset, short newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public char getAndSetChar(Object o, long offset, char newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public char getAndSetCharRelease(Object o, long offset, char newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public char getAndSetCharAcquire(Object o, long offset, char newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getAndSetFloat(Object o, long offset, float newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getAndSetFloatRelease(Object o, long offset, float newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getAndSetFloatAcquire(Object o, long offset, float newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getAndSetDouble(Object o, long offset, double newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getAndSetDoubleRelease(Object o, long offset, double newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getAndSetDoubleAcquire(Object o, long offset, double newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getAndBitwiseOrBoolean(Object o, long offset, boolean mask) {
        return false;
    }

    @Override
    public boolean getAndBitwiseOrBooleanRelease(Object o, long offset, boolean mask) {
        return false;
    }

    @Override
    public boolean getAndBitwiseOrBooleanAcquire(Object o, long offset, boolean mask) {
        return false;
    }

    @Override
    public boolean getAndBitwiseAndBoolean(Object o, long offset, boolean mask) {
        return false;
    }

    @Override
    public boolean getAndBitwiseAndBooleanRelease(Object o, long offset, boolean mask) {
        return false;
    }

    @Override
    public boolean getAndBitwiseAndBooleanAcquire(Object o, long offset, boolean mask) {
        return false;
    }

    @Override
    public boolean getAndBitwiseXorBoolean(Object o, long offset, boolean mask) {
        return false;
    }

    @Override
    public boolean getAndBitwiseXorBooleanRelease(Object o, long offset, boolean mask) {
        return false;
    }

    @Override
    public boolean getAndBitwiseXorBooleanAcquire(Object o, long offset, boolean mask) {
        return false;
    }

    @Override
    public byte getAndBitwiseOrByte(Object o, long offset, byte mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte getAndBitwiseOrByteRelease(Object o, long offset, byte mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte getAndBitwiseOrByteAcquire(Object o, long offset, byte mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte getAndBitwiseAndByte(Object o, long offset, byte mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte getAndBitwiseAndByteRelease(Object o, long offset, byte mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte getAndBitwiseAndByteAcquire(Object o, long offset, byte mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte getAndBitwiseXorByte(Object o, long offset, byte mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte getAndBitwiseXorByteRelease(Object o, long offset, byte mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte getAndBitwiseXorByteAcquire(Object o, long offset, byte mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public char getAndBitwiseOrChar(Object o, long offset, char mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public char getAndBitwiseOrCharRelease(Object o, long offset, char mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public char getAndBitwiseOrCharAcquire(Object o, long offset, char mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public char getAndBitwiseAndChar(Object o, long offset, char mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public char getAndBitwiseAndCharRelease(Object o, long offset, char mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public char getAndBitwiseAndCharAcquire(Object o, long offset, char mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public char getAndBitwiseXorChar(Object o, long offset, char mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public char getAndBitwiseXorCharRelease(Object o, long offset, char mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public char getAndBitwiseXorCharAcquire(Object o, long offset, char mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public short getAndBitwiseOrShort(Object o, long offset, short mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public short getAndBitwiseOrShortRelease(Object o, long offset, short mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public short getAndBitwiseOrShortAcquire(Object o, long offset, short mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public short getAndBitwiseAndShort(Object o, long offset, short mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public short getAndBitwiseAndShortRelease(Object o, long offset, short mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public short getAndBitwiseAndShortAcquire(Object o, long offset, short mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public short getAndBitwiseXorShort(Object o, long offset, short mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public short getAndBitwiseXorShortRelease(Object o, long offset, short mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public short getAndBitwiseXorShortAcquire(Object o, long offset, short mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getAndBitwiseOrInt(Object o, long offset, int mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getAndBitwiseOrIntRelease(Object o, long offset, int mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getAndBitwiseOrIntAcquire(Object o, long offset, int mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getAndBitwiseAndInt(Object o, long offset, int mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getAndBitwiseAndIntRelease(Object o, long offset, int mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getAndBitwiseAndIntAcquire(Object o, long offset, int mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getAndBitwiseXorInt(Object o, long offset, int mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getAndBitwiseXorIntRelease(Object o, long offset, int mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getAndBitwiseXorIntAcquire(Object o, long offset, int mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getAndBitwiseOrLong(Object o, long offset, long mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getAndBitwiseOrLongRelease(Object o, long offset, long mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getAndBitwiseOrLongAcquire(Object o, long offset, long mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getAndBitwiseAndLong(Object o, long offset, long mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getAndBitwiseAndLongRelease(Object o, long offset, long mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getAndBitwiseAndLongAcquire(Object o, long offset, long mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getAndBitwiseXorLong(Object o, long offset, long mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getAndBitwiseXorLongRelease(Object o, long offset, long mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getAndBitwiseXorLongAcquire(Object o, long offset, long mask) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putLongRelease(Object o, long offset, long x) {
        putOrderedLong(o, offset, x);
    }

    @Override
    public void putDoubleRelease(Object o, long offset, double x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getReferenceOpaque(Object o, long offset) {
        return null;
    }

    @Override
    public boolean getBooleanOpaque(Object o, long offset) {
        return false;
    }

    @Override
    public byte getByteOpaque(Object o, long offset) {
        throw new UnsupportedOperationException();
    }

    @Override
    public short getShortOpaque(Object o, long offset) {
        throw new UnsupportedOperationException();
    }

    @Override
    public char getCharOpaque(Object o, long offset) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getIntOpaque(Object o, long offset) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getFloatOpaque(Object o, long offset) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getLongOpaque(Object o, long offset) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getDoubleOpaque(Object o, long offset) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putReferenceOpaque(Object o, long offset, Object x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putBooleanOpaque(Object o, long offset, boolean x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putByteOpaque(Object o, long offset, byte x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putShortOpaque(Object o, long offset, short x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putCharOpaque(Object o, long offset, char x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putIntOpaque(Object o, long offset, int x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putFloatOpaque(Object o, long offset, float x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putLongOpaque(Object o, long offset, long x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putDoubleOpaque(Object o, long offset, double x) {
        throw new UnsupportedOperationException();
    }

    public Object getObject(Object param0, long param1) {
        return unsafe.getObject(param0, param1);
    }

    public Object getObjectVolatile(Object param0, long param1) {
        return unsafe.getObjectVolatile(param0, param1);
    }

    public void putObject(Object param0, long param1, Object param2) {
        unsafe.putObject(param0, param1, param2);
    }

    public void putObjectVolatile(Object param0, long param1, Object param2) {
        unsafe.putObjectVolatile(param0, param1, param2);
    }

    public Object getAndSetObject(Object param0, long param1, Object param2) {
        return unsafe.getAndSetObject(param0, param1, param2);
    }

    public boolean compareAndSwapObject(Object param0, long param1, Object param2, Object param3) {
        return unsafe.compareAndSwapObject(param0, param1, param2, param3);
    }

    public boolean compareAndSwapInt(Object param0, long param1, int param2, int param3) {
        return unsafe.compareAndSwapInt(param0, param1, param2, param3);
    }

    public boolean compareAndSwapLong(Object param0, long param1, long param2, long param3) {
        return unsafe.compareAndSwapLong(param0, param1, param2, param3);
    }

    public void putOrderedObject(Object param0, long param1, Object param2) {
        unsafe.putOrderedObject(param0, param1, param2);
    }

    public void putOrderedInt(Object param0, long param1, int param2) {
        unsafe.putOrderedInt(param0, param1, param2);
    }

    public void putOrderedLong(Object param0, long param1, long param2) {
        unsafe.putOrderedLong(param0, param1, param2);
    }
}

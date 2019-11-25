/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PacketDataSerializer.java@author: karlatemp@vip.qq.com: 19-11-17 上午12:09@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.packet;

import cn.mcres.karlatemp.mxlib.nbt.NBTCompressedStreamTools;
import cn.mcres.karlatemp.mxlib.nbt.NBTReadLimiter;
import cn.mcres.karlatemp.mxlib.nbt.NBTTagCompound;
import cn.mcres.karlatemp.mxlib.share.MinecraftKey;
import io.netty.buffer.*;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import io.netty.util.ByteProcessor;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

public class PacketDataSerializer extends ByteBuf {

    private final ByteBuf a;

    public PacketDataSerializer(ByteBuf bytebuf) {
        this.a = bytebuf;
    }

    public static int $unknown_a(int i) {
        for (int j = 1; j < 5; ++j) {
            if ((i & -1 << j * 7) == 0) {
                return j;
            }
        }

        return 5;
    }

    public PacketDataSerializer writeByteArray(byte[] abyte) {
        this.writeVarInt(abyte.length);
        this.writeBytes(abyte);
        return this;
    }

    public MinecraftKey readMinecraftKey() {
        return new MinecraftKey(readString(32767));
    }

    public PacketDataSerializer writeMinecraftKey(MinecraftKey key) {
        writeString(key.toString());
        return this;
    }

    public byte[] readByteArray() {
        return this.readByteArray(this.readableBytes());
    }

    public byte[] readByteArray(int allows) {
        int j = this.readVarInt();

        if (j > allows) {
            throw new DecoderException("ByteArray with size " + j + " is bigger than allowed " + allows);
        } else {
            byte[] abyte = new byte[j];

            this.readBytes(abyte);
            return abyte;
        }
    }

    public PacketDataSerializer writeVarIntArray(int[] aint) {
        this.writeVarInt(aint.length);
        int[] aint1 = aint;
        int i = aint.length;

        for (int j = 0; j < i; ++j) {
            int k = aint1[j];

            this.writeVarInt(k);
        }

        return this;
    }

    public int[] readVarIntArray() {
        return this.readVarIntArray(this.readableBytes());
    }

    public int[] readVarIntArray(int allowed) {
        int j = this.readVarInt();

        if (j > allowed) {
            throw new DecoderException("VarIntArray with size " + j + " is bigger than allowed " + allowed);
        } else {
            int[] aint = new int[j];

            for (int k = 0; k < aint.length; ++k) {
                aint[k] = this.readVarInt();
            }

            return aint;
        }
    }

    public PacketDataSerializer writeLongArray(long[] along) {
        this.writeVarInt(along.length);
        long[] along1 = along;
        int i = along.length;

        for (int j = 0; j < i; ++j) {
            long k = along1[j];

            this.writeLong(k);
        }

        return this;
    }

    public <T extends Enum<T>> T readEnum(Class<T> oclass) {
        return (oclass.getEnumConstants())[this.readVarInt()];
    }

    public PacketDataSerializer writeEnum(Enum<?> oenum) {
        return this.writeVarInt(oenum.ordinal());
    }

    public int readVarInt() {
        int i = 0;
        int j = 0;

        byte b0;

        do {
            b0 = this.readByte();
            i |= (b0 & 127) << j++ * 7;
            if (j > 5) {
                throw new RuntimeException("VarInt too big");
            }
        } while ((b0 & 128) == 128);

        return i;
    }

    public long readVarLong() {
        long i = 0L;
        int j = 0;

        byte b0;

        do {
            b0 = this.readByte();
            i |= (long) (b0 & 127) << j++ * 7;
            if (j > 10) {
                throw new RuntimeException("VarLong too big");
            }
        } while ((b0 & 128) == 128);

        return i;
    }

    public PacketDataSerializer writeUUID(UUID uuid) {
        this.writeLong(uuid.getMostSignificantBits());
        this.writeLong(uuid.getLeastSignificantBits());
        return this;
    }

    public UUID readUUID() {
        return new UUID(this.readLong(), this.readLong());
    }

    public PacketDataSerializer writeVarInt(int i) {
        while ((i & -128) != 0) {
            this.writeByte(i & 127 | 128);
            i >>>= 7;
        }

        this.writeByte(i);
        return this;
    }

    public PacketDataSerializer writeVarLong(long i) {
        while ((i & -128L) != 0L) {
            this.writeByte((int) (i & 127L) | 128);
            i >>>= 7;
        }

        this.writeByte((int) i);
        return this;
    }

    public PacketDataSerializer writeNBT(@Nullable NBTTagCompound nbttagcompound) {
        if (nbttagcompound == null) {
            this.writeByte(0);
        } else {
            try {
                NBTCompressedStreamTools.write(nbttagcompound, new ByteBufOutputStream(this));
            } catch (IOException ioexception) {
                throw new EncoderException(ioexception);
            }
        }

        return this;
    }

    @Nullable
    public NBTTagCompound readNBT() {
        int i = this.readerIndex();
        byte b0 = this.readByte();

        if (b0 == 0) {
            return null;
        } else {
            this.readerIndex(i);

            try {
                return NBTCompressedStreamTools.loadCompound(new ByteBufInputStream(this), new NBTReadLimiter(2097152L));
            } catch (IOException ioexception) {
                throw new EncoderException(ioexception);
            }
        }
    }
/*
    public PacketDataSerializer a(ItemStack itemstack) {
        if (itemstack.isEmpty()) {
            this.writeBoolean(false);
        } else {
            this.writeBoolean(true);
            Item item = itemstack.getItem();

            this.writeVarInt(Item.getId(item));
            this.writeByte(itemstack.getCount());
            NBTTagCompound nbttagcompound = null;

            if (item.usesDurability() || item.m()) {
                nbttagcompound = itemstack.getTag();
            }

            this.a(nbttagcompound);
        }

        return this;
    }

    public ItemStack m() {
        if (!this.readBoolean()) {
            return ItemStack.a;
        } else {
            int i = this.i();
            byte b0 = this.readByte();
            ItemStack itemstack = new ItemStack(Item.getById(i), b0);

            itemstack.setTag(this.l());
            return itemstack;
        }
    }*/

    public String readString(int i) {
        int j = this.readVarInt();

        if (j > i * 4) {
            throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + j + " > " + i * 4 + ")");
        } else if (j < 0) {
            throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
        } else {
            String s = this.toString(this.readerIndex(), j, StandardCharsets.UTF_8);

            this.readerIndex(this.readerIndex() + j);
            if (s.length() > i) {
                throw new DecoderException("The received string length is longer than maximum allowed (" + j + " > " + i + ")");
            } else {
                return s;
            }
        }
    }

    public PacketDataSerializer writeString(String s) {
        return this.writeString(s, 32767);
    }

    public PacketDataSerializer writeString(String s, int allowed) {
        byte[] abyte = s.getBytes(StandardCharsets.UTF_8);

        if (abyte.length > allowed) {
            throw new EncoderException("String too big (was " + abyte.length + " bytes encoded, max " + allowed + ")");
        } else {
            this.writeVarInt(abyte.length);
            this.writeBytes(abyte);
            return this;
        }
    }

    public Date readDate() {
        return new Date(this.readLong());
    }

    public PacketDataSerializer writeDate(Date date) {
        this.writeLong(date.getTime());
        return this;
    }

    @Override
    public int capacity() {
        return a.capacity();
    }

    @Override
    public ByteBuf capacity(int i) {
        return a.capacity(i);
    }

    @Override
    public int maxCapacity() {
        return a.maxCapacity();
    }

    @Override
    public ByteBufAllocator alloc() {
        return a.alloc();
    }

    @Deprecated
    @Override
    public ByteOrder order() {
        return a.order();
    }

    @Deprecated
    @Override
    public ByteBuf order(ByteOrder byteOrder) {
        return a.order(byteOrder);
    }

    @Override
    public ByteBuf unwrap() {
        return a.unwrap();
    }

    @Override
    public boolean isDirect() {
        return a.isDirect();
    }

    @Override
    public boolean isReadOnly() {
        return a.isReadOnly();
    }

    @Override
    public PacketDataSerializer asReadOnly() {
        return new PacketDataSerializer(a.asReadOnly());
    }

    @Override
    public int readerIndex() {
        return a.readerIndex();
    }

    @Override
    public ByteBuf readerIndex(int i) {
        return a.readerIndex(i);
    }

    @Override
    public int writerIndex() {
        return a.writerIndex();
    }

    @Override
    public ByteBuf writerIndex(int i) {
        return a.writerIndex(i);
    }

    @Override
    public ByteBuf setIndex(int i, int i1) {
        return a.setIndex(i, i1);
    }

    @Override
    public int readableBytes() {
        return a.readableBytes();
    }

    @Override
    public int writableBytes() {
        return a.writableBytes();
    }

    @Override
    public int maxWritableBytes() {
        return a.maxWritableBytes();
    }

    @Override
    public int maxFastWritableBytes() {
        return a.maxFastWritableBytes();
    }

    @Override
    public boolean isReadable() {
        return a.isReadable();
    }

    @Override
    public boolean isReadable(int i) {
        return a.isReadable(i);
    }

    @Override
    public boolean isWritable() {
        return a.isWritable();
    }

    @Override
    public boolean isWritable(int i) {
        return a.isWritable(i);
    }

    @Override
    public ByteBuf clear() {
        return a.clear();
    }

    @Override
    public ByteBuf markReaderIndex() {
        return a.markReaderIndex();
    }

    @Override
    public ByteBuf resetReaderIndex() {
        return a.resetReaderIndex();
    }

    @Override
    public ByteBuf markWriterIndex() {
        return a.markWriterIndex();
    }

    @Override
    public ByteBuf resetWriterIndex() {
        return a.resetWriterIndex();
    }

    @Override
    public ByteBuf discardReadBytes() {
        return a.discardReadBytes();
    }

    @Override
    public ByteBuf discardSomeReadBytes() {
        return a.discardSomeReadBytes();
    }

    @Override
    public ByteBuf ensureWritable(int i) {
        return a.ensureWritable(i);
    }

    @Override
    public int ensureWritable(int i, boolean b) {
        return a.ensureWritable(i, b);
    }

    @Override
    public boolean getBoolean(int i) {
        return a.getBoolean(i);
    }

    @Override
    public byte getByte(int i) {
        return a.getByte(i);
    }

    @Override
    public short getUnsignedByte(int i) {
        return a.getUnsignedByte(i);
    }

    @Override
    public short getShort(int i) {
        return a.getShort(i);
    }

    @Override
    public short getShortLE(int index) {
        return a.getShortLE(index);
    }

    @Override
    public int getUnsignedShort(int i) {
        return a.getUnsignedShort(i);
    }

    @Override
    public int getUnsignedShortLE(int index) {
        return a.getUnsignedShortLE(index);
    }

    @Override
    public int getMedium(int i) {
        return a.getMedium(i);
    }

    @Override
    public int getMediumLE(int index) {
        return a.getMediumLE(index);
    }

    @Override
    public int getUnsignedMedium(int i) {
        return a.getUnsignedMedium(i);
    }

    @Override
    public int getUnsignedMediumLE(int index) {
        return a.getUnsignedMediumLE(index);
    }

    @Override
    public int getInt(int i) {
        return a.getInt(i);
    }

    @Override
    public int getIntLE(int index) {
        return a.getIntLE(index);
    }

    @Override
    public long getUnsignedInt(int i) {
        return a.getUnsignedInt(i);
    }

    @Override
    public long getUnsignedIntLE(int index) {
        return a.getUnsignedIntLE(index);
    }

    @Override
    public long getLong(int i) {
        return a.getLong(i);
    }

    @Override
    public long getLongLE(int index) {
        return a.getLongLE(index);
    }

    @Override
    public char getChar(int i) {
        return a.getChar(i);
    }

    @Override
    public float getFloat(int i) {
        return a.getFloat(i);
    }

    @Override
    public float getFloatLE(int index) {
        return a.getFloatLE(index);
    }

    @Override
    public double getDouble(int i) {
        return a.getDouble(i);
    }

    @Override
    public double getDoubleLE(int index) {
        return a.getDoubleLE(index);
    }

    @Override
    public ByteBuf getBytes(int i, ByteBuf byteBuf) {
        return a.getBytes(i, byteBuf);
    }

    @Override
    public ByteBuf getBytes(int i, ByteBuf byteBuf, int i1) {
        return a.getBytes(i, byteBuf, i1);
    }

    @Override
    public ByteBuf getBytes(int i, ByteBuf byteBuf, int i1, int i2) {
        return a.getBytes(i, byteBuf, i1, i2);
    }

    @Override
    public ByteBuf getBytes(int i, byte[] bytes) {
        return a.getBytes(i, bytes);
    }

    @Override
    public ByteBuf getBytes(int i, byte[] bytes, int i1, int i2) {
        return a.getBytes(i, bytes, i1, i2);
    }

    @Override
    public ByteBuf getBytes(int i, ByteBuffer byteBuffer) {
        return a.getBytes(i, byteBuffer);
    }

    @Override
    public ByteBuf getBytes(int i, OutputStream outputStream, int i1) throws IOException {
        return a.getBytes(i, outputStream, i1);
    }

    @Override
    public int getBytes(int i, GatheringByteChannel gatheringByteChannel, int i1) throws IOException {
        return a.getBytes(i, gatheringByteChannel, i1);
    }

    @Override
    public int getBytes(int index, FileChannel out, long position, int length) throws IOException {
        return a.getBytes(index, out, position, length);
    }

    @Override
    public CharSequence getCharSequence(int index, int length, Charset charset) {
        return a.getCharSequence(index, length, charset);
    }

    @Override
    public ByteBuf setBoolean(int i, boolean b) {
        return a.setBoolean(i, b);
    }

    @Override
    public ByteBuf setByte(int i, int i1) {
        return a.setByte(i, i1);
    }

    @Override
    public ByteBuf setShort(int i, int i1) {
        return a.setShort(i, i1);
    }

    @Override
    public ByteBuf setShortLE(int index, int value) {
        return a.setShortLE(index, value);
    }

    @Override
    public ByteBuf setMedium(int i, int i1) {
        return a.setMedium(i, i1);
    }

    @Override
    public ByteBuf setMediumLE(int index, int value) {
        return a.setMediumLE(index, value);
    }

    @Override
    public ByteBuf setInt(int i, int i1) {
        return a.setInt(i, i1);
    }

    @Override
    public ByteBuf setIntLE(int index, int value) {
        return a.setIntLE(index, value);
    }

    @Override
    public ByteBuf setLong(int i, long l) {
        return a.setLong(i, l);
    }

    @Override
    public ByteBuf setLongLE(int index, long value) {
        return a.setLongLE(index, value);
    }

    @Override
    public ByteBuf setChar(int i, int i1) {
        return a.setChar(i, i1);
    }

    @Override
    public ByteBuf setFloat(int i, float v) {
        return a.setFloat(i, v);
    }

    @Override
    public ByteBuf setFloatLE(int index, float value) {
        return a.setFloatLE(index, value);
    }

    @Override
    public ByteBuf setDouble(int i, double v) {
        return a.setDouble(i, v);
    }

    @Override
    public ByteBuf setDoubleLE(int index, double value) {
        return a.setDoubleLE(index, value);
    }

    @Override
    public ByteBuf setBytes(int i, ByteBuf byteBuf) {
        return a.setBytes(i, byteBuf);
    }

    @Override
    public ByteBuf setBytes(int i, ByteBuf byteBuf, int i1) {
        return a.setBytes(i, byteBuf, i1);
    }

    @Override
    public ByteBuf setBytes(int i, ByteBuf byteBuf, int i1, int i2) {
        return a.setBytes(i, byteBuf, i1, i2);
    }

    @Override
    public ByteBuf setBytes(int i, byte[] bytes) {
        return a.setBytes(i, bytes);
    }

    @Override
    public ByteBuf setBytes(int i, byte[] bytes, int i1, int i2) {
        return a.setBytes(i, bytes, i1, i2);
    }

    @Override
    public ByteBuf setBytes(int i, ByteBuffer byteBuffer) {
        return a.setBytes(i, byteBuffer);
    }

    @Override
    public int setBytes(int i, InputStream inputStream, int i1) throws IOException {
        return a.setBytes(i, inputStream, i1);
    }

    @Override
    public int setBytes(int i, ScatteringByteChannel scatteringByteChannel, int i1) throws IOException {
        return a.setBytes(i, scatteringByteChannel, i1);
    }

    @Override
    public int setBytes(int index, FileChannel in, long position, int length) throws IOException {
        return a.setBytes(index, in, position, length);
    }

    @Override
    public ByteBuf setZero(int i, int i1) {
        return a.setZero(i, i1);
    }

    @Override
    public int setCharSequence(int index, CharSequence sequence, Charset charset) {
        return a.setCharSequence(index, sequence, charset);
    }

    @Override
    public boolean readBoolean() {
        return a.readBoolean();
    }

    @Override
    public byte readByte() {
        return a.readByte();
    }

    @Override
    public short readUnsignedByte() {
        return a.readUnsignedByte();
    }

    @Override
    public short readShort() {
        return a.readShort();
    }

    @Override
    public short readShortLE() {
        return a.readShortLE();
    }

    @Override
    public int readUnsignedShort() {
        return a.readUnsignedShort();
    }

    @Override
    public int readUnsignedShortLE() {
        return a.readUnsignedShortLE();
    }

    @Override
    public int readMedium() {
        return a.readMedium();
    }

    @Override
    public int readMediumLE() {
        return a.readMediumLE();
    }

    @Override
    public int readUnsignedMedium() {
        return a.readUnsignedMedium();
    }

    @Override
    public int readUnsignedMediumLE() {
        return a.readUnsignedMediumLE();
    }

    @Override
    public int readInt() {
        return a.readInt();
    }

    @Override
    public int readIntLE() {
        return a.readIntLE();
    }

    @Override
    public long readUnsignedInt() {
        return a.readUnsignedInt();
    }

    @Override
    public long readUnsignedIntLE() {
        return a.readUnsignedIntLE();
    }

    @Override
    public long readLong() {
        return a.readLong();
    }

    @Override
    public long readLongLE() {
        return a.readLongLE();
    }

    @Override
    public char readChar() {
        return a.readChar();
    }

    @Override
    public float readFloat() {
        return a.readFloat();
    }

    @Override
    public float readFloatLE() {
        return a.readFloatLE();
    }

    @Override
    public double readDouble() {
        return a.readDouble();
    }

    @Override
    public double readDoubleLE() {
        return a.readDoubleLE();
    }

    @Override
    public ByteBuf readBytes(int i) {
        return a.readBytes(i);
    }

    @Override
    public ByteBuf readSlice(int i) {
        return a.readSlice(i);
    }

    @Override
    public ByteBuf readRetainedSlice(int length) {
        return a.readRetainedSlice(length);
    }

    @Override
    public ByteBuf readBytes(ByteBuf byteBuf) {
        return a.readBytes(byteBuf);
    }

    @Override
    public ByteBuf readBytes(ByteBuf byteBuf, int i) {
        return a.readBytes(byteBuf, i);
    }

    @Override
    public ByteBuf readBytes(ByteBuf byteBuf, int i, int i1) {
        return a.readBytes(byteBuf, i, i1);
    }

    @Override
    public ByteBuf readBytes(byte[] bytes) {
        return a.readBytes(bytes);
    }

    @Override
    public ByteBuf readBytes(byte[] bytes, int i, int i1) {
        return a.readBytes(bytes, i, i1);
    }

    @Override
    public ByteBuf readBytes(ByteBuffer byteBuffer) {
        return a.readBytes(byteBuffer);
    }

    @Override
    public ByteBuf readBytes(OutputStream outputStream, int i) throws IOException {
        return a.readBytes(outputStream, i);
    }

    @Override
    public int readBytes(GatheringByteChannel gatheringByteChannel, int i) throws IOException {
        return a.readBytes(gatheringByteChannel, i);
    }

    @Override
    public CharSequence readCharSequence(int length, Charset charset) {
        return a.readCharSequence(length, charset);
    }

    @Override
    public int readBytes(FileChannel out, long position, int length) throws IOException {
        return a.readBytes(out, position, length);
    }

    @Override
    public ByteBuf skipBytes(int i) {
        return a.skipBytes(i);
    }

    @Override
    public ByteBuf writeBoolean(boolean b) {
        return a.writeBoolean(b);
    }

    @Override
    public ByteBuf writeByte(int i) {
        return a.writeByte(i);
    }

    @Override
    public ByteBuf writeShort(int i) {
        return a.writeShort(i);
    }

    @Override
    public ByteBuf writeShortLE(int value) {
        return a.writeShortLE(value);
    }

    @Override
    public ByteBuf writeMedium(int i) {
        return a.writeMedium(i);
    }

    @Override
    public ByteBuf writeMediumLE(int value) {
        return a.writeMediumLE(value);
    }

    @Override
    public ByteBuf writeInt(int i) {
        return a.writeInt(i);
    }

    @Override
    public ByteBuf writeIntLE(int value) {
        return a.writeIntLE(value);
    }

    @Override
    public ByteBuf writeLong(long l) {
        return a.writeLong(l);
    }

    @Override
    public ByteBuf writeLongLE(long value) {
        return a.writeLongLE(value);
    }

    @Override
    public ByteBuf writeChar(int i) {
        return a.writeChar(i);
    }

    @Override
    public ByteBuf writeFloat(float v) {
        return a.writeFloat(v);
    }

    @Override
    public ByteBuf writeFloatLE(float value) {
        return a.writeFloatLE(value);
    }

    @Override
    public ByteBuf writeDouble(double v) {
        return a.writeDouble(v);
    }

    @Override
    public ByteBuf writeDoubleLE(double value) {
        return a.writeDoubleLE(value);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf byteBuf) {
        return a.writeBytes(byteBuf);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf byteBuf, int i) {
        return a.writeBytes(byteBuf, i);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf byteBuf, int i, int i1) {
        return a.writeBytes(byteBuf, i, i1);
    }

    @Override
    public ByteBuf writeBytes(byte[] bytes) {
        return a.writeBytes(bytes);
    }

    @Override
    public ByteBuf writeBytes(byte[] bytes, int i, int i1) {
        return a.writeBytes(bytes, i, i1);
    }

    @Override
    public ByteBuf writeBytes(ByteBuffer byteBuffer) {
        return a.writeBytes(byteBuffer);
    }

    @Override
    public int writeBytes(InputStream inputStream, int i) throws IOException {
        return a.writeBytes(inputStream, i);
    }

    @Override
    public int writeBytes(ScatteringByteChannel scatteringByteChannel, int i) throws IOException {
        return a.writeBytes(scatteringByteChannel, i);
    }

    @Override
    public int writeBytes(FileChannel in, long position, int length) throws IOException {
        return a.writeBytes(in, position, length);
    }

    @Override
    public ByteBuf writeZero(int i) {
        return a.writeZero(i);
    }

    @Override
    public int writeCharSequence(CharSequence sequence, Charset charset) {
        return a.writeCharSequence(sequence, charset);
    }

    @Override
    public int indexOf(int i, int i1, byte b) {
        return a.indexOf(i, i1, b);
    }

    @Override
    public int bytesBefore(byte b) {
        return a.bytesBefore(b);
    }

    @Override
    public int bytesBefore(int i, byte b) {
        return a.bytesBefore(i, b);
    }

    @Override
    public int bytesBefore(int i, int i1, byte b) {
        return a.bytesBefore(i, i1, b);
    }

    @Override
    public int forEachByte(ByteProcessor processor) {
        return a.forEachByte(processor);
    }

    @Override
    public int forEachByte(int index, int length, ByteProcessor processor) {
        return a.forEachByte(index, length, processor);
    }

    @Override
    public int forEachByteDesc(ByteProcessor processor) {
        return a.forEachByteDesc(processor);
    }

    @Override
    public int forEachByteDesc(int index, int length, ByteProcessor processor) {
        return a.forEachByteDesc(index, length, processor);
    }

    @Override
    public ByteBuf copy() {
        return a.copy();
    }

    @Override
    public ByteBuf copy(int i, int i1) {
        return a.copy(i, i1);
    }

    @Override
    public ByteBuf slice() {
        return a.slice();
    }

    @Override
    public ByteBuf retainedSlice() {
        return a.retainedSlice();
    }

    @Override
    public ByteBuf slice(int i, int i1) {
        return a.slice(i, i1);
    }

    @Override
    public ByteBuf retainedSlice(int index, int length) {
        return a.retainedSlice(index, length);
    }

    @Override
    public ByteBuf duplicate() {
        return a.duplicate();
    }

    @Override
    public ByteBuf retainedDuplicate() {
        return a.retainedDuplicate();
    }

    @Override
    public int nioBufferCount() {
        return a.nioBufferCount();
    }

    @Override
    public ByteBuffer nioBuffer() {
        return a.nioBuffer();
    }

    @Override
    public ByteBuffer nioBuffer(int i, int i1) {
        return a.nioBuffer(i, i1);
    }

    @Override
    public ByteBuffer internalNioBuffer(int i, int i1) {
        return a.internalNioBuffer(i, i1);
    }

    @Override
    public ByteBuffer[] nioBuffers() {
        return a.nioBuffers();
    }

    @Override
    public ByteBuffer[] nioBuffers(int i, int i1) {
        return a.nioBuffers(i, i1);
    }

    @Override
    public boolean hasArray() {
        return a.hasArray();
    }

    @Override
    public byte[] array() {
        return a.array();
    }

    @Override
    public int arrayOffset() {
        return a.arrayOffset();
    }

    @Override
    public boolean hasMemoryAddress() {
        return a.hasMemoryAddress();
    }

    @Override
    public long memoryAddress() {
        return a.memoryAddress();
    }

    @Override
    public String toString(Charset charset) {
        return a.toString(charset);
    }

    @Override
    public String toString(int i, int i1, Charset charset) {
        return a.toString(i, i1, charset);
    }

    @Override
    public int hashCode() {
        return a.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return a.equals(o);
    }

    @Override
    public int compareTo(ByteBuf byteBuf) {
        return a.compareTo(byteBuf);
    }

    @Override
    public String toString() {
        return a.toString();
    }

    @Override
    public ByteBuf retain(int i) {
        return a.retain(i);
    }

    @Override
    public ByteBuf retain() {
        return a.retain();
    }

    @Override
    public ByteBuf touch() {
        return a.touch();
    }

    @Override
    public ByteBuf touch(Object hint) {
        return a.touch(hint);
    }

    @Override
    public int refCnt() {
        return a.refCnt();
    }

    @Override
    public boolean release() {
        return a.release();
    }

    @Override
    public boolean release(int i) {
        return a.release(i);
    }
}

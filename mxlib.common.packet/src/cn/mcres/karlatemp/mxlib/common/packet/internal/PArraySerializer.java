/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: PArraySerializer.java@author: karlatemp@vip.qq.com: 2020/1/23 下午3:14@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.packet.internal;

import cn.mcres.karlatemp.mxlib.common.packet.MessageSerializer;
import cn.mcres.karlatemp.mxlib.common.packet.SerializeInput;
import cn.mcres.karlatemp.mxlib.common.packet.SerializeOutput;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class PArraySerializer implements MessageSerializer {

    @Override
    public boolean supportType(Class<?> type) {
        return type == int[].class || type == boolean[].class || type == short[].class || type == char[].class
                || type == byte[].class || type == long[].class || type == float[].class || type == double[].class
                || type == String[].class;
    }

    @Override
    public void serialize(Object object, SerializeOutput output) throws IOException {
        Class<?> type = object.getClass();
        final DataOutput o = output.getOutput();
        if (type == String[].class) {
            String[] sa = (String[]) object;
            o.writeShort(sa.length);
            for (String s : sa) output.writeString(s);
        } else if (type == int[].class) {
            int[] arr = (int[]) object;
            o.writeShort(arr.length);
            for (int i : arr) o.writeInt(i);
        } else if (type == boolean[].class) {
            boolean[] arr = (boolean[]) object;
            o.writeShort(arr.length);
            for (int i = 0; i < arr.length; i += 8) {
                int b = 0;
                for (int l = 0; l < 8; l++) {
                    int offset = i + l;
                    if (offset < arr.length) {
                        if (arr[offset]) {
                            b |= 1 << l;
                        }
                    } else break;
                }
                o.writeByte(b);
            }
        } else if (type == short[].class) {
            short[] arr = (short[]) object;
            o.writeShort(arr.length);
            for (short s : arr) o.writeShort(s);
        } else if (type == char[].class) {
            char[] c = (char[]) object;
            o.writeShort(c.length);
            for (char cx : c) o.writeChar(cx);
        } else if (type == byte[].class) {
            byte[] b = (byte[]) object;
            o.writeShort(b.length);
            o.write(b);
        } else if (type == long[].class) {
            long[] arr = (long[]) object;
            o.writeShort(arr.length);
            for (long l : arr) o.writeLong(l);
        } else if (type == float[].class) {
            float[] f = (float[]) object;
            o.writeShort(f.length);
            for (float fx : f) o.writeFloat(fx);
        } else if (type == double[].class) {
            double[] dd = (double[]) object;
            o.writeShort(dd.length);
            for (double d : dd) o.writeDouble(d);
        }
    }

    @Override
    public <T> T deserialize(Class<T> type, SerializeInput input) throws IOException {
        final DataInput i = input.getInput();
        if (type == String[].class) {
            String[] sa = new String[i.readUnsignedShort()];
            for (int i0 = 0; i0 < sa.length; i0++) sa[i0] = input.readString();
            //noinspection unchecked
            return (T) sa;
        } else if (type == int[].class) {
            int[] arr = new int[i.readUnsignedShort()];
            for (int k = 0; k < arr.length; k++) {
                arr[k] = i.readInt();
            }
            return (T) arr;
        } else if (type == boolean[].class) {
            int size = i.readUnsignedShort();
            boolean[] bl = new boolean[size];
            for (int k = 0; k < size; k += 8) {
                int data = i.readUnsignedByte();
                for (int l = 0; l < 8; l++) {
                    int offset = k + l;
                    if (offset < bl.length) {
                        bl[offset] = ((data >> l) & 1) != 0;
                    } else break;
                }
            }
            return (T) bl;
        } else if (type == short[].class) {
            short[] s = new short[i.readUnsignedShort()];
            for (int k = 0; k < s.length; k++) {
                s[k] = i.readShort();
            }
            return (T) s;
        } else if (type == char[].class) {
            char[] c = new char[i.readUnsignedShort()];
            for (int k = 0; k < c.length; k++) {
                c[k] = i.readChar();
            }
            return (T) c;
        } else if (type == byte[].class) {
            byte[] bs = new byte[i.readUnsignedShort()];
            i.readFully(bs);
            return (T) bs;
        } else if (type == long[].class) {
            long[] la = new long[i.readUnsignedShort()];
            for (int x = 0; x < la.length; x++) la[x] = i.readLong();
            return (T) la;
        } else if (type == float.class) {
            float[] f = new float[i.readUnsignedShort()];
            for (int r = 0; r < f.length; r++) f[r] = i.readFloat();
            return (T) f;
        } else if (type == double.class) {
            double[] d = new double[i.readUnsignedShort()];
            for (int w = 0; w < d.length; w++) d[w] = i.readDouble();
            return (T) d;
        }
        return null;
    }
}

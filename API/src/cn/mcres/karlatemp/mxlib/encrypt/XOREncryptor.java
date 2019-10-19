/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: XOREncryptor.java@author: karlatemp@vip.qq.com: 19-10-14 下午12:34@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.encrypt;

import cn.mcres.karlatemp.mxlib.exceptions.EncryptException;
import org.jetbrains.annotations.NotNull;

public class XOREncryptor implements Encryptor, Encryptor.Decoder, Encryptor.Encoder {
    final byte[] pwd;

    private static int ox(int ln, int at) {
        if (ln < 1) throw new ArrayIndexOutOfBoundsException(at);
        while (true) {
            if (at >= 0 && at < ln) return at;
            if (at < 0) at += ln;
            else at -= ln;
        }
    }

    public static XOREncryptor create(@NotNull byte[] pwd, int level) {
        switch (level) {
            case 1: {
                return new XOREncryptor(pwd) {

                    private byte a(int ax, int bx, int cx) {
                        byte[] pd = this.pwd;
                        int rrd = ax ^ bx | cx;
                        int box = (bx << 4) | (cx >> 2) & bx;
                        int ows = (cx >> bx) ^ (bx << ox(Byte.SIZE * 2, ax >> cx));
                        int ln = pd.length;
                        byte a = pd[ox(ln, rrd)];
                        byte b = pd[ox(ln, box)];
                        byte c = pd[ox(ln, ows)];
                        return (byte) ((((a | c) ^ b) >> (c & 0b111)) | (a & b));
                    }

                    @Override
                    protected byte pos(int index, int len) {
                        byte[] pd = this.pwd;
                        int dox = 10;
                        do {
                            int vvx = (index ^ dox) | (len << dox);
                            if (vvx < dox && vvx > 0) {
                                return a(dox, vvx, len);
                            }
                        } while (dox-- > 0);
                        return a(len, 777, index);
                    }
                };
            }
        }
        return new XOREncryptor(pwd);
    }

    public XOREncryptor(@NotNull byte[] pwd) {
        if (pwd.length == 0) throw new ArrayIndexOutOfBoundsException(0);
        this.pwd = pwd;
    }

    public int getLevel() {
        return 0;
    }

    protected byte pos(int index, int len) {
        int oxx = pwd.length;
        index *= 2;
        index -= len;
        len ^= index;
        return pwd[ox(oxx, len)];
    }

    @Override
    public byte[] encode(byte[] data) throws EncryptException {
        if (data != null) {
            int len = data.length;
            if (len != 0) {
                byte[] cp = new byte[len];
                for (int i = 0; i < len; i++) {
                    cp[i] = (byte) (0xFF & (data[i] ^ pos(i, len)));
                }
                return cp;
            }
            return data;
        }
        return new byte[0];
    }

    @Override
    public byte[] decode(byte[] b) throws EncryptException {
        return encode(b);
    }

    @Override
    public Encoder getEncoder() {
        return this;
    }

    @Override
    public Decoder getDecoder() {
        return this;
    }

    @Override
    public boolean isSupportEncoder() {
        return true;
    }

    @Override
    public boolean isSupportDecoder() {
        return true;
    }
}

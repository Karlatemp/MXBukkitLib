/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Encryptor.java@author: karlatemp@vip.qq.com: 19-10-14 下午12:22@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.encrypt;

import cn.mcres.karlatemp.mxlib.exceptions.EncryptException;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 数据加密器
 *
 * @since 2.4
 */
public interface Encryptor {
    interface Encoder {
        default String encodeToString(byte[] data) throws EncryptException {
            return new String(encode(data), UTF_8);
        }

        default String encodeToString(String data) throws EncryptException {
            return new String(encode(data), UTF_8);
        }

        byte[] encode(byte[] data) throws EncryptException;

        default byte[] encode(String data) throws EncryptException {
            return encode(data.getBytes(UTF_8));
        }
    }

    interface Decoder {


        byte[] decode(byte[] b) throws EncryptException;

        default byte[] decode(String data) throws EncryptException {
            return decode(data.getBytes(UTF_8));
        }

        default String decodeToString(String data) throws EncryptException {
            return new String(decode(data), UTF_8);
        }

        default String decodeToString(byte[] data) throws EncryptException {
            return new String(decode(data), UTF_8);
        }

    }

    Encoder getEncoder();

    Decoder getDecoder();

    boolean isSupportEncoder();

    boolean isSupportDecoder();
}

/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Actuator.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.encode;

import cn.mcres.karlatemp.mxlib.encrypt.Encryptor;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 加密器
 *
 * @author 32798
 */
public interface Actuator extends Encryptor {

    interface Encoder extends Encryptor.Encoder {
        default String encodeToString(byte[] b) throws EncodeException {
            return new String(encode(b), UTF_8);
        }

        default String encodeToString(String b) throws EncodeException {
            return new String(encode(b), UTF_8);
        }

        byte[] encode(byte[] b) throws EncodeException;

        default byte[] encode(String b) throws EncodeException {
            return encode(b.getBytes(UTF_8));
        }
    }

    interface Decoder extends Encryptor.Decoder {


        byte[] decode(byte[] b) throws DecodeException;

        default byte[] decode(String data) throws DecodeException {
            return decode(data.getBytes(UTF_8));
        }

        default String decodeToString(String data) throws DecodeException {
            return new String(decode(data), UTF_8);
        }

        default String decodeToString(byte[] data) throws DecodeException {
            return new String(decode(data), UTF_8);
        }

    }

    Encoder getEncoder();

    Decoder getDecoder();

    boolean isSupportEncoder();

    boolean isSupportDecoder();
}

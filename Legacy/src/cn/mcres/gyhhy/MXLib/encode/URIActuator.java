/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: URIActuator.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.encode;

import static java.nio.charset.StandardCharsets.UTF_8;

public class URIActuator implements Actuator, Actuator.Encoder, Actuator.Decoder {

    public static final URIActuator actuator = new URIActuator();

    public static URIActuator getInstance() {
        return actuator;
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

    @Override
    public String encodeToString(byte[] b) throws EncodeException {
        return encodeToString(new String(b, UTF_8));
    }

    @Override
    public String encodeToString(String b) throws EncodeException {
        return URLEncoder.encode(b, UTF_8);
    }

    @Override
    public byte[] encode(byte[] b) throws EncodeException {
        return encodeToString(b).getBytes(UTF_8);
    }

    @Override
    public byte[] encode(String b) throws EncodeException {
        return encodeToString(b).getBytes(UTF_8);
    }

    @Override
    public byte[] decode(byte[] b) throws DecodeException {
        return decodeToString(b).getBytes(UTF_8);
    }

    @Override
    public byte[] decode(String data) throws DecodeException {
        return decodeToString(data).getBytes(UTF_8);
    }

    @Override
    public String decodeToString(String data) throws DecodeException {
        return URLDecoder.decode(data, UTF_8);
    }

    @Override
    public String decodeToString(byte[] data) throws DecodeException {
        return decodeToString(new String(data, UTF_8));
    }

}

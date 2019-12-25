/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Base64Actuator.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.karlatemp.mxlib.encrypt;

import cn.mcres.karlatemp.mxlib.exceptions.DecodeException;

import java.util.Base64;

/**
 * @author 32798
 */
public class Base64Actuator implements SymmetricEncryptor, Encryptor.Encoder, Encryptor.Decoder {

    public static final Base64Actuator actuator = new Base64Actuator();

    public static Base64Actuator getInstance() {
        return actuator;
    }

    @Override
    public Base64Actuator getEncoder() {
        return this;
    }

    @Override
    public Base64Actuator getDecoder() {
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
    public byte[] decode(String data) {
        return Base64.getDecoder().decode(data.trim());
    }

    @Override
    public byte[] decode(byte[] b) {
        try {
            return Base64.getDecoder().decode(b);
        } catch (IllegalArgumentException iae) {
            throw new DecodeException(iae);
        }
    }

    @Override
    public String encodeToString(byte[] b) {
        return Base64.getEncoder().encodeToString(b);
    }

    @Override
    public byte[] encode(byte[] b) {
        return Base64.getEncoder().encode(b);
    }

}

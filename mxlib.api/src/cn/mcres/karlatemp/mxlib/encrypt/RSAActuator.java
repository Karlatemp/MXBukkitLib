/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: RSAActuator.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.karlatemp.mxlib.encrypt;

import cn.mcres.karlatemp.mxlib.encryption.RSAEncoder;
import cn.mcres.karlatemp.mxlib.exceptions.DecodeException;
import cn.mcres.karlatemp.mxlib.exceptions.EncodeException;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import static java.nio.charset.StandardCharsets.UTF_8;

public class RSAActuator implements AsymmetricEncryptor, Encryptor.Decoder, Encryptor.Encoder {

    private static final RSAEncoder encoder = new RSAEncoder();

    public static RSAEncoder getRSAEncoder() {
        return encoder;
    }

    private final RSAPublicKey pub;
    private final RSAPrivateKey pri;
    private boolean decodeUsePublic;
    private boolean encodeUsePublic;

    public RSAActuator(RSAPublicKey pub, RSAPrivateKey pri) {
        this.pri = pri;
        this.pub = pub;
        this.encodeUsePublic = true;
        this.decodeUsePublic = false;
    }

    public RSAActuator(byte[] pri, byte[] pub) {
        RSAPrivateKey p = null;
        RSAPublicKey b = null;
        if (pri != null) {
            p = encoder.getPrivateKey(pri);
        }
        if (pub != null) {
            b = encoder.getPublicKey(pub);
        }
        this.pri = p;
        this.pub = b;
        this.encodeUsePublic = true;
        this.decodeUsePublic = false;
    }

    @Override
    public byte[] decode(String data) throws DecodeException {
        return decode(Base64Actuator.getInstance().decode(data));
    }

    @Override
    public String encodeToString(String b) throws EncodeException {
        return Base64Actuator.getInstance().encodeToString(encode(b));
    }

    @Override
    public byte[] encode(String b) throws EncodeException {
        return encode(b.getBytes(UTF_8));
    }

    @Override
    public String decodeToString(String data) throws DecodeException {
        return decodeToString(Base64Actuator.getInstance().decode(data));
    }

    @Override
    public String decodeToString(byte[] data) throws DecodeException {
        return new String(decode(data), UTF_8);
    }

    @Override
    public String encodeToString(byte[] b) throws EncodeException {
        return Base64Actuator.getInstance().encodeToString(encode(b));
    }

    public boolean encodeUsePrivateKey() {
        return !this.encodeUsePublic;
    }

    public boolean encodeUsePublicKey() {
        return this.encodeUsePublic;
    }

    public boolean decodeUsePublicKey() {
        return this.decodeUsePublic;
    }

    public boolean decodeUsePrivateKey() {
        return !this.decodeUsePublic;
    }

    public RSAActuator setEncodeUsePrivateKey(boolean p) {
        this.encodeUsePublic = !p;
        return this;
    }

    public RSAActuator setEncodeUsePublicKey(boolean p) {
        this.encodeUsePublic = p;
        return this;
    }

    public RSAActuator setDecodeUsePublicKey(boolean p) {
        this.decodeUsePublic = p;
        return this;
    }

    public RSAActuator setDecodeUsePrivateKey(boolean p) {
        this.decodeUsePublic = !p;
        return this;
    }

    @Override
    public RSAActuator getEncoder() {
        if (!isSupportEncoder()) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        return this;
    }

    @Override
    public RSAActuator getDecoder() {
        if (!isSupportDecoder()) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        return this;
    }

    @Override
    public boolean isSupportEncoder() {
        if (this.encodeUsePrivateKey()) {
            return pri != null;
        }
        return pub != null;
    }

    @Override
    public boolean isSupportDecoder() {
        if (this.decodeUsePrivateKey()) {
            return pri != null;
        }
        return pub != null;
    }

    @Override
    public byte[] encode(byte[] b) throws DecodeException {
        if (this.encodeUsePrivateKey()) {
            return encoder.encode(b, pri);
        }
        return encoder.encode(b, pub);
    }

    @Override
    public byte[] decode(byte[] b) throws EncodeException {
        if (this.decodeUsePrivateKey()) {
            return encoder.decode(b, pri);
        }
        return encoder.decode(b, pub);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.encode;

import cn.mcres.gyhhy.MXLib.encryption.RSAEncoder;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class RSAActuator implements AsymmetricActuator, Actuator.Decoder, Actuator.Encoder {

    private static final RSAEncoder encoder = new RSAEncoder();

    public static RSAEncoder getRSAEncoder() {
        return encoder;
    }
    private final RSAPublicKey pub;
    private final RSAPrivateKey pri;
    private boolean useP;
    public RSAActuator(RSAPublicKey pub,RSAPrivateKey pri){
        this.pri = pri;
        this.pub = pub;
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
        this.useP = false;
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
        return new String(decode(data),UTF_8);
    }
    @Override
    public String encodeToString(byte[] b) throws EncodeException {
        return Base64Actuator.getInstance().encodeToString(encode(b));
    }

    public boolean encodeUsePrivateKey() {
        return useP;
    }

    public boolean encodeUsePublicKey() {
        return !useP;
    }

    public boolean decodeUsePublicKey() {
        return encodeUsePrivateKey();
    }

    public boolean decodeUsePrivateKey() {
        return encodeUsePublicKey();
    }

    public RSAActuator setEncodeUsePrivateKey(boolean p) {
        useP = p;
        return this;
    }

    public RSAActuator setEncodeUsePublicKey(boolean p) {
        return setEncodeUsePrivateKey(!p);
    }

    public RSAActuator setDecodeUsePublicKey(boolean p) {
        return setEncodeUsePrivateKey(p);
    }

    public RSAActuator setDecodeUsePrivateKey(boolean p) {
        return setEncodeUsePublicKey(p);
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

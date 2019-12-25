/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Encoder.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.encryption;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public abstract class Encoder extends cn.mcres.karlatemp.mxlib.encryption.Encoder {

    public static Cipher getCipher(String n) {
        return cn.mcres.karlatemp.mxlib.encryption.Encoder.getCipher(n);
    }

    public static KeyPair generateKeyPair(KeyPairGenerator go, int keysize) {
        return cn.mcres.karlatemp.mxlib.encryption.Encoder.generateKeyPair(go, keysize);
    }

    public static KeyFactory getKeyFactory(String algorithm) {
        return cn.mcres.karlatemp.mxlib.encryption.Encoder.getKeyFactory(algorithm);
    }

    public static PrivateKey getPrivateKey(KeyFactory kf, byte[] pri) {
        return cn.mcres.karlatemp.mxlib.encryption.Encoder.getPrivateKey(kf, pri);
    }

    public static PublicKey getPublicKey(KeyFactory kf, byte[] public_key) {
        return cn.mcres.karlatemp.mxlib.encryption.Encoder.getPublicKey(kf, public_key);
    }

    public static KeyPair generateKeyPair(KeyPairGenerator go, AlgorithmParameterSpec params, SecureRandom random) {
        return cn.mcres.karlatemp.mxlib.encryption.Encoder.generateKeyPair(go, params, random);
    }

    public static KeyPair generateKeyPair(KeyPairGenerator go, int keysize, SecureRandom random) {
        return cn.mcres.karlatemp.mxlib.encryption.Encoder.generateKeyPair(go, keysize, random);
    }

    public static KeyPair generateKeyPair(KeyPairGenerator go, AlgorithmParameterSpec params) {
        return cn.mcres.karlatemp.mxlib.encryption.Encoder.generateKeyPair(go, params);
    }

    public static KeyPairGenerator getKeyPairGenerator(String name) {
        return cn.mcres.karlatemp.mxlib.encryption.Encoder.getKeyPairGenerator(name);
    }

    public abstract PrivateKey getPrivateKey(byte[] pri);

    public abstract PublicKey getPublicKey(byte[] pub);

    public abstract KeyPairGenerator getKeyPairGenerator();

    public abstract byte[] encode(byte[] data, PublicKey pk);

    public abstract byte[] decode(byte[] data, PrivateKey pk);

    public abstract byte[] encode(byte[] data, PrivateKey pk);

    public abstract byte[] decode(byte[] data, PublicKey pk);
}

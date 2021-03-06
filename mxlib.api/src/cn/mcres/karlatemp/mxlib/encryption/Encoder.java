/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Encoder.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.karlatemp.mxlib.encryption;

import cn.mcres.karlatemp.mxlib.exceptions.EncryptionException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public abstract class Encoder {

    public static Cipher getCipher(String n) {
        try {
            return Cipher.getInstance(n);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            throw new EncryptionException(ex.getLocalizedMessage(), ex);
        }
    }

    public static KeyPair generateKeyPair(KeyPairGenerator go, int keysize) {
        synchronized (go) {
            go.initialize(keysize);
            return go.generateKeyPair();
        }
    }

    public static KeyFactory getKeyFactory(String algorithm) {
        try {
            return KeyFactory.getInstance(algorithm);
        } catch (NoSuchAlgorithmException ex) {
            throw new EncryptionException(ex.getLocalizedMessage(), ex);
        }
    }
    public static PrivateKey getPrivateKey(KeyFactory kf, byte[] pri) {
        try {
            return kf.generatePrivate(new PKCS8EncodedKeySpec(pri));
        } catch (InvalidKeySpecException ex) {
            throw new EncryptionException(ex.getLocalizedMessage(), ex);
        }
    }

    public static PublicKey getPublicKey(KeyFactory kf, byte[] public_key) {
        try {
            return kf.generatePublic(new X509EncodedKeySpec(public_key));
        } catch (InvalidKeySpecException ex) {
            throw new EncryptionException(ex.getLocalizedMessage(), ex);
        }
    }

    public static KeyPair generateKeyPair(KeyPairGenerator go, AlgorithmParameterSpec params, SecureRandom random) {
        synchronized (go) {
            try {
                go.initialize(params, random);
                return go.generateKeyPair();
            } catch (InvalidAlgorithmParameterException ex) {
                throw new EncryptionException(ex.getLocalizedMessage(), ex);
            }
        }
    }

    public static KeyPair generateKeyPair(KeyPairGenerator go, int keysize, SecureRandom random) {
        synchronized (go) {
            go.initialize(keysize, random);
            return go.generateKeyPair();
        }
    }

    public static KeyPair generateKeyPair(KeyPairGenerator go, AlgorithmParameterSpec params) {
        synchronized (go) {
            try {
                go.initialize(params);
                return go.generateKeyPair();
            } catch (InvalidAlgorithmParameterException ex) {
                throw new EncryptionException(ex.getLocalizedMessage(), ex);
            }
        }
    }
    public static KeyPairGenerator getKeyPairGenerator(String name) {
        try {
            return KeyPairGenerator.getInstance(name);
        } catch (NoSuchAlgorithmException ex) {
            throw new EncryptionException(ex.getLocalizedMessage(), ex);
        }
    }
    public abstract PrivateKey getPrivateKey(byte[] pri);
    public abstract PublicKey getPublicKey(byte[] pub);
    public abstract KeyPairGenerator getKeyPairGenerator();

    public abstract byte[] encode(byte[] data, PublicKey pk);

    public abstract byte[] decode(byte[] data, PrivateKey pk);

    public abstract byte[] encode(byte[] data, PrivateKey pk);

    public abstract byte[] decode(byte[] data, PublicKey pk);
}

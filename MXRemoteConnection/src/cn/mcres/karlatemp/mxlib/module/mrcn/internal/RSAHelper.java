/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: RSAHelper.java@author: karlatemp@vip.qq.com: 19-12-17 下午11:14@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.mrcn.internal;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAHelper {
    public static final String KEY_ALGORITHM_AES = "RSA";
    public static final String KEY_ALGORITHM_AES_PCKS1PADDING = "RSA/ECB/PKCS1PADDING";
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    private static final int MAX_ENCRYPT_BLOCK = 117;
    private static final int MAX_DECRYPT_BLOCK = 128;

    public static KeyPair genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM_AES);
        keyPairGen.initialize(1024);
        return keyPairGen.generateKeyPair();
    }

    public static PublicKey initPublicKey(byte[] publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return KeyFactory.getInstance(KEY_ALGORITHM_AES).generatePublic(new X509EncodedKeySpec(publicKey));
    }

    public static Cipher initCipher(int opmode, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_AES_PCKS1PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher;
    }

    public static byte[] encrypt(byte[] data, Cipher cipher) throws Exception {
        return doFinal(cipher, data, data.length, true);
    }

    public static byte[] decrypt(byte[] data, Cipher cipher) throws Exception {
        return doFinal(cipher, data, data.length, false);
    }

    public static byte[] sign(byte[] data, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }

    public static boolean verify(byte[] data, PublicKey publicKey, byte[] sign) throws Exception {
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicKey);
        signature.update(data);
        return signature.verify(sign);
    }

    private static byte[] doFinal(Cipher cipher, byte[] data, int inputLen, boolean isEncryptMode) throws Exception {
        int maxBlockSize = isEncryptMode ? MAX_ENCRYPT_BLOCK : MAX_DECRYPT_BLOCK;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > maxBlockSize) {
                cache = cipher.doFinal(data, offSet, maxBlockSize);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * maxBlockSize;
        }
        byte[] result = out.toByteArray();
        out.close();
        return result;
    }
}

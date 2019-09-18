/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: RSAEncoder.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.encryption;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

public class RSAEncoder extends Encoder {

    public static final String AL = "RSA";
    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize) {
        int maxBlock = 0;
        if (opmode == Cipher.DECRYPT_MODE) {
            maxBlock = keySize / 8;
        } else {
            maxBlock = keySize / 8 - 11;
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            int offSet = 0;
            byte[] buff;
            int i = 0;
            while (datas.length > offSet) {
                if (datas.length - offSet > maxBlock) {
                    buff = cipher.doFinal(datas, offSet, maxBlock);
                } else {
                    buff = cipher.doFinal(datas, offSet, datas.length - offSet);
                }
                out.write(buff, 0, buff.length);
                i++;
                offSet = i * maxBlock;
            }
            return out.toByteArray();
        } catch (IOException | IllegalBlockSizeException | BadPaddingException ex) {
            throw new EncryptionException(ex.getLocalizedMessage(), ex);
        }
    }

    @Override
    public byte[] encode(byte[] data, PublicKey pk) {
        try {
            Cipher cipher = getCipher(AL);
            cipher.init(Cipher.ENCRYPT_MODE, pk);
            return rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data, ((RSAKey) pk).getModulus().bitLength());
        } catch (InvalidKeyException e) {
            throw new EncryptionException(e.getLocalizedMessage(),e);
        }
    }

    @Override
    public byte[] decode(byte[] data, PrivateKey pk) {
        try {
            Cipher cipher = getCipher(AL);
            cipher.init(Cipher.DECRYPT_MODE, pk);
            return (rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, (data), ((RSAKey) pk).getModulus().bitLength()));
        } catch (InvalidKeyException e) {
            throw new EncryptionException(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public byte[] encode(byte[] data, PrivateKey pk) {
        try {
            Cipher cipher = getCipher(AL);
            cipher.init(Cipher.ENCRYPT_MODE, pk);
            return rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data, ((RSAKey) pk).getModulus().bitLength());
        } catch (InvalidKeyException e) {
            throw new EncryptionException(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public byte[] decode(byte[] data, PublicKey pk) {
        try {
            Cipher cipher = getCipher(AL);
            cipher.init(Cipher.DECRYPT_MODE, pk);
            return (rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, data, ((RSAKey) pk).getModulus().bitLength()));
        } catch (InvalidKeyException ex) {
            throw new EncryptionException(ex.getLocalizedMessage(), ex);
        }

    }

    @Override
    public KeyPairGenerator getKeyPairGenerator() {
        return Encoder.getKeyPairGenerator(AL);
    }

    @Override
    public RSAPrivateKey getPrivateKey(byte[] pri) {
        return (RSAPrivateKey) Encoder.getPrivateKey(Encoder.getKeyFactory(AL), pri);
    }

    @Override
    public RSAPublicKey getPublicKey(byte[] pub) {
        return (RSAPublicKey) Encoder.getPublicKey(Encoder.getKeyFactory(AL), pub);
    }

}

/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: HashEncryptor.java@author: karlatemp@vip.qq.com: 19-10-14 下午12:30@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.encrypt;

/**
 * 散列加密
 */
public interface HashEncryptor extends Encryptor {
    @Override
    default boolean isSupportDecoder() {
        return false;
    }

    @Override
    default Decoder getDecoder() {
        throw new UnsupportedOperationException();
    }
}

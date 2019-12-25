/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: EncryptionException.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.encryption;

/**
 *
 * @author 32798
 */
public class EncryptionException extends cn.mcres.karlatemp.mxlib.exceptions.EncryptionException {

    public EncryptionException(Throwable cause) {
        super(cause);
    }

    public EncryptionException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Creates a new instance of <code>EncryptionException</code> without detail
     * message.
     */
    public EncryptionException() {
    }

    /**
     * Constructs an instance of <code>EncryptionException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public EncryptionException(String msg) {
        super(msg);
    }
}

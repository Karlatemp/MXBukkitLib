/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: DecodeException.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.encode;

/**
 *
 * @author 32798
 */
public class DecodeException extends cn.mcres.karlatemp.mxlib.exceptions.DecodeException {

    public DecodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public DecodeException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new instance of <code>ActuatorException</code> without detail
     * message.
     */
    public DecodeException() {
    }

    /**
     * Constructs an instance of <code>ActuatorException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DecodeException(String msg) {
        super(msg);
    }
}

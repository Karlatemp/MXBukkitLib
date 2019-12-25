/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: EncodeException.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
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
public class EncodeException extends cn.mcres.karlatemp.mxlib.exceptions.EncodeException {

    public EncodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public EncodeException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new instance of <code>ActuatorException</code> without detail
     * message.
     */
    public EncodeException() {
    }

    /**
     * Constructs an instance of <code>ActuatorException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public EncodeException(String msg) {
        super(msg);
    }
}

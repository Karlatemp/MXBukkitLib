/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: SysRuntimeException.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib;

/**
 *
 * @author 32798
 */
public class SysRuntimeException extends RuntimeException {

    /**
     * Creates a new instance of <code>RuntimeException</code> without detail
     * message.
     */
    public SysRuntimeException() {
    }
    
    public SysRuntimeException(Throwable thr) {
        super(thr);
    }
    

    public SysRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs an instance of <code>RuntimeException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public SysRuntimeException(String msg) {
        super(msg);
    }
}

/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: CompeteException.java@author: karlatemp@vip.qq.com: 19-9-18 下午9:55@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.exceptions;

/**
 * @see cn.mcres.karlatemp.mxlib.tools.IEvalProcessor
 */
public class CompeteException extends Exception {
    public CompeteException() {
    }

    public CompeteException(String message) {
        super(message);
    }

    public CompeteException(String message, Throwable cause) {
        super(message, cause);
    }

    public CompeteException(Throwable cause) {
        super(cause);
    }
}

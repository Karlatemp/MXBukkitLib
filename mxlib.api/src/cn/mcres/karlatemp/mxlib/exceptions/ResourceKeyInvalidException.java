/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ResourceKeyInvalidException.java@author: karlatemp@vip.qq.com: 19-11-16 下午6:39@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.exceptions;

public class ResourceKeyInvalidException extends RuntimeException {
    public ResourceKeyInvalidException() {
        super();
    }

    public ResourceKeyInvalidException(String message) {
        super(message);
    }

    public ResourceKeyInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceKeyInvalidException(Throwable cause) {
        super(cause);
    }

    protected ResourceKeyInvalidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

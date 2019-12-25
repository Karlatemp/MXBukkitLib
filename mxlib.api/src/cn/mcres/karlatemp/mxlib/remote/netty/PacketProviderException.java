/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PacketProviderException.java@author: karlatemp@vip.qq.com: 19-11-28 下午11:43@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.remote.netty;

public class PacketProviderException extends RuntimeException {
    public PacketProviderException() {
    }

    public PacketProviderException(String message) {
        super(message);
    }

    public PacketProviderException(String message, Throwable cause) {
        super(message, cause);
    }

    public PacketProviderException(Throwable cause) {
        super(cause);
    }

    protected PacketProviderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

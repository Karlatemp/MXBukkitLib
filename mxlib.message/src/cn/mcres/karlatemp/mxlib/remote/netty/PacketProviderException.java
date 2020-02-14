/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 24:50:41
 *
 * MXLib/mxlib.message/PacketProviderException.java
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

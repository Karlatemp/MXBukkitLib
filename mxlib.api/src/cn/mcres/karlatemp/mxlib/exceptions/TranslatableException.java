/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: TranslatableException.java@author: karlatemp@vip.qq.com: 2020/1/30 下午7:44@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.exceptions;

import cn.mcres.karlatemp.mxlib.util.TranslatableMessage;

public class TranslatableException extends RuntimeException {
    private final TranslatableMessage message;

    public TranslatableException(String message, Object... params) {
        super(message);
        this.message = new TranslatableMessage(message, params);
    }

    public TranslatableMessage getTranslatableMessage() {
        return message;
    }

    public TranslatableException(String message, Throwable cause, Object... params) {
        super(message, cause);
        this.message = new TranslatableMessage(message);
    }

    public String getLocalizedMessage() {
        return message.getMessage();
    }
}

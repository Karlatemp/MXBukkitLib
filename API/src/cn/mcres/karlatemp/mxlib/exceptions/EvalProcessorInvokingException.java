/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: EvalProcessorInvokingException.java@author: karlatemp@vip.qq.com: 19-10-19 下午7:16@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.exceptions;

/**
 * IEvalProcessor执行错误
 *
 * @see cn.mcres.karlatemp.mxlib.tools.IEvalProcessor
 * @since 2.4
 */
public class EvalProcessorInvokingException extends RuntimeException {
    public EvalProcessorInvokingException() {
        super();
    }

    public EvalProcessorInvokingException(String message) {
        super(message);
    }

    public EvalProcessorInvokingException(String message, Throwable cause) {
        super(message, cause);
    }

    public EvalProcessorInvokingException(Throwable cause) {
        super(cause);
    }

    protected EvalProcessorInvokingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

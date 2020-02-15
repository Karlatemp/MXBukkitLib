/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 01:08:02
 *
 * MXLib/mxlib.message/TypeNotMatchException.java
 */

package cn.mcres.karlatemp.mxlib.codec;

import java.io.IOException;

public class TypeNotMatchException extends IOException {
    public TypeNotMatchException() {
    }

    public TypeNotMatchException(String message) {
        super(message);
    }

    public TypeNotMatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public TypeNotMatchException(Throwable cause) {
        super(cause);
    }
}

/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: HttpHandlerAction.java@author: karlatemp@vip.qq.com: 19-12-21 下午8:33@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.network;

/**
 * Specify what should be done next
 *
 * @since 2.9
 */
public class HttpHandlerAction {
    private final Type type;
    private final boolean rd;
    public static final HttpHandlerAction
            CONTINUE = new HttpHandlerAction(Type.CONTINUE, false),
            RESET_DEPTH = new HttpHandlerAction(Type.RESET_DEPTH, true),
            CANCEL = new HttpHandlerAction(Type.CANCEL, false),
            END = new HttpHandlerAction(Type.END, false),
            SKIP_CONTENT = new HttpHandlerAction(Type.SKIN_CONTENT, false);

    public static HttpHandlerAction redirect(String uri) {
        return new HttpHandlerAction(Type.REDIRECT, false) {
            @Override
            public boolean doRedirect() {
                return true;
            }

            @Override
            public String getRedirect() {
                return uri;
            }
        };
    }

    public static HttpHandlerAction redirectAndReset(String uri) {
        return new HttpHandlerAction(Type.REDIRECT_AND_RESET_DEPTH, true) {
            @Override
            public boolean doRedirect() {
                return true;
            }

            @Override
            public String getRedirect() {
                return uri;
            }
        };
    }


    public enum Type {
        REDIRECT, REDIRECT_AND_RESET_DEPTH, CANCEL, CONTINUE, RESET_DEPTH, END, SKIN_CONTENT;
    }

    public Type getType() {
        return type;
    }

    public boolean doRedirect() {
        return false;
    }

    public String getRedirect() {
        throw new UnsupportedOperationException();
    }

    public boolean resetDepth() {
        return rd;
    }

    private HttpHandlerAction(Type type, boolean reset) {
        this.type = type;
        this.rd = reset;
    }
}

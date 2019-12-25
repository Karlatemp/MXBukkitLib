/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: HttpCallback.java@author: karlatemp@vip.qq.com: 19-12-21 下午6:46@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.network;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpResponse;

import java.net.URI;

/**
 * The callback for http connection.
 *
 * @since 2.9
 */
public interface HttpCallback {
    /**
     * Call when http connection finish.
     *
     * @param uri          The connect uri.
     * @param responseCode The response.
     * @param buf          Http context
     * @param error        The error if connect failed.
     */
    void done(URI uri, HttpResponse responseCode, ByteBuf buf, Throwable error);

    /**
     * Specify what should be done next
     *
     * @param uri      Current URI
     * @param response The response in current connect. Use it to get headers.
     * @return <pre>
     *      null if undo anything.
     *      {@link HttpHandlerAction#CONTINUE} real full context. Even if this is an error page.
     *      {@link HttpHandlerAction#RESET_DEPTH} Like *null*. and it will reset depth times.
     *      {@link HttpHandlerAction#CANCEL} Cancel this connection.(call {@link #done(URI, HttpResponse, ByteBuf, Throwable)} with a error.)
     *      {@link HttpHandlerAction#END} call {@link #done(URI, HttpResponse, ByteBuf, Throwable)} with empty context.
     *      {@link HttpHandlerAction#redirect(String)} Redirect to giving url.
     *      {@link HttpHandlerAction#redirectAndReset(String)} Redirect to giving url and reset depth times.
     * </pre>
     */
    HttpHandlerAction preview(URI uri, HttpResponse response);
}

/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: HttpHandler.java@author: karlatemp@vip.qq.com: 19-12-21 下午7:29@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.net.URI;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

/**
 * The Main Handler for HttpConnection
 *
 * @since 2.9
 */
public class HttpHandler extends SimpleChannelInboundHandler<HttpObject> {
    private final HttpCallback callback;
    private final ConcurrentLinkedDeque<ByteBuf> buffers = new ConcurrentLinkedDeque<>();
    private final URI uri;
    private int depth;
    private final int max;
    private final int ct;
    private final long rt;
    private final TimeUnit rtu;
    private HttpResponse response;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        closed = true;
        ctx.channel().close().addListener((ChannelFutureListener) (a) -> {
        });
        callback.done(uri, response, null, cause);
    }

    public HttpHandler(HttpCallback callback, int depth, int max, int ct, long rt, TimeUnit rtu, URI uri) {
        this.depth = depth;
        this.max = max;
        this.callback = callback;
        this.ct = ct;
        this.rt = rt;
        this.rtu = rtu;
        this.uri = uri;
    }

    private void done(ChannelHandlerContext context) {
        closed = true;
        context.channel().close().addListener((ChannelFutureListener) (a) -> {
        });
        ByteBuf buf;
        if (buffers.isEmpty()) buf = Unpooled.EMPTY_BUFFER;
        else buf = Unpooled.wrappedBuffer(buffers.toArray(new ByteBuf[0]));
        callback.done(uri, response, buf, null);
    }

    private boolean closed = false;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (closed) return;
        if (msg instanceof HttpResponse) {

            HttpResponse response = (HttpResponse) msg;

            final HttpHandlerAction preview = callback.preview(uri, response);
            boolean ct = false;
            if (preview != null) {
                switch (preview.getType()) {
                    case END:
                        closed = true;
                        done(ctx);
                        return;
                    case CONTINUE:
                        ct = true;
                        break;
                    case CANCEL:
                        throw new IllegalAccessException("Connection cancelled.");
                    case REDIRECT_AND_RESET_DEPTH:
                        closed = true;
                        ctx.channel().close();
                        HttpClient.get(preview.getRedirect(), ctx.channel().eventLoop(), this.ct, rt, rtu, callback, 0, max);
                        return;
                    case REDIRECT:
                        closed = true;
                        ctx.channel().close();
                        HttpClient.get(preview.getRedirect(), ctx.channel().eventLoop(), this.ct, rt, rtu, callback, depth + 1, max);
                        return;
                    case RESET_DEPTH:
                        depth = 0;
                        break;
                    case SKIN_CONTENT:
                        done(ctx);
                        return;
                    default:
                        throw new IllegalAccessException("Unknown Type of " + preview.getType());
                }
            }

            int responseCode = response.status().code();
            this.response = response;
            if (responseCode == HttpResponseStatus.NO_CONTENT.code()) {
                done(ctx);
                return;
            }
            if (responseCode == HttpResponseStatus.MOVED_PERMANENTLY.code() || responseCode == HttpResponseStatus.FOUND.code()) {
                final String location = response.headers().get("Location");
                if (location == null) {
                    throw new IllegalAccessException("Got HTTP response " + response.status() + ", absent location.");
                }
                closed = true;
                ctx.channel().close();
                HttpClient.get(location, ctx.channel().eventLoop(), this.ct, rt, rtu, callback, depth + 1, max);
                return;
            }
            if (responseCode != HttpResponseStatus.OK.code() && !ct) {
                throw new IllegalStateException("Expected HTTP response 200 OK, got " + response.status());
            }
        }
        if (msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;
            buffers.add(Unpooled.copiedBuffer(content.content()));
            if (msg instanceof io.netty.handler.codec.http.LastHttpContent) {
                done(ctx);
            }
        }
    }
}

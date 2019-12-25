/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: NettyHelper.java@author: karlatemp@vip.qq.com: 19-12-22 下午2:29@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.network;

import io.netty.channel.*;
import io.netty.util.AttributeKey;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @since 2.9
 */
public class NettyHelper {
    public interface MessageCallback<T> {
        void done(T message, Throwable error);
    }

    private static class Hooker extends ChannelInboundHandlerAdapter {
        private final AtomicReference<Object> notify = new AtomicReference<>();
        private final ConcurrentLinkedDeque<MessageCallback> readers = new ConcurrentLinkedDeque<>();

        @SuppressWarnings("unchecked")
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            synchronized (readers) {
                ArrayList<MessageCallback> a = new ArrayList<>(readers);
                readers.clear();
                for (MessageCallback cb : a) {
                    cb.done(msg, null);
                }
            }
            synchronized (notify) {
                notify.set(msg);
                notify.notify();
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            boolean a = true;
            synchronized (readers) {
                ArrayList<MessageCallback> aw = new ArrayList<>(readers);
                readers.clear();
                for (MessageCallback cb : aw) {
                    cb.done(null, cause);
                    a = false;
                }
            }
            synchronized (notify) {
                notify.notify();
            }
            if (a) {
                ctx.fireExceptionCaught(cause);
            }
        }
    }

    private static final AttributeKey<Hooker> hooker = AttributeKey.valueOf("MXLib-NettyHelper-Hooker");

    public static void sendMessage(@NotNull Channel channel, Object msg, ChannelFutureListener listener) {
        final ChannelFuture future = channel.writeAndFlush(msg);
        if (listener != null) future.addListener(listener);
        future.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }

    public static ChannelInboundHandlerAdapter createHooker(Channel channel) {
        if (channel.hasAttr(hooker)) {
            throw new IllegalArgumentException("Channel " + channel + " registered a hooker.");
        }
        Hooker h = new Hooker();
        channel.attr(hooker).set(h);
        return h;
    }

    public static <T> void readMessage(Channel channel, MessageCallback<T> callback) {
        if (channel.isOpen() && channel.isActive()) {
            if (!channel.hasAttr(hooker)) {
                callback.done(null, new IllegalArgumentException("Channel " + channel + " has no hooker."));
            }
            final Hooker hooker = channel.attr(NettyHelper.hooker).get();
            if (hooker == null)
                callback.done(null, new IllegalArgumentException("Where is " + channel + "'s hooker?"));
            synchronized (hooker.readers) {
                hooker.readers.add(callback);
            }
        } else {
            callback.done(null, new RuntimeException("Channel not active."));
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T readMessage(Channel channel, long time, boolean error) {
        if (channel.isOpen() && channel.isActive()) {
            if (!channel.hasAttr(hooker)) {
                throw new IllegalArgumentException("Channel " + channel + " has no hooker.");
            }
            final Hooker hooker = channel.attr(NettyHelper.hooker).get();
            if (hooker == null)
                throw new IllegalArgumentException("Where is " + channel + "'s hooker?");
            final AtomicReference<Object> reference = hooker.notify;
            synchronized (reference) {
                try {
                    Object result = reference.get();
                    if (result != null) {
                        return (T) result;
                    }
                    try {
                        reference.wait(time);
                    } catch (InterruptedException err) {
                        if (error) {
                            throw new RuntimeException(err);
                        }
                        return null;
                    }
                    result = reference.get();
                    if (result == null && error) {
                        throw new RuntimeException(new TimeoutException());
                    }
                    return (T) result;
                } finally {
                    reference.set(null);
                }
            }
        }
        if (error) throw new RuntimeException("Channel closed.");
        return null;
    }
}

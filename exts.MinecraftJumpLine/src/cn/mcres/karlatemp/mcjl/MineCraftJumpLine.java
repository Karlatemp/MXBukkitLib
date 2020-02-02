/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: MineCraftJumpLine.java@author: karlatemp@vip.qq.com: 2020/1/14 上午2:22@version: 2.0
 */

package cn.mcres.karlatemp.mcjl;

import cn.mcres.karlatemp.mxlib.network.IPAddress;
import cn.mcres.karlatemp.mxlib.network.NettyHelper;
import cn.mcres.karlatemp.mxlib.network.PipelineUtils;
import cn.mcres.karlatemp.mxlib.network.minecraft.MinecraftProtocolHelper;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

public class MineCraftJumpLine {
    public static int port = Integer.getInteger("port", 25565);
    public static String host = System.getProperty("host", "play.39mc.top");

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("log4j2.loggerContextFactory", "org.apache.logging.log4j.simple.SimpleLoggerContextFactory");
        var gp = PipelineUtils.newEventLoopGroup();
        new ServerBootstrap().channel(PipelineUtils.getServerChannel()).childHandler(new ChannelInitializer<>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                var list = new ConcurrentLinkedQueue<>();
                var remote = new AtomicReference<Channel>();
                ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        final Channel channel = remote.get();
                        if (channel != null) {
                            if (channel.isOpen() && channel.isActive()) {
                                if (!list.isEmpty()) {
                                    for (Object o : list) {
                                        channel.writeAndFlush(msg).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                                    }
                                    list.clear();
                                }
                                channel.writeAndFlush(msg).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                                return;
                            }
                        }
                        if (msg instanceof ByteBuf) {
                            msg = Unpooled.copiedBuffer((ByteBuf) msg);
                        }
                        list.add(msg);
                    }

                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                        cause.printStackTrace();
                        ctx.channel().close();
                    }
                });
                final IPAddress address = MinecraftProtocolHelper.parseMinecraftServerAddress(host);
                remote.set(new Bootstrap().group(gp)
                        .channel(PipelineUtils.getChannel())
                        .handler(new ChannelInitializer<>() {
                            @Override
                            protected void initChannel(Channel rm) throws Exception {
                                if (ch.isActive() && ch.isOpen()) {
                                    rm.closeFuture().addListener(c -> ch.close());
                                    ch.closeFuture().addListener(c -> rm.close());
                                    rm.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                        @Override
                                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                            ch.writeAndFlush(msg).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                                        }

                                        @Override
                                        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                            cause.printStackTrace();
                                            ctx.channel().close();
                                        }
                                    });
                                } else {
                                    rm.close();
                                }
                            }
                        })
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                        .connect(address.getHost(), address.getPort())
                        .sync().channel());
            }
        }).group(gp, PipelineUtils.newEventLoopGroup()).bind(port).sync();
    }
}

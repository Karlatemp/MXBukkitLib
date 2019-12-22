/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Tester.java@author: karlatemp@vip.qq.com: 19-12-16 下午11:48@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.mrcn;

import cn.mcres.karlatemp.mxlib.module.mrcn.packet.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Tester {
    public static void main(String[] args) throws Throwable {
        System.setProperty("log4j2.loggerContextFactory", "org.apache.logging.log4j.simple.SimpleLoggerContextFactory");

        Thread.currentThread().setUncaughtExceptionHandler((a, b) -> {
            b.printStackTrace();
            System.exit(-1);
        });
        ServerWorker server = new ServerWorker() {
            @Override
            public void initAuthResponse(PacketLoginAuthResponse response) {
                response.bukkit_name = "FUCKQ";
                response.server_name = "HUM";
                response.allow_set_permission = false;
                response.is_op = true;
                response.server_version = "1.12.2";
                response.bukkit_version = "v1_13_R1";
            }

            @Override
            public boolean preLoginOk(Channel client, PacketLoginPreLogin packet) {
                System.out.println("S: Connect passwd: " + packet.passwd);
                return true;
            }

            @Override
            public ServerWorker auth(PacketLoginAuth auth) {
                System.out.println("S: Authing " + auth.user + "@" + new String(auth.passwd));
                return this;
            }

            @Override
            public void listen(NetWorkManager manager, PacketSystemInvokeCommand packet) throws Exception {
                System.out.println("S: Invoke Command: " + packet.command);
                manager.writePacket(new PacketSystemMessage("Command: " + packet.command));
            }
        };
        System.out.println("S1");
        new ServerBootstrap().channel(NioServerSocketChannel.class)
                .group(new NioEventLoopGroup(1), new NioEventLoopGroup())
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        System.out.println("S: Client Connected.");
                        MXRemoteConnectionUtil.openServer(ch, server);
                    }
                }).bind(25567).sync();
        System.out.println("S2");
        ClientWorker client = new ClientWorker() {
            @Override
            public void login_disconnect(String msg) throws Exception {
                System.out.println("C: Disconnect " + msg);
            }

            @Override
            public void login(PacketLoginAuth auth) {
                auth.user = "Karlatmep";
                auth.passwd = "TestPasswd".getBytes();
            }

            @Override
            public void listen(NetWorkManager manager, PacketSystemMessage packet) throws Exception {
                System.out.println("C: M: " + packet.message);
            }

            @Override
            public void doneLogin(NetWorkManager manager) {
                System.out.println("C: Login Done");
                manager.writePacket(new PacketSystemInvokeCommand("HelloWorld"));
                manager.writePacket(new PacketSystemInvokeCommand("Shit"));
                manager.writePacket(new PacketSystemInvokeCommand("What The FUCK"));
                manager.writePacket(new PacketSystemInvokeCommand("ARE YOU OK"));
            }
        };
        System.out.println("S3");
        Channel ch = new Bootstrap().channel(NioSocketChannel.class)
                .group(new NioEventLoopGroup())
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                    }
                }).connect("localhost", 25567).sync().channel();
        System.out.println("Post connect.");
        MXRemoteConnectionUtil.openClient(ch, "localhost", "PrePasswd", 7774,
                client);
//        System.exit(0);
    }
}

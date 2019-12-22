/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MXRemoteConnectionUtil.java@author: karlatemp@vip.qq.com: 19-12-16 下午10:17@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.mrcn;


import cn.mcres.karlatemp.mxlib.module.mrcn.internal.*;
import cn.mcres.karlatemp.mxlib.module.mrcn.packet.*;
import cn.mcres.karlatemp.mxlib.remote.netty.NettyPacketDecoder;
import cn.mcres.karlatemp.mxlib.remote.netty.NettyPacketEncoder;
import cn.mcres.karlatemp.mxlib.remote.netty.PacketProtocolProvider;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.jetbrains.annotations.NotNull;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("Duplicates")
public class MXRemoteConnectionUtil {
    public static void init(@NotNull Channel channel, AtomicReference<PacketProtocolProvider> provider) {
        final PacketProtocolProvider provider_ = EnumProtocol.provider(channel);
        provider_.setProtocol(null, EnumProtocol.LOGIN);
        channel.pipeline()
                .addLast("serializer", new DataSerializer())
                .addLast("encoder", new NettyPacketEncoder(provider_))
                .addLast("deserializer", new DataDeserializer())
                .addLast("decoder", new NettyPacketDecoder(provider_));
        if (provider != null) provider.set(provider_);
    }

    private static void a(boolean w, String sw) {
        if (!w) throw new RuntimeException(sw);
    }

    public static enum LoginStatus {
        PRE_LOGIN, ENCRYPTING, AUTH, AUTHING;
    }

    public static void openClient(
            @NotNull final Channel connect,
            @NotNull String host, @NotNull String passwd, int port,
            @NotNull ClientWorker worker) {
        final PacketProtocolProvider provider;
        {
            AtomicReference<PacketProtocolProvider> providerReference = new AtomicReference<>();
            init(connect, providerReference);
            provider = providerReference.get();
        }
        NetWorkManager manager = new NetWorkManager(connect);
        manager.handler = new PacketListener() {
            @Override
            public String getName() {
                return "Client";
            }

            @Override
            public void listen(NetWorkManager manager, PacketLoginDisconnect packet) throws Exception {
                worker.login_disconnect(packet.reason);
                manager.disconnect(null);

            }

            @Override
            public void listen(NetWorkManager manager, PacketLoginAuthResponse packet) throws Exception {
                boolean allow_set_permission = packet.allow_set_permission;
                worker.authResponse(packet);
                if (allow_set_permission) {
                    provider.setProtocol(null, EnumProtocol.PERMISSION_INIT);
                    worker.doPermOverride(manager);
                    manager.writePacket(new PacketPermissionDone());
                }
                provider.setProtocol(null, EnumProtocol.SYSTEM);
                worker.doneLogin(manager);
                manager.handler = worker;
            }

            @Override
            public void listen(NetWorkManager manager, PacketLoginEncrypt packet) throws Exception {
                // System.out.println("CR: Pub: " + Base64.getEncoder().encodeToString(packet.rsa_public));

                RSAPublicKey pubKey = (RSAPublicKey) RSAHelper.initPublicKey(packet.rsa_public);
                {
                    Cipher cipher = Cipher.getInstance("RSA");
                    cipher.init(Cipher.ENCRYPT_MODE, pubKey);
                    connect.pipeline().addAfter("serializer", "encrypter", new DataEncrypter(cipher));
                }
                {
                    Cipher cipher = Cipher.getInstance("RSA");
                    cipher.init(Cipher.DECRYPT_MODE, pubKey);
                    connect.pipeline().addAfter("deserializer", "decrypter", new DataDecrypter(cipher));
                }
                PacketLoginAuth auth = new PacketLoginAuth();
                worker.login(auth);
                manager.writePacket(auth);
            }
        };
        PacketLoginPreLogin pre = new PacketLoginPreLogin();
        pre.host = host;
        pre.port = port;
        pre.passwd = passwd;
        manager.writePacket(pre);
        connect.pipeline().addLast("handler", manager);
    }

    public static void openServer(@NotNull final Channel client, @NotNull final ServerWorker worker) {
        final PacketProtocolProvider provider;
        {
            AtomicReference<PacketProtocolProvider> providerReference = new AtomicReference<>();
            init(client, providerReference);
            provider = providerReference.get();
        }
        NetWorkManager mgr = new NetWorkManager(client);
        AtomicReference<ServerWorker> work = new AtomicReference<>();
        mgr.handler = new PacketListener() {
            LoginStatus ls = LoginStatus.PRE_LOGIN;

            @Override
            public String getName() {
                return "Server";
            }

            @Override
            public void exceptionCaught(NetWorkManager netWorkManager, ChannelHandlerContext ctx, Throwable cause) throws Exception {
                mgr.writePacket(new PacketLoginDisconnect(String.valueOf(cause)));
            }

            @Override
            public void disconnect(NetWorkManager netWorkManager, String s) {
                mgr.writePacket(new PacketLoginDisconnect(s));
            }

            @Override
            public void listen(NetWorkManager manager, PacketLoginAuth packet) throws Exception {
                a(ls == LoginStatus.AUTH, "Error packet type.");
                ls = LoginStatus.AUTHING;

                System.out.println("AUTH PACKET INSERT.");
                ServerWorker authed = worker.auth(packet);
                if (authed == null) {
                    mgr.disconnect("Failed to auth this account.");
                    return;
                }
                work.set(authed);
                PacketLoginAuthResponse response = new PacketLoginAuthResponse();
                authed.initAuthResponse(response);
                mgr.writePacket(response);
                if (response.allow_set_permission) {
                    provider.setProtocol(null, EnumProtocol.PERMISSION_INIT);
                    mgr.handler = new PacketListener() {
                        @Override
                        public void listen(NetWorkManager manager, PacketPermissionDone packet) throws Exception {
                            provider.setProtocol(null, EnumProtocol.SYSTEM);
                            mgr.handler = authed;
                        }

                        @Override
                        public void listen(NetWorkManager manager, PacketPermissionResponse packet) throws Exception {
                            authed.listen(manager, packet);
                        }

                        @Override
                        public void listen(NetWorkManager manager, PacketPermissionOverridePermissions packet) throws Exception {
                            authed.listen(manager, packet);

                        }

                        @Override
                        public void listen(NetWorkManager manager, PacketPermissionSetOp packet) throws Exception {
                            authed.listen(manager, packet);
                        }
                    };
                } else {
                    provider.setProtocol(null, EnumProtocol.SYSTEM);
                    mgr.handler = authed;
                }
            }

            @Override
            public void listen(NetWorkManager manager, PacketLoginPreLogin packet) throws Exception {
                a(ls == LoginStatus.PRE_LOGIN, "Error packet type.");
                if (!worker.preLoginOk(client, packet)) {
                    mgr.disconnect("Failed to pre login.");
                }
                ls = LoginStatus.ENCRYPTING;
                KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
                keyPairGen.initialize(1024, new SecureRandom());
                KeyPair keyPair = keyPairGen.generateKeyPair();
                PacketLoginEncrypt encrypt = new PacketLoginEncrypt();
                encrypt.rsa_public = keyPair.getPublic().getEncoded();
                mgr.writePacket(encrypt);
                // System.out.println("SR: Pub: " + Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
                {
                    Cipher cipher = Cipher.getInstance("RSA");
                    cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPrivate());
                    client.pipeline().addAfter("serializer", "encrypter", new DataEncrypter(cipher));
                }
                {
                    Cipher cipher = Cipher.getInstance("RSA");
                    cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
                    client.pipeline().addAfter("deserializer", "decrypter", new DataDecrypter(cipher));
                }
                ls = LoginStatus.AUTH;
            }
        };
        client.pipeline().addLast("handler", mgr);
    }

    public static Channel timeout(Channel channel, long timeout, TimeUnit unit) {
        channel.pipeline().addFirst("timeout", new ReadTimeoutHandler(timeout, unit));
        return channel;
    }
}

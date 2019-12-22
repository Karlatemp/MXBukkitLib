/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: EnumProtocol.java@author: karlatemp@vip.qq.com: 19-12-16 下午10:18@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.mrcn.packet;

import cn.mcres.karlatemp.mxlib.remote.netty.Packet;
import cn.mcres.karlatemp.mxlib.remote.netty.PacketProtocol;
import cn.mcres.karlatemp.mxlib.remote.netty.PacketProtocolProvider;
import cn.mcres.karlatemp.mxlib.remote.netty.PacketProviderLink;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.jetbrains.annotations.NotNull;


public class EnumProtocol extends PacketProtocol {
    public static final AttributeKey<EnumProtocol> PROTOCOL = AttributeKey.valueOf("MXLib-MXRemoteConnection-EnumProtocol");
    public static final
    EnumProtocol LOGIN = new EnumProtocol("Login")
            .a(PacketLoginPreLogin.class)
            .a(PacketLoginDisconnect.class)
            .a(PacketLoginAuth.class)
            .a(PacketLoginAuthResponse.class)
            .a(PacketLoginEncrypt.class),
            PERMISSION_INIT = new EnumProtocol("Permission init")
                    .a(PacketPermissionSetOp.class)
                    .a(PacketPermissionOverridePermissions.class)
                    .a(PacketPermissionResponse.class)
                    .a(PacketPermissionDone.class),
            SYSTEM = new EnumProtocol("System")
                    .a(PacketSystemDisconnect.class)
                    .a(PacketSystemInvokeCommand.class)
                    .a(PacketSystemChannelMessage.class)
                    .a(PacketSystemKeepAlive.class)
                    .a(PacketSystemMessage.class)
                    .a(PacketSystemMessages.class);
    public final PacketProviderLink link = new PacketProviderLink();

    protected EnumProtocol(String name) {
        super(name);
    }

    private <T extends Packet<T>> EnumProtocol a(Class<T> c) {
        link.register(c);
        return this;
    }

    public static PacketProtocolProvider provider(final Channel impl) {
        return new PacketProtocolProvider() {
            @Override
            public PacketProtocol getProtocol(ChannelHandlerContext context) {
                return impl.attr(PROTOCOL).get();
            }

            @Override
            public void setProtocol(ChannelHandlerContext context, @NotNull PacketProtocol protocol) {
                if (protocol instanceof EnumProtocol) {
                    impl.attr(PROTOCOL).set((EnumProtocol) protocol);
                }
            }
        }.register(LOGIN, LOGIN.link).register(PERMISSION_INIT, PERMISSION_INIT.link).register(SYSTEM, SYSTEM.link);
    }
}

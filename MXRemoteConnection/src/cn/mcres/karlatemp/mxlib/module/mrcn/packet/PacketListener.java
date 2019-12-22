/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PacketListener.java@author: karlatemp@vip.qq.com: 19-12-16 下午10:51@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.mrcn.packet;

import cn.mcres.karlatemp.mxlib.module.mrcn.NetWorkManager;
import cn.mcres.karlatemp.mxlib.remote.netty.Packet;
import cn.mcres.karlatemp.mxlib.tools.ThrowHelper;
import io.netty.channel.ChannelHandlerContext;

public interface PacketListener {
    static void post(PacketListener listener, NetWorkManager manager, Packet<?> packet) throws Exception {
        if (packet instanceof PacketLoginPreLogin) {
            listener.listen(manager, (PacketLoginPreLogin) packet);
        } else if (packet instanceof PacketLoginDisconnect) {
            listener.listen(manager, (PacketLoginDisconnect) packet);
        } else if (packet instanceof PacketLoginAuth) {
            listener.listen(manager, (PacketLoginAuth) packet);
        } else if (packet instanceof PacketLoginAuthResponse) {
            listener.listen(manager, (PacketLoginAuthResponse) packet);
        } else if (packet instanceof PacketLoginEncrypt) {
            listener.listen(manager, (PacketLoginEncrypt) packet);
        } else if (packet instanceof PacketPermissionSetOp) {
            listener.listen(manager, (PacketPermissionSetOp) packet);
        } else if (packet instanceof PacketPermissionOverridePermissions) {
            listener.listen(manager, (PacketPermissionOverridePermissions) packet);
        } else if (packet instanceof PacketPermissionResponse) {
            listener.listen(manager, (PacketPermissionResponse) packet);
        } else if (packet instanceof PacketPermissionDone) {
            listener.listen(manager, (PacketPermissionDone) packet);
        } else if (packet instanceof PacketSystemDisconnect) {
            listener.listen(manager, (PacketSystemDisconnect) packet);
        } else if (packet instanceof PacketSystemInvokeCommand) {
            listener.listen(manager, (PacketSystemInvokeCommand) packet);
        } else if (packet instanceof PacketSystemChannelMessage) {
            listener.listen(manager, (PacketSystemChannelMessage) packet);
        } else if (packet instanceof PacketSystemKeepAlive) {
        } else if (packet instanceof PacketSystemMessage) {
            listener.listen(manager, (PacketSystemMessage) packet);
        } else if (packet instanceof PacketSystemMessages) {
            listener.listen(manager, (PacketSystemMessages) packet);
        } else {
            throw new UnsupportedOperationException("Unknown packet");
        }
    }

    default void exceptionCaught(NetWorkManager netWorkManager, ChannelHandlerContext ctx, Throwable cause) throws Exception {
    }

    default void listen(NetWorkManager manager, PacketLoginAuth packet) throws Exception {
    }

    default void listen(NetWorkManager manager, PacketLoginEncrypt packet) throws Exception {
    }

    default void listen(NetWorkManager manager, PacketLoginAuthResponse packet) throws Exception {
    }

    default void listen(NetWorkManager manager, PacketLoginDisconnect packet) throws Exception {
    }

    default void listen(NetWorkManager manager, PacketLoginPreLogin packet) throws Exception {
    }

    default void listen(NetWorkManager manager, PacketPermissionDone packet) throws Exception {
    }

    default void listen(NetWorkManager manager, PacketPermissionResponse packet) throws Exception {
    }

    default void listen(NetWorkManager manager, PacketPermissionOverridePermissions packet) throws Exception {
    }

    default void listen(NetWorkManager manager, PacketPermissionSetOp packet) throws Exception {
    }

    default void listen(NetWorkManager manager, PacketSystemMessage packet) throws Exception {
    }

    default void listen(NetWorkManager manager, PacketSystemMessages packet) throws Exception {
        PacketSystemMessage msg = new PacketSystemMessage(null);
        Throwable t = null;
        for (String msg0 : packet.messages) {
            msg.message = msg0;
            try {
                listen(manager, msg);
            } catch (Throwable err0) {
                if (t == null) {
                    t = err0;
                } else {
                    t.addSuppressed(err0);
                }
            }
        }
        if (t != null)
            ThrowHelper.thrown(t);
    }

    default void listen(NetWorkManager manager, PacketSystemChannelMessage packet) throws Exception {
    }

    default void listen(NetWorkManager manager, PacketSystemInvokeCommand packet) throws Exception {
    }

    default void listen(NetWorkManager manager, PacketSystemDisconnect packet) throws Exception {
    }

    default void disconnect(NetWorkManager netWorkManager, String s) throws Exception {
    }

    default String getName() {
        return "";
    }
}

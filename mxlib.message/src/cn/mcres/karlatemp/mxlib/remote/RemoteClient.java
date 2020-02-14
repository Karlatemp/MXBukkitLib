/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 24:50:41
 *
 * MXLib/mxlib.message/RemoteClient.java
 */

package cn.mcres.karlatemp.mxlib.remote;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class RemoteClient implements Closeable {

    private RemoteConnection connection;

    public RemoteClient(@NotNull RemoteConnection connection) {
        this.connection = connection;
    }

    public RemoteClient(@NotNull SocketChannel channel) {
        this(new RemoteConnection(channel));
    }

    public RemoteClient(@NotNull String host, int port) throws IOException {
        this(SocketChannel.open(
                new InetSocketAddress(host, port)
        ));
    }

    public RemoteClient() throws IOException {
        this(SocketChannel.open());
    }

    @Contract(pure = true)
    public RemoteConnection getConnection() {
        return connection;
    }

    @Override
    public synchronized void close() throws IOException {
        if (connection == null) return;
        connection.disconnect();
        connection = null;
    }
}

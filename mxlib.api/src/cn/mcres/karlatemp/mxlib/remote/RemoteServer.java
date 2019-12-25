/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: RemoteServer.java@author: karlatemp@vip.qq.com: 19-9-28 下午9:39@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.remote;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.security.KeyPair;
import java.util.Set;

/**
 * Net Work Server.
 */
public class RemoteServer implements Closeable {
    private Selector selector;
    private ServerSocketChannel server;
    private KeyPair rsa;

    /**
     * Create a server with rsa encoded.
     *
     * @param rsa The rsa key.
     * @throws IOException If cannot create a ServerSocketChannel and Selector
     */
    public RemoteServer(@Nullable KeyPair rsa) throws IOException {
        this(ServerSocketChannel.open(), Selector.open(), rsa);
    }

    public RemoteServer() throws IOException {
        this((KeyPair) null);
    }

    public RemoteServer(@NotNull ServerSocketChannel server) throws IOException {
        this(server, Selector.open());
    }

    public RemoteServer(@NotNull ServerSocketChannel server,
                        @NotNull Selector selector) {
        this(server, selector, null);
    }

    /**
     * Create a server.
     *
     * @param server   The Server Socket
     * @param selector The selector using
     * @param rsa      The rsa key. 'null' to disable.
     */
    public RemoteServer(@NotNull ServerSocketChannel server,
                        @NotNull Selector selector,
                        @Nullable KeyPair rsa) {
        this.server = server;
        this.selector = selector;
        this.rsa = rsa;
    }

    /**
     * Close selector and server.
     *
     * @throws IOException Error IN closing selector or server.
     */
    @Override
    public synchronized void close() throws IOException {
        if (selector != null) {
            selector.close();
            selector = null;
        }
        if (server != null) {
            server.close();
            server = null;
        }
    }

    @Contract(pure = true)
    public ServerSocketChannel getServer() {
        return server;
    }

    @Contract(pure = true)
    public Selector getSelector() {
        return selector;
    }

    /**
     * Accept a connect.
     * The reason we set the pure to true is that you want to add the event handler.
     *
     * @return null or the remote connection
     * @throws IOException IO Error
     * @see RemoteConnection
     * @see RemoteConnection#getHandlerList()
     */
    @Nullable
    @Contract(pure = true)
    public RemoteConnection accept() throws IOException {
        final SocketChannel accept = server.accept();
        if (accept != null) {
            accept.configureBlocking(false);
            final SelectionKey key = accept.register(selector, SelectionKey.OP_READ);
            RemoteConnection connection;
            if (rsa != null) {
                AuthenticationRemoteConnection ar;
                key.attach(connection = ar = new AuthenticationRemoteConnection(accept, rsa));
                ar.init();
            } else
                key.attach(connection = new RemoteConnection(accept));
            return connection;
        }
        return null;
    }

    /**
     * Waiting for connected data and processing it
     * <p>
     * Please execute this method in a new thread, or execute it in {@link java.util.concurrent.ExecutorService}
     * <pre>{@code
     * RemoteServer server = new RemoteServer();
     * server.getServer().socket().bind(new InetSocketAddress(23333));
     * new Thread(()->{
     *      while(server.isOpen()){
     *          try{
     *              server.select();
     *          } catch (IOException ignore){}
     *      }
     * }).start();}</pre>
     *
     * @throws IOException IOException
     */
    public void select() throws IOException {
        int s = selector.select();
        if (s > 0) {
            final Set<SelectionKey> keys = selector.selectedKeys();
            for (SelectionKey key : keys) {
                if (key.isReadable()) {
                    Object att = key.attachment();
                    if (att instanceof RemoteConnection) {
                        ((RemoteConnection) att).onSelected();
                    }
                }
            }
            keys.clear();
        }
    }

    public boolean isOpen() {
        if (server == null) return false;
        if (selector == null) return false;
        return server.isOpen();
    }
}

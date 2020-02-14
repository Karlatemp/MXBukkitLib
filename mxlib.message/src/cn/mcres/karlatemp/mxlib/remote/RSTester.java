/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 24:50:41
 *
 * MXLib/mxlib.message/RSTester.java
 */

package cn.mcres.karlatemp.mxlib.remote;

import org.jetbrains.annotations.TestOnly;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.util.Arrays;

@SuppressWarnings("Duplicates")
public class RSTester {
    @TestOnly
    public static void main(String[] main) throws Throwable {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();

        int port;
        ServerSocket socket = new ServerSocket(0);
        port = socket.getLocalPort();
        System.out.println(port);
        socket.close();
        RemoteServer server = new RemoteServer(keyPair);
        server.getServer().socket().bind(new InetSocketAddress(port));
        final Object waitLock = new Object();
        Thread sr = new Thread(() -> {
            try {
                synchronized (waitLock) {
                    waitLock.notify();
                }
                final RemoteConnection accept = server.accept();
                accept.getHandlerList().register(a -> {
                    final ByteBuffer buffer = a.getBuffer();
                    final PrintStream ps = System.out;
                    synchronized (ps) {
                        byte[] wr = new byte[buffer.remaining()];
                        buffer.get(wr);
                        ps.println("[Server] ID=" +
                                a.current_packet + ", len= "
                                + wr.length + ", " + new String(wr) + ", " + Arrays.toString(wr)
                        );
                    }
                    try {
                        a.sendPacket((short) 5, "I Headed It".getBytes());
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                });
                while (server.isOpen()) {
                    server.select();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        synchronized (waitLock) {
            sr.start();
            waitLock.wait();
            Thread.sleep(1000L);
        }
        RemoteClient client = new RemoteClient(
                new AuthenticationRemoteConnection(
                        SocketChannel.open(new InetSocketAddress("localhost", port))
                ).init());
        final RemoteConnection connection = client.getConnection();
        connection.socket.configureBlocking(false);
        connection.socket.socket().setSoTimeout(1000);
        connection.sendPacket((short) 1, "Test".getBytes());
        connection.sendPacket((short) 2, "Test2".getBytes());
        connection.sendPacket((short) 3, "Test3".getBytes());
        connection.sendPacket((short) 4, "Test4".getBytes());
        connection.handlers.register(a -> {
            byte[] b = new byte[a.getBuffer().remaining()];
            a.getBuffer().get(b);
            System.out.println("[Response] ID= " + a.current_packet + ", len=" + +b.length + ", " + new String(b) + "," + Arrays.toString(b));
        });
        Thread.sleep(1000L);// Wait for send all
        int counter = 1000;
        while (counter-- > 0) {
            connection.onSelected();
        }
        Thread.sleep(1000L);
        System.out.println("Dis connect");
        connection.disconnect();
        Thread.sleep(3000L);
        System.exit(0);
    }
}

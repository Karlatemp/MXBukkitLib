/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MconClient.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.mcon;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class MconClient implements Types, Closeable, AutoCloseable {

    private final byte[] pwd;
    private final int port;
    private final String host;
    private SocketChannel net;

    private final ByteBuffer intReader = ByteBuffer.allocate(4);

    private synchronized ByteBuffer read0() throws IOException {
        intReader.position(0);
        int rsize = net.read(intReader);
        if (rsize == -1) {
            net.close();
            return null;
        } else if (rsize != 4) {
            return null;
        }
        intReader.position(0);
        int size = intReader.getInt();
        ByteBuffer bb = ByteBuffer.allocateDirect(size);
        net.read(bb);
        bb.position(0);
        return bb;
    }

    private synchronized void write(ByteBuffer bb) throws IOException {
        intReader.position(0);
        intReader.putInt(bb.remaining());
        intReader.position(0);
        net.write(new ByteBuffer[]{intReader, bb});
    }

    public MconClient(String host, int port, byte[] pwd) {
        this.host = host;
        this.port = port;
        this.pwd = pwd;
    }

    public void connect() throws IOException {
        System.out.println("OPEN CONNECT.");
        this.net = SocketChannel.open(new InetSocketAddress(host, port));
        net.configureBlocking(false);
        send(ByteBuffer.wrap(pwd));
        boolean readed = false;
        while (net.isOpen()) {
            ByteBuffer bb = read();
            if (bb != null) {
                readed = true;
                int code = bb.getInt();
                if (code != 200) {
                    throw new IOException(code + " - " + UTF_8.decode(bb));
                }
                break;
            }
        }
        if (!readed) {
            throw new IOException("Cannot read response from server.");
        }
    }

    public void send(int a, String b) throws IOException {
        if (b == null) {
            intReader.position(0);
            intReader.putInt(a);
            intReader.position(0);
            send(intReader);
            return;
        }
        ByteBuffer bb = UTF_8.encode(b);
        ByteBuffer sd = ByteBuffer.allocateDirect(4 + bb.remaining());
        sd.putInt(a);
        sd.put(bb);
        sd.flip();
        send(sd);
    }

    public void sendCommand(String line) throws IOException {
        send(INVOKE_COMMAND, line);
    }

    public void setOp(boolean b) throws IOException {
        send(SET_OP, b ? "1" : "0");
    }

    public void setName(String name) throws IOException {
        send(SET_NAME, name);
    }

    public void close() throws IOException {
        send(STOP, null);
    }

    public ByteBuffer read() throws IOException {
        if (net.isOpen()) {
            return read0();
        }
        return null;
    }

    public void send(ByteBuffer data) throws IOException {
        if (net.isOpen()) {
            write(data);
        }
    }
}

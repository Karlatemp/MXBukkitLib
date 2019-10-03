/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: RemoteConnection.java@author: karlatemp@vip.qq.com: 19-9-28 下午9:57@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.remote;

import cn.mcres.karlatemp.mxlib.event.Event;
import cn.mcres.karlatemp.mxlib.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * RemoteConnection. As Server Or Client.
 * <p>
 * The reason we break the specification ourselves is because we want each Remote Server/Connection to have its own listening system instead of the global listening system.
 * <p>
 * RemoteConnection will not provide <b>static</b> handlers field, as that may lead to the disclosure of security information, which is not what we want.
 */
public class RemoteConnection extends Event {

    public final SocketChannel socket;
    protected final ByteBuffer buffer;
    public boolean acc = false;
    /**
     * For status with {@link Status#PROCESSING_PACKET}, it is the packet id for {@link #sendPacket(short, ByteBuffer)}
     */
    public short current_packet; // We use to to save the packet.
    public final HandlerList<RemoteConnection> handlers;
    protected Status status = Status.WAIT_FULL_PACKET;

    /**
     * Get the current status for this connection
     *
     * @return Current for this connection
     */
    public Status getStatus() {
        return status;
    }

    public enum Status {
        /**
         * We are processing a packet.
         */
        PROCESSING_PACKET,
        /**
         * We are waiting for the client/server to send the complete packet
         */
        WAIT_FULL_PACKET,
        /**
         * The connection this closed. Dont send more data.
         */
        DISCONNECT;
    }

    @SuppressWarnings("unchecked")
    protected RemoteConnection(
            SocketChannel socket,
            HandlerList handlerList,
            ByteBuffer buffer) {
        this.socket = socket;
        buffer.limit(Short.BYTES * 2);
        handlers = handlerList;
        this.buffer = buffer;
    }

    /**
     * Create a new connection boxing.
     *
     * @param socket The socket channel. Can be ClientSocket or Server Accept Socket.
     */
    public RemoteConnection(SocketChannel socket) {
        this(socket, new HandlerList<>(), ByteBuffer.allocateDirect(1024));
    }

    /**
     * Send packet to remote
     *
     * @param type The packet id.
     * @param data The data send
     * @throws IOException IOException
     */
    public void sendPacket(short type, @NotNull byte[] data) throws IOException {
        sendPacket(type, data, 0, data.length);
    }

    /**
     * Get the data buffer.
     * You need to read the data sent from here.
     *
     * @return remote data
     */
    public ByteBuffer getBuffer() {
        return buffer;
    }

    /**
     * Send packet to remote. It used {@link ByteBuffer#wrap(byte[], int, int)}
     *
     * @param type The packet id
     * @param data The data send
     * @param off  The data offset
     * @param len  The data length
     * @throws IOException
     */
    public void sendPacket(short type, @NotNull byte[] data, int off, int len) throws IOException {
        sendPacket(type, ByteBuffer.wrap(data, off, len));
    }

    /**
     * Sending a empty packet.
     *
     * @param type The packet id.
     * @throws IOException IOException
     */
    protected void sendEmptyPacket(short type) throws IOException {
        if (status == Status.DISCONNECT) throw new IOException("Connection was closed.");
        //noinspection RedundantCast
        socket.write((ByteBuffer) (ByteBuffer.allocate(Short.BYTES * 2).putShort(type).putShort((short) 0).flip()));
    }

    /**
     * Send a packet to remote
     *
     * @param type The packet id.
     * @param send The data
     * @throws IOException IOException.
     */
    public void sendPacket(short type, @NotNull ByteBuffer send) throws IOException {
        if (status == Status.DISCONNECT) throw new IOException("Connection was closed.");
        if (!send.hasRemaining()) {
            sendEmptyPacket(type);
        } else {
            int size = send.remaining();
            if (size < Short.BYTES) {
                ByteBuffer b = ByteBuffer.allocate(Short.BYTES * 2);
                b.putShort(type).putShort((short) size).flip();
                socket.write(b);
                socket.write(send);
            } else if (size >= Short.BYTES * 2) {
                int limit = send.limit();
                int pos = send.position();
                short a = send.getShort(), b = send.getShort();
                send.position(pos);
                send.putShort(type).putShort((short) size).position(pos);
                send.limit(pos + (Short.BYTES * 2));
                socket.write(send);
                send.position(pos);
                send.putShort(a).putShort(b).position(pos).limit(limit);
                socket.write(send);
            } else {
                int limit = send.limit();
                int pos = send.position();
                send.limit(pos + Short.BYTES);
                short value = send.getShort();
                send.position(pos);
                send.putShort(type).position(pos);
                socket.write(send);
                send.position(pos);
                send.putShort((short) size).position(pos);
                socket.write(send);
                send.position(pos);
                send.putShort(value).position(pos).limit(limit);
                socket.write(send);
            }
        }
    }

    /**
     * Send "Bad packet" packet and disconnect this connection.
     * <p>
     * {@code 0x1, "Bad Packet.".getBytes(StandardCharsets.UTF_8)}
     *
     * @throws IOException If cannot send packet/cannot disconnect.
     */
    public void badPacket() throws IOException {
        IOException ioe = null;
        try {
            sendPacket((short) 0x1, "Bad Packet.".getBytes(StandardCharsets.UTF_8));
        } catch (IOException i) {
            ioe = i;
        }
        dis0(ioe);
    }

    private void dis0(IOException ioe) throws IOException {
        try {
            disconnect();
        } catch (IOException ix) {
            if (ioe != null) ix.addSuppressed(ioe);
            throw ix;
        }
        if (ioe != null) throw ioe;
    }

    /**
     * Send packet "0x1, &lt;message&gt;" and call {@link #disconnect()}
     *
     * @param message The disconnect message will send.
     * @throws IOException IOException
     */
    public void disconnect(String message) throws IOException {
        IOException ioe = null;
        if (message != null) {
            try {
                sendPacket((short) 0x1, message.getBytes(StandardCharsets.UTF_8));
            } catch (IOException i) {
                ioe = i;
            }
        }
        dis0(ioe);
    }

    /**
     * Close this connection
     *
     * @throws IOException when cannot closed this connection.
     */
    public void disconnect() throws IOException {
        status = Status.DISCONNECT;
        socket.shutdownInput();
        socket.shutdownOutput();
        socket.close();
    }

    /**
     * Execute a Selector select method
     *
     * @throws IOException Error
     * @see Selector
     * @see Selector#select()
     * @see Selector#selectedKeys()
     * @see RemoteServer#select()
     */
    public void onSelected() throws IOException {
        if (status == Status.DISCONNECT) return;
        if (buffer.hasRemaining()) {
            status = Status.WAIT_FULL_PACKET;
            // Buffer need to read more data.
            try {
                socket.read(buffer);
            } catch (IOException ioe) {
                status = Status.DISCONNECT;
                try {
                    disconnect();
                } catch (IOException ignore) {
                }
                post();
                throw ioe;
            }
        } else {
            status = Status.PROCESSING_PACKET;
            // Data read.
            buffer.flip();
            if (!acc) {// We reade Packet Type.
                current_packet = buffer.getShort();
                short limit = buffer.getShort();
                if (limit < 0 || limit > buffer.capacity()) {
                    // Max of size. Sending BAD Packet Response.
                    badPacket();
                    return;
                }
                buffer.clear();
                buffer.limit(limit);
                acc = true;
            } else {
                post();
                buffer.clear();
                buffer.limit(Short.BYTES * 2);
                acc = false;
            }
            status = Status.WAIT_FULL_PACKET;
        }
    }

    /**
     * Get event handlers for this connection
     * @return The event handlers. NOT GLOBAL
     */
    @Override
    public HandlerList<RemoteConnection> getHandlerList() {
        return handlers;
    }
}

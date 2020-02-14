/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 24:50:41
 *
 * MXLib/mxlib.message/AuthenticationRemoteConnection.java
 */

package cn.mcres.karlatemp.mxlib.remote;

import cn.mcres.karlatemp.mxlib.event.HandlerList;
import cn.mcres.karlatemp.mxlib.tools.ThrowHelper;
import cn.mcres.karlatemp.mxlib.util.ByteBufferInputStream;
import cn.mcres.karlatemp.mxlib.util.ByteBufferOutputStream;
import org.jetbrains.annotations.NotNull;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class AuthenticationRemoteConnection extends RemoteConnection {
    public static final Cipher cipher;

    static {
        Cipher c;
        try {
            c = Cipher.getInstance("RSA");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            c = ThrowHelper.thrown(e);
        }
        cipher = c;
    }

    private PrivateKey pri;
    private PublicKey pub;
    private final ByteBuffer decode = ByteBuffer.allocateDirect(1536),
            ecWork = ByteBuffer.allocateDirect(buffer.capacity());
    private ByteBuffer encode_;
    private boolean encode = false;
    private final ByteBufferInputStream bbis = new ByteBufferInputStream(buffer);
    private final ByteBufferOutputStream bbos = new ByteBufferOutputStream(ecWork);
    private final byte[] dec = new byte[1024];
    private final boolean client;
    private final ByteBuffer bufferBuffer = ByteBuffer.allocateDirect(buffer.capacity());

    public AuthenticationRemoteConnection(SocketChannel socket) {
        this(true, socket);
    }

    private AuthenticationRemoteConnection(boolean client, SocketChannel socket) {
        super(socket, new HandlerList(), ByteBuffer.allocateDirect(2048));
        this.client = client;
    }

    public AuthenticationRemoteConnection(SocketChannel socket, @NotNull KeyPair rsa) {
        this(false, socket);
        this.pub = rsa.getPublic();
        this.pri = rsa.getPrivate();
    }

    @Override
    public void sendPacket(short type, @NotNull ByteBuffer send) throws IOException {
        synchronized (this) {
            if (!send.hasRemaining()) sendEmptyPacket(type);
            else {
                encode_ = send;
                encodeData();
                super.sendPacket(type, ecWork);
            }
        }
    }

    private void encodeData() throws IOException {
        ByteBuffer encode_ = this.encode_;
        if (!encode_.hasRemaining()) {
            ecWork.position(0).limit(0);
            return;
        }
        ecWork.clear();
        if (!encode) {
            ecWork.put(encode_);
            ecWork.flip();
            return;
        }
        bufferBuffer.clear();
        synchronized (cipher) {
            try {
                cipher.init(Cipher.ENCRYPT_MODE, client ? pub : pri);
                cipher.doFinal(encode_, bufferBuffer);
            } catch (Exception e) {
                throw new IOException(e);
            }
        }
        bufferBuffer.flip();
        ecWork.clear();
        try (GZIPOutputStream gzip = new GZIPOutputStream(bbos)) {
            while (bufferBuffer.hasRemaining()) {
                int rem = Math.min(dec.length, bufferBuffer.remaining());
                bufferBuffer.get(dec, 0, rem);
                gzip.write(dec, 0, rem);
            }
        }
        ecWork.flip();
    }

    private synchronized void decodeData() throws IOException {
        if (!buffer.hasRemaining()) {
            decode.position(0).limit(0);
            return;
        }
        decode.clear();
        if (encode) {
            try (GZIPInputStream zip = new GZIPInputStream(bbis)) {
                while (true) {
                    int len = zip.read(dec);
                    if (len == -1) {
                        break;
                    }
                    decode.put(dec, 0, len);
                }
            }
            synchronized (cipher) {
                try {
                    cipher.init(Cipher.DECRYPT_MODE, client ? pub : pri);
                    buffer.clear();
                    decode.flip();
                    buffer.put(decode).flip();
                    decode.clear();
                    cipher.doFinal(buffer, decode);
                } catch (Exception e) {
                    throw new IOException(e);
                }
            }
        } else {
            decode.put(buffer);
        }
        decode.flip();
    }

    private int cc;

    public AuthenticationRemoteConnection init() throws IOException {
        if (client) {
            while (pub == null) {
                onSelected();
            }
        } else {
            System.out.println(Arrays.toString(pub.getEncoded()));
            sendPacket((short) 5, pub.getEncoded());
        }
        encode = true;
        return this;
    }

    @Override
    public ByteBuffer getBuffer() {
        return decode;
    }

    @Override
    public void post() {
        if (status == Status.DISCONNECT) {
            super.post();
            return;
        }
        if (pub == null) {
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            System.out.println(current_packet + "] " + Arrays.toString(data));
            try {
                pub = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(data));
            } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
                ThrowHelper.thrown(e);
            }
        } else {
            try {
                decodeData();
                super.post();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

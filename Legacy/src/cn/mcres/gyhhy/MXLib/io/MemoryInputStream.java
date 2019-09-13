/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MemoryInputStream.java@author: karlatemp@vip.qq.com: 19-9-11 下午1:14@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.io;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.nio.channels.WritableByteChannel;
import java.nio.channels.ReadableByteChannel;

public class MemoryInputStream extends InputStream implements ReadableByteChannel, WritableByteChannel, Closeable, AutoCloseable {

    private byte[] buffer;
    private byte[] cop = null;
    private final Object lock = new Object();
    private final Object readLock = new Object();
    private int pos;
    private int limit;
    private boolean l;
    private volatile boolean stop = false;
    private volatile boolean flush = false;

    public void flush() {
        synchronized (lock) {
            flush = true;
            lock.notify();
        }
    }

    public void close() {
        closeOutputStream();
    }

    public MemoryInputStream() {
        this(false);
    }

    public MemoryInputStream(int size) {
        this(size, false);
    }

    public MemoryInputStream(byte[] warp) {
        this(warp, false);
    }

    public MemoryInputStream(byte[] warp, boolean block) {
        this(warp, 0, warp.length, block);
    }

    public MemoryInputStream(byte[] warp, int form, int length) {
        this(warp, form, length, false);
    }

    public MemoryInputStream(byte[] warp, int off, int length, boolean block) {
        check(warp, off, length);
        l = block;
        pos = 0;
        limit = (buffer = new byte[warp.length]).length;
        System.arraycopy(warp, off, buffer, 0, length);
    }

    public MemoryInputStream(int size, boolean block) {
        l = block;
        buffer = new byte[size];
        pos = limit = 0;
    }

    public MemoryInputStream(boolean block) {
        this(16, block);
    }

    public void reinit() throws IOException {
        if (!stop) {
            throw new IOException("Stream not closed.");
        }
        synchronized (lock) {
            closeAll();
            pos = 0;
            limit = 0;
            buffer = new byte[16];
        }
    }

    public void closeAll() {
        synchronized (lock) {
            closeOutputStream();
            synchronized (readLock) {
                limit = 0;
                buffer = null;
                cop = null;
            }
        }
    }

    public void closeOutputStream() {
        synchronized (lock) {
            stop = true;
            lock.notifyAll();
        }
    }

    private byte[] cop(int size) {
        synchronized (lock) {
            if (cop == null) {
                return cop = new byte[size];
            }
            if (cop.length >= size) {
                return cop;
            }
            return cop = new byte[size];
        }
    }

    @Override
    public int read(byte[] buf) {
        return read(buf, 0, buf.length);
    }

    private void left() {
        synchronized (lock) {
            if (pos == 0) {
                return;
            }
            int size;
            byte[] save = cop(size = this.available());
            synchronized (save) {
                System.arraycopy(buffer, pos, save, 0, size);
                System.arraycopy(save, 0, buffer, 0, size);
            }
            limit = size;
            pos = 0;
        }
    }

    public byte[] getByteArray() {
        synchronized (readLock) {
            synchronized (lock) {
                int size = this.available();
                if (size < 1) {
                    return new byte[0];
                }
                byte[] dd = new byte[size];
                read(dd);
                return dd;
            }
        }
    }

    public static void main(String[] args) throws Throwable {
        System.out.println("Started..");
        try (MemoryInputStream mis = new MemoryInputStream()) {
            mis.write("12345678901234567890".getBytes());
            mis.skip(10);
            System.out.write(mis.getByteArray());
        }
    }

    private void check(byte[] buf, int off, int leng) {
        if (off < 0 || off + leng > buf.length) {
            throw new ArrayIndexOutOfBoundsException("Off: " + off + ", Length:" + leng + ", BufferLength:" + buf.length);
        }
    }

    public void write(byte[] buf, int off, int leng) throws IOException {
        if (stop) {
            throw new IOException("Stream closed.");
        }
        if (leng < 1) {
            return;
        }
        check(buf, off, leng);
        synchronized (lock) {
            if (limit + leng < buffer.length) {
                System.arraycopy(buf, off, buffer, limit, leng);
                limit += leng;
            } else {
                left();
                if (limit + leng < buffer.length) {
                    System.arraycopy(buf, off, buffer, limit, leng);
                    limit += leng;
                } else {
                    int size = this.available();
                    byte[] dot = cop(size + leng);
                    synchronized (dot) {
                        try {
                            System.arraycopy(buffer, pos, dot, 0, size);
                            System.arraycopy(buf, off, dot, size, leng);
                            byte[] ob = buffer;
                            buffer = dot;
                            limit += leng;
                            cop = ob;
                        } catch (RuntimeException re) {
                            throw new RuntimeException("Dot: " + dot.length + ", Size:" + size + ", Leng: " + leng + ", off:" + off + ", BFL: " + buf.length + ", SaveLength:" + buffer.length + ", Pos: " + pos, re);
                        }
                    }
                }
            }
            lock.notify();
        }
    }

    public void write(int i) throws IOException {
        if (stop) {
            throw new IOException("Stream closed.");
        }
        synchronized (lock) {
            if (limit < buffer.length) {
                buffer[limit++] = (byte) i;
            } else {
                left();
                if (limit < buffer.length) {
                    buffer[limit++] = (byte) i;
                } else {
                    int size = this.available();
                    byte[] dot = cop(size + 1);
                    synchronized (dot) {
                        System.arraycopy(buffer, pos, dot, 0, size);
                        dot[size] = (byte) i;
                        byte[] obuffer = buffer;
                        buffer = dot;
                        limit++;
                        cop = obuffer;
                    }
                }
            }
            lock.notify();
        }
    }

    @Override
    public int read(byte[] buf, int off, int leng) {
        check(buf, off, leng);
        if (leng == 0) {
            int size = this.available();
            if (size < 1) {
                return -1;
            }
            return 0;
        }
        synchronized (readLock) {
            synchronized (lock) {
                if (l && (!stop)) {
                    while (this.available() < leng) {
                        if (stop || flush) {
                            leng = Math.max(0, Math.min(this.available(), leng));
                            flush = false;
                            break;
                        }
                        try {
                            lock.wait(100l);
                        } catch (InterruptedException ex) {
                        }
                    }
                    if (leng == 0) {
                        return -1;
                    }
                    System.arraycopy(buffer, pos, buf, off, leng);
                    pos += leng;
                    return leng;
                } else {
                    leng = Math.max(0, Math.min(this.available(), leng));
                    if (leng < 1) {
                        return -1;
                    }
                    System.arraycopy(buffer, pos, buf, off, leng);
                    pos += leng;
                    return leng;
                }
            }
        }
    }

    @Override
    public int read() {
        synchronized (readLock) {
            if (l && (!stop)) {
                synchronized (lock) {
                    while (this.available() < 1) {
                        if (stop) {
                            byte[] a = buffer;
                            if (pos < limit) {
                                return a[pos++];
                            }
                            return -1;
                        }
                        try {
                            lock.wait(100l);
                        } catch (InterruptedException ex) {
                        }
                    }
                    byte[] a = buffer;
                    return a[pos++];
                }
            } else {
                byte[] a = buffer;
                if (this.available() > 0) {
                    return a[pos++];
                }
                return -1;
            }
        }
    }

    public void write(byte[] data) throws IOException {
        write(data, 0, data.length);
    }

    @Override
    public int available() {
        return Math.max(0, limit - pos);
    }
    final O o = new O(this);

    public OutputStream asOutputStream() {
        return o;
    }

    @Override
    public int read(ByteBuffer dst) {
        int leng = dst.remaining();
        synchronized (readLock) {
            synchronized (lock) {
                if (l && (!stop)) {
                    while (this.available() < leng) {
                        if (stop || flush) {
                            leng = Math.max(0, Math.min(this.available(), leng));
                            flush = false;
                            break;
                        }
                        try {
                            lock.wait(100l);
                        } catch (InterruptedException ex) {
                        }
                    }
                    if (leng == 0) {
                        return -1;
                    }
                    dst.put(buffer, pos, leng);
                    pos += leng;
                    return leng;
                } else {
                    leng = Math.max(0, Math.min(this.available(), leng));
                    if (leng < 1) {
                        return -1;
                    }
                    dst.put(buffer, pos, leng);
                    pos += leng;
                    return leng;
                }
            }
        }
    }

    @Override
    public boolean isOpen() {
        return !stop;
    }

    @Deprecated
    public int limit() {
        return limit;
    }

    @Deprecated
    public int position() {
        return pos;
    }

    @Deprecated
    public byte[] getContainer() {
        return buffer;
    }

    @Deprecated
    public Object getReadingLock() {
        return readLock;
    }

    @Deprecated
    public Object getLock() {
        return lock;
    }

    @Override
    public long skip(long sk) {
        synchronized (readLock) {
            synchronized (lock) {
                if (sk > 0) {
                    ByteBuffer bb = ByteBuffer.allocateDirect(1024);
                    while (sk > 0) {
                        bb.position(0);
                        int rm;
                        if (sk > 1024) {
                            rm = 1024;
                        } else {
                            rm = (int) sk;
                        }
                        bb.limit(rm);
                        sk -= rm;
                        pos += rm;
                    }
                }
            }
        }
        return -1;
    }

    @Override
    public int write(ByteBuffer src) throws IOException {
        int leng = src.remaining();
        if (stop) {
            throw new IOException("Stream closed.");
        }
        if (leng < 1) {
            return 0;
        }
        synchronized (lock) {
            if (limit + leng < buffer.length) {
                byte[] cpp = cop(leng);
                synchronized (cpp) {
                    src.get(cpp, 0, leng);
                    System.arraycopy(cpp, 0, buffer, limit, leng);
                }
                limit += leng;
            } else {
                left();
                if (limit + leng < buffer.length) {
                    byte[] cpp = cop(leng);
                    synchronized (cpp) {
                        src.get(cpp, 0, leng);
                        System.arraycopy(cpp, 0, buffer, limit, leng);
                    }
                    limit += leng;
                } else {
                    int size = this.available();
                    byte[] dot = cop(size + leng);
                    synchronized (dot) {
                        try {
                            System.arraycopy(buffer, pos, dot, 0, size);
                            src.get(dot, size, leng);
                            byte[] ob = buffer;
                            buffer = dot;
                            limit += leng;
                            cop = ob;
                        } catch (RuntimeException re) {
                            throw new RuntimeException("Dot: " + dot.length + ", Size:" + size + ", Leng: " + leng + ", SaveLength:" + buffer.length + ", Pos: " + pos, re);
                        }
                    }
                }
            }
            lock.notify();
        }
        return leng;
    }

    static class O extends OutputStream implements ReadableByteChannel, WritableByteChannel, Closeable, AutoCloseable {

        private final MemoryInputStream mis;

        O(MemoryInputStream mis) {
            this.mis = mis;
        }

        public int available() {
            return mis.available();
        }

        public void write(byte[] data, int off, int length) throws IOException {
            mis.write(data, off, length);
        }

        public void write(byte[] data) throws IOException {
            mis.write(data);
        }

        public void write(int i) throws IOException {
            mis.write(i);
        }

        public int read(byte[] b) {
            return mis.read(b);
        }

        public int read(byte[] b, int off, int len) {
            return mis.read(b, off, len);
        }

        public int read() {
            return mis.read();
        }

        public long skip(long n) {
            return mis.skip(n);
        }

        public void close() throws IOException {
            mis.closeOutputStream();
        }

        public void mark(int readlimit) {
            mis.mark(readlimit);
        }

        public void reset() throws IOException {
            mis.reset();
        }

        public boolean markSupported() {
            return mis.markSupported();
        }

        @Override
        public int read(ByteBuffer dst) {
            return mis.read(dst);
        }

        @Override
        public boolean isOpen() {
            return mis.isOpen();
        }

        @Override
        public int write(ByteBuffer src) throws IOException {
            return mis.write(src);
        }

    }

}

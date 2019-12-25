/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: FileChunkDataStorage.java@author: karlatemp@vip.qq.com: 19-11-21 下午10:20@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.data;

import cn.mcres.karlatemp.mxlib.interfaces.ExceptionConsumer;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import cn.mcres.karlatemp.mxlib.util.WrappedDataOutput;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.atomic.AtomicInteger;

public class FileChunkDataStorage {
    private final RandomAccessFile access;
    private long nextStart;

    public FileChunkDataStorage(@NotNull RandomAccessFile access) {
        this.access = access;
    }

    public void toTop() throws IOException {
        access.seek(0);
        nextStart = 0;
    }

    private static class PacketStreamR extends InputStream {

        private final RandomAccessFile file;
        private final long end;
        private long position;

        @Override
        public int available() {
            return (int) ava();
        }

        long ava() {
            return end - position;
        }

        @Override
        public int read(@NotNull byte[] b, int off, int len) throws IOException {
            if (ava() > 0) {
                int size = available();
                file.seek(position);
                int read;
                if (size < len) {
                    read = file.read(b, off, size);
                } else {
                    read = file.read(b, off, len);
                }
                if (read > -1) position += read;
                return read;
            } else {
                return -1;
            }
        }

        @Override
        public int read() throws IOException {
            if (ava() > 0) {
                file.seek(position);
                int ret = file.read();
                position++;
                return ret;
            }
            return -1;
        }

        PacketStreamR(RandomAccessFile access, long end, long position) {
            this.end = end;
            this.position = position;
            this.file = access;
        }

    }

    private static class PacketStream extends OutputStream {
        private final RandomAccessFile file;
        private final long end;
        private long position;
        private final ByteArrayOutputStream over_range = new ByteArrayOutputStream();

        PacketStream(RandomAccessFile access, long end, long position) {
            this.end = end;
            this.position = position;
            this.file = access;
        }

        private long alive() {
            return end - position;
        }

        @Override
        public void write(int b) throws IOException {
            if (alive() > 0) {
                file.seek(position);
                file.write(b);
                position++;
            } else {
                over_range.write(b);
            }
        }

        @Override
        public void write(@NotNull byte[] b) throws IOException {
            write(b, 0, b.length);
        }

        @Override
        public void write(@NotNull byte[] b, int off, int len) throws IOException {
            if (len <= alive()) {
                file.seek(position);
                file.write(b, off, len);
                position += len;
            } else {
                int av = (int) alive();
                file.seek(position);
                file.write(b, off, av);
                over_range.write(b, off + av, len - av);
            }
        }

    }

    public boolean hasMoreChunk() throws IOException {
        return access.length() - nextStart >= Integer.BYTES;
    }

    public FileChunk createChunk() throws IOException {
        if (hasMoreChunk()) return nextChunk();
        access.seek(nextStart);
        access.writeInt(Integer.BYTES);
        access.writeInt(0);
        return nextChunk();
    }

    public FileChunk nextChunk() throws IOException {
        if (!hasMoreChunk()) return createChunk();
        access.seek(nextStart);
        final long sttw = access.getFilePointer();
        final AtomicInteger fullSize = new AtomicInteger(access.readInt());
        final AtomicInteger realSize = new AtomicInteger(access.readInt());
        FileChunk chunk = new FileChunk();
        long start = access.getFilePointer();
        chunk.end = start + realSize.get();
        chunk.start = start;
        chunk.position = start;
        chunk.file = access;

        chunk.addSize = added -> {
            long pos = access.getFilePointer();

            realSize.addAndGet(added);
            int n_rs = realSize.get();
            long insertPos = chunk.end;

            int psize = fullSize.get();
            if (n_rs + Integer.BYTES > psize) {
                insertEmpty(insertPos, n_rs + Integer.BYTES - psize, access);
                fullSize.set(n_rs + Integer.BYTES);
            }

            access.seek(sttw);

            access.writeInt(fullSize.get());
            access.writeInt(n_rs);

            chunk.end += added;

            access.seek(pos);
            nextStart = sttw + Integer.BYTES + fullSize.get();
        };
        nextStart = sttw + Integer.BYTES + fullSize.get();
        return chunk;
    }

    public static void insertEmpty(long position, int size,@NotNull RandomAccessFile access) throws IOException {
        Toolkit.IO.insertEmpty(position, size, access);
    }

    interface W {
        void accept(int wx) throws IOException;
    }

    public static class FileChunk {
        long start, end, position;
        RandomAccessFile file;
        W addSize;

        @NotNull
        @Contract(pure = true)
        public DataInput postRead() {
            PacketStreamR st = new PacketStreamR(file, end, position);
            return new DataInputStream(st);
        }

        public void postWrite(@NotNull ExceptionConsumer<DataOutput> hook) throws IOException {
            PacketStream ps = new PacketStream(file, end, position);
            DataOutputStream dos = new DataOutputStream(ps);
            try {
                hook.accept(dos);
            } catch (IOException ioe) {
                throw ioe;
            } catch (Exception e) {
                throw new IOException(e);
            }
            final ByteArrayOutputStream range = ps.over_range;
            if (range.size() > 0) {
                addSize.accept(range.size());
                file.seek(ps.end);
                file.write(range.toByteArray());
            }
        }
    }
}

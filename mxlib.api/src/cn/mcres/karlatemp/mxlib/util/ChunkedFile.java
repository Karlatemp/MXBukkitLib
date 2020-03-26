/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/03/13 18:27:46
 *
 * MXLib/mxlib.api/ChunkedFile.java
 */

package cn.mcres.karlatemp.mxlib.util;

import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLongArray;

public class ChunkedFile implements Closeable {
    protected FileOpener opener;
    private final int maxSize;
    private final int chunkSize;
    protected Deque<RandomAccessFile> opened;
    protected AtomicLongArray offsets;
    public static final int DEFAULT_MAGIC_NUMBER = 0xFE77_17AE;
    protected int MAGIC_NUMBER = DEFAULT_MAGIC_NUMBER;
    protected transient long nextOffset;

    public interface AccessAction<T> {
        T invoke(RandomAccessFile access) throws IOException;
    }

    protected void initializeFields() {
        opened = new ConcurrentLinkedDeque<>();
        offsets = new AtomicLongArray(maxSize);
    }

    public <T> T invoke(int index, AccessAction<T> action) throws IOException {
        checkBound(index);
        return invoke(file -> {
            initialize(index, file);
            return action.invoke(file);
        });
    }

    protected void initialize(int index, RandomAccessFile file) throws IOException {
        var offset = offsets.get(index);
        if (offset == 0) {
            synchronized (this) {
                offset = offsets.get(index);
                if (offset == 0) {
                    offsets.set(index, offset = nextOffset);
                    file.seek((index * Long.BYTES) + Integer.BYTES);
                    file.writeLong(offset);
                    nextOffset += chunkSize;
                }
            }
        }
        file.seek(offset);
    }

    protected final void checkBound(int index) throws IOException {
        if (!inBound(index)) throw new IOException("Out of bound: " + index);
    }

    protected final boolean inBound(int index) {
        return index >= 0 && index < maxSize;
    }

    protected void initialize(boolean override) throws IOException {
        initializeFields();
        invoke(file -> {
            file.seek(0);
            if (file.length() < 4) {
                file.writeInt(MAGIC_NUMBER);
            } else if (file.readInt() != MAGIC_NUMBER) {
                if (override) {
                    file.seek(0);
                    file.writeInt(MAGIC_NUMBER);
                    int x = maxSize;
                    while (x-- > 0) {
                        file.writeLong(0);
                    }
                    nextOffset = file.getFilePointer();
                    return null;
                }
                throw new IOException("Not chunked file format!");
            }
            if (file.length() < 4 + (maxSize * Long.BYTES)) {
                int x = maxSize;
                while (x-- > 0) {
                    file.writeLong(0);
                }
                nextOffset = file.getFilePointer();
            } else {
                long c = nextOffset = file.getFilePointer();
                for (int i = 0; i < maxSize; i++) {
                    long v = file.readLong();
                    if (v < c) {
                        v = 0;
                    } else {
                        nextOffset = Math.max(c + ((v - c) / chunkSize * chunkSize), nextOffset);
                    }
                    offsets.set(i, v);
                }
            }
            return null;
        });
    }

    protected RandomAccessFile open() throws IOException {
        if (opened == null) throw new IOException("chunked file closed");
        RandomAccessFile file = opened.poll();
        if (file == null) return opener.access();
        return file;
    }

    protected <T> T invoke(AccessAction<T> action) throws IOException {
        var file = open();
        try {
            file.seek(0);
            T val = action.invoke(file);
            opened.add(file);
            return val;
        } catch (Throwable any) {
            try {
                file.close();
            } catch (IOException ioe) {
                any.addSuppressed(ioe);
            }
            throw any;
        }
    }

    public interface FileOpener {
        @NotNull
        RandomAccessFile access() throws IOException;
    }

    @Override
    public void close() throws IOException {
        final Deque<RandomAccessFile> opened;
        synchronized (this) {
            opener = null;
            opened = this.opened;
            this.opened = null;
        }
        if (opened != null) {
            List<Throwable> exceptions = new ArrayList<>(opened.size());
            for (RandomAccessFile raf : opened) {
                try {
                    raf.close();
                } catch (Throwable any) {
                    exceptions.add(any);
                }
            }
            if (!exceptions.isEmpty()) {
                if (exceptions.size() == 1) {
                    throwIOE(exceptions.get(0));
                }
                IOException ioe = new IOException("Multiple errors");
                for (var e : exceptions) {
                    ioe.addSuppressed(e);
                }
                throw ioe;
            }
        }
    }

    protected void throwIOE(Throwable throwable) throws IOException {
        if (throwable instanceof IOException) throw (IOException) throwable;
        if (throwable instanceof Error) throw (Error) throwable;
        if (throwable instanceof RuntimeException) throw (RuntimeException) throwable;
        if (throwable instanceof InvocationTargetException) throwIOE(throwable.getCause());
        throw new IOException(throwable);
    }

    public ChunkedFile(int maxSize, int chunkSize, @NotNull FileOpener opener)
            throws IOException {
        this(maxSize, chunkSize, opener, false);
    }

    public ChunkedFile(int maxSize, int chunkSize, @NotNull FileOpener opener, boolean override)
            throws IOException {
        this.maxSize = maxSize;
        this.chunkSize = chunkSize;
        this.opener = opener;
        initialize(override);
    }
}

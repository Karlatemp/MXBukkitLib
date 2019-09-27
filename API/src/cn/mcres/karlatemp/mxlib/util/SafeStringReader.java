/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: SafeStringReader.java@author: karlatemp@vip.qq.com: 19-9-27 下午12:47@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.util;

import java.io.IOError;
import java.io.IOException;
import java.io.Reader;

/**
 * {@link java.io.StringReader}'s Upgraded version
 *
 * @author Karlatemp
 */
public class SafeStringReader extends Reader {

    private String str;
    private int length;
    private int next = 0;
    private int mark = 0;

    /**
     * Creates a new string reader.
     *
     * @param s String providing the character stream.
     */
    public SafeStringReader(String s) {
        this.str = s;
        this.length = s.length();
    }

    public boolean hasNext() {
        if (str == null) {
            return false;
        }
        if (next >= length) {
            return false;
        }
        return true;
    }

    public SafeStringReader setString(String str) {
        this.str = str;
        this.length = str.length();
        this.next = this.mark;
        return this;
    }

    public String getString() {
        return str;
    }

    /**
     * Check to make sure that the stream has not been closed
     */
    private void ensureOpen() {
        if (str == null) {
            throw new IOError(new IOException("Stream closed"));
        }
    }

    /**
     * Reads a single character.
     *
     * @return The character read, or -1 if the end of the stream has been
     * reached
     */
    public int read() {
        synchronized (this) {
            ensureOpen();
            if (next >= length) {
                return -1;
            }
            return str.charAt(next++);
        }
    }

    /**
     * Reads characters into a portion of an array.
     *
     * @param cbuf Destination buffer
     * @param off  Offset at which to start writing characters
     * @param len  Maximum number of characters to read
     * @return The number of characters read, or -1 if the end of the stream has
     * been reached
     */
    public int read(char cbuf[], int off, int len) {
        synchronized (this) {
            ensureOpen();
            if ((off < 0) || (off > cbuf.length) || (len < 0)
                    || ((off + len) > cbuf.length) || ((off + len) < 0)) {
                throw new IndexOutOfBoundsException();
            } else if (len == 0) {
                return 0;
            }
            if (next >= length) {
                return -1;
            }
            int n = Math.min(length - next, len);
            str.getChars(next, next + n, cbuf, off);
            next += n;
            return n;
        }
    }

    /**
     * Skips the specified number of characters in the stream. Returns the
     * number of characters that were skipped.
     *
     * <p>
     * The <code>ns</code> parameter may be negative, even though the
     * <code>skip</code> method of the {@link java.io.Reader} superclass throws an
     * exception in this case. Negative values of <code>ns</code> cause the
     * stream to skip backwards. Negative return values indicate a skip
     * backwards. It is not possible to skip backwards past the beginning of the
     * string.
     *
     * <p>
     * If the entire string has been read or skipped, then this method has no
     * effect and always returns 0.
     */
    public long skip(long ns) {
        synchronized (this) {
            ensureOpen();
            if (next >= length) {
                return 0;
            }
            // Bound skip by beginning and end of the source
            long n = Math.min(length - next, ns);
            n = Math.max(-next, n);
            next += n;
            return n;
        }
    }

    /**
     * Tells whether this stream is ready to be read.
     *
     * @return True if the next read() is guaranteed not to block for input
     */
    public boolean ready() {
        synchronized (this) {
            ensureOpen();
            return true;
        }
    }

    /**
     * Tells whether this stream supports the mark() operation, which it does.
     */
    public boolean markSupported() {
        return true;
    }

    /**
     * Marks the present position in the stream. Subsequent calls to reset()
     * will reposition the stream to this point.
     *
     * @param readAheadLimit Limit on the number of characters that may be read
     *                       while still preserving the mark. Because the stream's input comes from a
     *                       string, there is no actual limit, so this argument must not be negative,
     *                       but is otherwise ignored.
     * @throws IllegalArgumentException If {@code readAheadLimit < 0}
     */
    public void mark(int readAheadLimit) {
        if (readAheadLimit < 0) {
            throw new IllegalArgumentException("Read-ahead limit < 0");
        }
        synchronized (this) {
            ensureOpen();
            mark = next;
        }
    }

    /**
     * Resets the stream to the most recent mark, or to the beginning of the
     * string if it has never been marked.
     */
    public void reset() {
        synchronized (this) {
            ensureOpen();
            next = mark;
        }
    }

    /**
     * Closes the stream and releases any system resources associated with it.
     * Once the stream has been closed, further read(), ready(), mark(), or
     * reset() invocations will throw an IOException. Closing a previously
     * closed stream has no effect.
     */
    public void close() {
        str = null;
    }
}

/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: StringReader.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.io;

import cn.mcres.karlatemp.mxlib.util.SafeStringReader;

import java.io.IOError;
import java.io.IOException;

/**
 * {@link java.io.StringReader}'s Upgraded version
 *
 * @author Karlatemp
 */
public class StringReader extends SafeStringReader {

    /**
     * Creates a new string reader.
     *
     * @param s String providing the character stream.
     */
    public StringReader(String s) {
        super(s);
    }

    public boolean hasNext() {
        return super.hasNext();
    }

    public StringReader setString(String str) {
        super.setString(str);
        return this;
    }

    public String getString() {
        return super.getString();
    }

    /**
     * Reads a single character.
     *
     * @return The character read, or -1 if the end of the stream has been
     * reached
     */
    public int read() {
        return super.read();
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
        return super.read(cbuf, off, len);
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
        return super.skip(ns);
    }

    /**
     * Tells whether this stream is ready to be read.
     *
     * @return True if the next read() is guaranteed not to block for input
     */
    public boolean ready() {
        return super.ready();
    }

    /**
     * Tells whether this stream supports the mark() operation, which it does.
     */
    public boolean markSupported() {
        return super.markSupported();
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
        super.mark(readAheadLimit);
    }

    /**
     * Resets the stream to the most recent mark, or to the beginning of the
     * string if it has never been marked.
     */
    public void reset() {
        super.reset();
    }

    /**
     * Closes the stream and releases any system resources associated with it.
     * Once the stream has been closed, further read(), ready(), mark(), or
     * reset() invocations will throw an IOException. Closing a previously
     * closed stream has no effect.
     */
    public void close() {
        super.close();
    }
}

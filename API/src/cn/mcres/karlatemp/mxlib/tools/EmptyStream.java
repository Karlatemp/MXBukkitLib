/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.karlatemp.mxlib.tools;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * @author Karlatemp
 */
public class EmptyStream extends Writer implements ReadableByteChannel, WritableByteChannel, Appendable {
    public static final EmptyStream stream = new EmptyStream();

    public static EmptyStream getInstance() {
        return stream;
    }

    private final EmptyInputStream i;
    private final EmptyOutputStream o;
    private final EmptyReader r = new EmptyReader();

    private EmptyStream() {
        this.i = new EmptyInputStream();
        this.o = new EmptyOutputStream();
    }

    @Override
    public void write(@NotNull char[] cbuf, int off, int len) throws IOException {

    }

    public Reader asReader() {
        return r;
    }

    public InputStream asInputStream() {
        return i;
    }

    public OutputStream asOutputStream() {
        return o;
    }

    @Override
    public int read(ByteBuffer dst) {
        return -1;
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public void close() {
    }

    @Override
    public int write(ByteBuffer src) {
        return -1;
    }

    @Override
    public Writer append(CharSequence csq) throws IOException {
        return this;
    }

    @Override
    public Writer append(CharSequence csq, int start, int end) throws IOException {
        return this;
    }

    @Override
    public Writer append(char c) throws IOException {
        return this;
    }

    @Override
    public void flush() throws IOException {

    }

    @Override
    public void write(int c) throws IOException {
    }

    @Override
    public void write(char[] cbuf) throws IOException {
    }

    @Override
    public void write(String str) throws IOException {
    }

    @Override
    public void write(String str, int off, int len) throws IOException {
    }

    private static final class EmptyInputStream extends InputStream {

        @Override
        public int read() {
            return -1;
        }

        @Override
        public int read(byte[] b) {
            return -1;
        }

        @Override
        public int read(byte[] b, int off, int len) {
            return -1;
        }

        @Override
        public int available() throws IOException {
            return 0;
        }

        @Override
        public boolean markSupported() {
            return false;
        }

        @Override
        public long skip(long n) throws IOException {
            return 0;
        }

        @Override
        public void close() {

        }

        @Override
        public void reset() {
        }
    }

    private static final class EmptyOutputStream extends OutputStream {

        public EmptyOutputStream() {
        }

        @Override
        public void write(int b) {
        }

        @Override
        public void write(byte[] b) {
        }

        @Override
        public void write(byte[] b, int off, int len) {
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() {
        }
    }

    private static final class EmptyReader extends Reader {
        protected EmptyReader() {
            super();
        }

        @Override
        public int read(@NotNull CharBuffer target) throws IOException {
            return -1;
        }

        @Override
        public int read() throws IOException {
            return -1;
        }

        @Override
        public int read(@NotNull char[] cbuf) throws IOException {
            return -1;
        }

        @Override
        public long skip(long n) throws IOException {
            return 0;
        }

        @Override
        public boolean ready() throws IOException {
            return true;
        }

        @Override
        public boolean markSupported() {
            return false;
        }

        @Override
        public void mark(int readAheadLimit) throws IOException {
        }

        @Override
        public void reset() throws IOException {
        }

        @Override
        public int read(@NotNull char[] cbuf, int off, int len) throws IOException {
            return -1;
        }

        @Override
        public void close() throws IOException {

        }
    }
}

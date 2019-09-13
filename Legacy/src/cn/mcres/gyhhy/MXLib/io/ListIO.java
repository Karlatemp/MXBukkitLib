/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ListIO.java@author: karlatemp@vip.qq.com: 19-9-11 下午1:14@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Objects;

/**
 * {@link MemoryInputStream}
 */
@Deprecated
public class ListIO {

    private ListIO() {
    }

    private static byte cxr(int c) {
        switch (c) {
            case '1':
                return 1;
            case '2':
                return 2;
            case '3':
                return 3;
            case '4':
                return 4;
            case '5':
                return 5;
            case '6':
                return 6;
            case '7':
                return 7;
            case '8':
                return 8;
            case '9':
                return 9;
            case '0':
                return 0;
            case 'a':
            case 'A':
                return 0xa;
            case 'b':
            case 'B':
                return 0xb;
            case 'c':
            case 'C':
                return 0xc;
            case 'd':
            case 'D':
                return 0xd;
            case 'e':
            case 'E':
                return 0xe;
            case 'f':
            case 'F':
                return 0xf;

        }
        return (byte) c;
    }

    private static int[] mhex(String str) {
        return mhex(str, false);
    }

    private static int mhex(int[] ix) {
        int rt = 0;
        for (int i : ix) {
            rt <<= 8;
            rt += i;
        }
        return rt;
    }

    private static int[] mhex(String str, boolean fx) {
        if (str.length() > 8) {
            throw new ArrayIndexOutOfBoundsException();
        }
        final int[] req = fx ? new int[4] : new int[(str.length() + str.length() % 2) / 2];
        final int len = str.length();
        final int ln = req.length - 1;
        if (len > 0) {
            if (len > 1) {
                req[ln - 0] = (cxr(str.charAt(len - 2)) << 4) + cxr(str.charAt(len - 1));
            } else {
                req[ln - 0] = cxr(str.charAt(len - 1));
            }
        }
        if (len > 2) {
            if (len > 3) {
                req[ln - 1] = (cxr(str.charAt(len - 4)) << 4) + cxr(str.charAt(len - 3));
            } else {
                req[ln - 1] = cxr(str.charAt(len - 3));
            }
        }
        if (len > 4) {
            if (len > 5) {
                req[ln - 2] = (cxr(str.charAt(len - 6)) << 4) + cxr(str.charAt(len - 5));
            } else {
                req[ln - 2] = cxr(str.charAt(len - 5));
            }
        }
        if (len > 6) {
            if (len > 7) {
                req[ln - 3] = (cxr(str.charAt(len - 8)) << 4) + cxr(str.charAt(len - 7));
            } else {
                req[ln - 3] = cxr(str.charAt(len - 7));
            }
        }
        return req;
    }

    public static class ListInputStream extends InputStream {

        private MInputStream reader;
        public int size = 0;

        public ListInputStream(InputStream io) {
            this.reader = new MInputStream(io);
        }

        public void open() throws IOException {
            while (size-- > 0) {
                reader.read();
            }
            if (reader.hasNext()) {
                int[] ix = new int[]{
                        reader.read(), reader.read(), reader.read(), reader.read()
                };
                size = mhex(ix);
            }
        }

        public void close() throws IOException {
            try {
                reader.close();
            } catch (IOException e) {
                throw e;
            } finally {
                reader = null;
                size = 0;
            }
        }

        @Override
        public int read() throws IOException {
            if (size-- < 1) {
                return -1;
            }
            return reader.read();
        }

        public boolean hasNext() {
            return reader.hasNext();
        }
    }

    public static class ListOutputStream extends OutputStream {

        private final OutputStream out;
        private int[][] buffers;
        private int[] using;
        private int usd;

        public ListOutputStream(OutputStream out) {
            this.out = Objects.requireNonNull(out);
            buffers = new int[0][];
        }

        public void end() throws IOException {
            synchronized (this) {
                synchronized (out) {
                    if (using != null) {
                        if (usd < using.length) {
                            int[] ix = new int[usd];
                            System.arraycopy(using, 0, ix, 0, usd);
                            using = ix;
                        }
                    }
                    flush();
                    int size = bufferSize();
                    String siz = Integer.toHexString(size);
                    int[] isz = mhex(siz, true);
                    out.write(isz[0]);
                    out.write(isz[1]);
                    out.write(isz[2]);
                    out.write(isz[3]);
                    for (int[] buffer : buffers) {
                        if (buffer != null) {
                            for (int i : buffer) {
                                out.write(i);
                            }
                        }
                    }
                    buffers = null;
                }
            }
        }

        public void write(int i) {
            synchronized (this) {
                if (using == null) {
                    using = new int[1024];
                    usd = 0;
                }
                using[usd] = i;
                usd++;
                if (i == -1) {
                    int[] buf = new int[usd];
                    System.arraycopy(using, 0, buf, 0, usd);
                    using = buf;
                }
                if (usd == using.length) {
                    flush();
                }
            }
        }

        public int bufferSize() {
            if (buffers == null) {
                return 0;
            }
            synchronized (this) {
                int x = 0;
                for (int[] buf : buffers) {
                    if (buf != null) {
                        x += buf.length;
                    }
                }
                return x;
            }
        }

        public void flush() {
            System.err.println("Flush");
            synchronized (this) {
                if (buffers == null) {
                    buffers = new int[][]{using};
                    using = null;
                } else {
                    int[][] nex = new int[buffers.length + 1][];
                    System.arraycopy(buffers, 0, nex, 0, buffers.length);
                    nex[buffers.length] = using;
                    using = null;
                    buffers = nex;
                }
                usd = 0;
            }
        }
    }

    public static void main(String[] main) throws Throwable {
        Thread.currentThread();
        class I extends OutputStream {

            int[][] buffers = new int[0][];
            int[] using = new int[1024];
            int id = 0;

            public void write(int b) {
                using[id++] = b;
                if (id == using.length) {
                    flush();
                    id = 0;
                }
            }

            public void flush() {
                if (id == using.length) {
                    int[][] bf = new int[buffers.length + 1][];
                    System.arraycopy(buffers, 0, bf, 0, buffers.length);
                    bf[buffers.length] = using;
                    buffers = bf;
                    id = 0;
                } else {
                    int[] bfx = new int[id];
                    System.arraycopy(using, 0, bfx, 0, id);

                    int[][] bf = new int[buffers.length + 1][];
                    System.arraycopy(buffers, 0, bf, 0, buffers.length);
                    bf[buffers.length] = bfx;
                    buffers = bf;
                    id = 0;
                }
                using = new int[1024];
            }

            public void pr() {
                for (int[] a : buffers) {
                    for (int b : a) {
                        System.out.write(b);
                    }
                }
            }
        }
        I x = new I();
        ListOutputStream out = new ListOutputStream(x);
        PrintStream pr = new PrintStream(out);
        pr.println("Fuck You little man");
        pr.println("Shit");

        out.end();
        pr.println("Boy next Door");
        pr.println("Do you like want you see?");
        out.end();

        x.flush();
        x.pr();
        class U extends InputStream {

            int line = 0;
            int row = 0;

            public int read() {
                if (line == x.buffers.length) {
                    return -1;
                }
                int[] bx = x.buffers[line];
                if (row == bx.length) {
                    row = 0;
                    line++;
                    return read();
                }
                return bx[row++];
            }
        }
        U mx = new U();
        ListInputStream in = new ListInputStream(mx);
        while (in.hasNext()) {
            System.out.println("\n\nNext!!\n\n");
            in.open();
            while (true) {
                int l = in.read();
                if (l == -1) {
                    break;
                }
                System.out.write(l);
            }
        }
    }
}

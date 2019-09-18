/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: LineOutputStream.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.io;

import cn.mcres.gyhhy.MXLib.fcs.B3C;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class LineOutputStream extends OutputStream {

    public static String getLineSeparator() {
        return System.getProperty("line.separator");
    }

    private final MemoryInputStream mis = new MemoryInputStream();
    protected B3C cr;

    protected LineOutputStream() {
        this.lineSeparator = new byte[]{'\r', '\n'};
    }

    public LineOutputStream(B3C v) {
        this(v, null);
    }

    public LineOutputStream(B3C v, byte[] lineSeparator) {
        cr = v;
        if (lineSeparator == null) {
            lineSeparator = getLineSeparator().getBytes();
        }
        this.lineSeparator = lineSeparator;
    }

    @Override
    public void write(int b) throws IOException {
        mis.write(b);
        flush();
    }

    static int i;

    public static void main(String[] main) {
        LineOutputStream los = new LineOutputStream((a, b, c) -> {
//            System.out.println("SK-- ");
            System.out.append("AA - ").println(new String(a, b, c - b));
//                System.out.append(" --ON ");
//            System.out.append(" --OED ").append("" + i++).append("]\n");
        });
        PrintStream ps = new PrintStream(los);
        ps.println("FUCK YOU LITTLE MAN,A\nFUCKQ\nBOY NEXT DOOR.");
        ps.print("SHIT.");
        ps.append(" Loggin on ").append("Karlatemp's PC").append(".");
        ps.println();
        ps.println();
        ps.append("FAQ?");
        ps.println();
    }

    protected byte[] lineSeparator;

    protected int test(byte[] a, int p, int limit) {
        for (int i = 0; i < lineSeparator.length; i++) {
            int b = p + i;
            if (b < limit) {
                if (a[b] != lineSeparator[i]) {
                    return 0;
                }
            } else {
                return 0;
            }
        }
        return lineSeparator.length;
    }

    @SuppressWarnings({"deprecation"})
    public void flush() throws IOException {
        synchronized (mis.getReadingLock()) {
            synchronized (mis.getLock()) {
                byte[] d = mis.getContainer();
                int f = mis.position();
                final int r = f;
                int t = mis.limit();
                int lf = f;
                for (; f < t; f++) {
                    int app = test(d, f, t);
                    if (app != 0) {
                        callLine(d, lf, f);
                        lf = f + app;
                    }
                }
                mis.skip(lf - r);
            }
        }
    }

    public void write(byte[] bf) throws IOException {
        mis.write(bf);
        flush();
    }

    public void write(byte[] a, int b, int c) throws IOException {
        mis.write(a, b, c);
        flush();
    }

    public void callLine(byte[] line, int form, int limit) throws IOException {
        if (cr != null) {
            cr.c(line, form, limit);
        }
//        new String(line,form,limit);
    }
}

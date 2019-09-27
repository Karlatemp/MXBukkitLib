/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Version.java@author: karlatemp@vip.qq.com: 19-9-27 下午12:47@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.karlatemp.mxlib.util;


import java.util.Arrays;
import java.util.Objects;

/**
 * <a href="https://semver.org/">https://semver.org/</a>
 *
 * @author 32798
 */
public class Version implements Comparable<Version> {

    private static boolean isV(int c, int flags) {
        if (c >= '0' && c <= '9') {
            return true;
        }
        if ((flags & 0b00_01) != 0 && c == '.') {
            return true;
        }
        if ((flags & 0b00_10) != 0 && ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
            return true;
        }
        return false;
    }

    public static void checkThrow(String ver) {
        if (!check(ver)) {
            throw new UnsupportedOperationException(ver + " is not Semantic Version");
        }
    }

    public static boolean check(String ver) {
        SafeStringReader rd = new SafeStringReader(ver);
        if (!isV(rd.read(), 0)) {
            return false;
        }
        // read x,y,z(and more?)
        int nt;
        while (true) {
            if (!isV(nt = rd.read(), 1)) {
                break;
            }
        }
        if (nt == -1) {
            return true;
        }
        if (nt == '-') {
            while (true) {
                if (!isV(nt = rd.read(), 0b10)) {
                    break;
                }
            }
            if (nt == '.' || nt == -1) {
                while (true) {
                    if (!isV(nt = rd.read(), 0b01)) {
                        break;
                    }
                }
            } else {
                return false;
            }
        }
        if (nt == '+') {
            bb:
            while (true) {
                switch (rd.read()) {
                    case -1:
                        break bb;
                    case '\n':
                    case ' ':
                    case '-':
                    case '+':
                        return false;
                }
            }
            return true;
        }
        if (nt == -1) {
            return true;
        }
        return false;
    }

    public String getMetadata() {
        return metadata;
    }

    public static void main(String[] args) {
        System.out.println(
                extract(
                        "1.1.0.759-alpha.0.1+fcc"
                )
        );
    }

    private static int[] parse(String buff) {
        SafeStringReader rd = new SafeStringReader(buff);
        int lg = 1;
        while (true) {
            int i = rd.read();
            if (i == -1) {
                break;
            }
            if (!isV(i, 1)) {
                return null;
            }
            if (i == '.') {
                lg++;
            }
        }
        int[] bf = new int[lg];
        StringBuilder be = new StringBuilder();
        rd.reset();
        lg = 0;
        while (true) {
            int i = rd.read();
            if (i == '.' || i == -1) {
                bf[lg++] = Integer.parseInt(be.toString());
                be.delete(0, be.length());
                if (i == -1) {
                    break;
                }
                continue;
            }
            be.append((char) i);
        }
        return bf;
    }

    @SuppressWarnings("rawtypes")
    private static Comparable[] parse(StringBuilder buff) {
        SafeStringReader r = new SafeStringReader(buff.toString());
        buff.delete(0, buff.length());
        int lg = 1;
        while (true) {
            int n = r.read();
            if (n == -1) {
                break;
            }
            if (n == '.') {
                lg++;
            }
        }
        r.reset();
        Comparable[] rq = new Comparable[lg];
        lg = 0;
        boolean num = true;
        while (true) {
            int n = r.read();
            if (n == '.' || n == -1) {
                String s = buff.toString();
                buff.delete(0, buff.length());
                if (num) {
                    rq[lg++] = Integer.parseInt(s);
                } else {
                    rq[lg++] = s;
                }
                num = true;
            } else {
                buff.append((char) n);
                if (!isV(n, 0)) {
                    num = false;
                }
            }
            if (n == -1) {
                break;
            }
        }
        return rq;
    }

    @SuppressWarnings("NestedAssignment")
    public static Version extract(String ver) {
        checkThrow(ver);
        SafeStringReader rd = new SafeStringReader(ver);
        StringBuilder buffer = new StringBuilder();
        // read x,y,z(and more?)
        int nt;
        while (true) {
            if (!isV(nt = rd.read(), 1)) {
                break;
            }
            if (nt != -1) {
                buffer.append((char) nt);
            }
        }
        final int[] vers = parse(buffer.toString());
        @SuppressWarnings("rawtypes")
        Comparable[] subb = null;
        String metadata = null;
        if (nt == -1) {
            return new Version(vers, subb, metadata);
        }
        if (nt == '-') {
            buffer.delete(0, buffer.length());
            while (true) {
                nt = rd.read();
                if (nt == -1 || nt == '+') {
                    break;
                }
                buffer.append((char) nt);
            }
            subb = parse(buffer);
        }
        if (nt == '+') {
            buffer.delete(0, buffer.length());
            while (true) {
                nt = rd.read();
                if (nt == -1) {
                    break;
                }
                buffer.append((char) nt);
            }
            metadata = buffer.toString();
        }
        return new Version(vers, subb, metadata);
    }

    /**
     * Compare the version
     *
     * @param c The current version
     * @param d Be compare version
     * @return The compare result
     */
    public static int compare(String c, String d) {
        return extract(c).compareTo(extract(d));
    }

    private static int gt(int[] bd, int rr) {
        return rr > -1 && rr < bd.length ? bd[rr] : 0;
    }

    @SuppressWarnings("rawtypes")
    private static Comparable gt(Comparable[] tt, int rr) {
        return rr > -1 && rr < tt.length ? tt[rr] : null;
    }

    private final int[] vers;
    private final String metadata;
    @SuppressWarnings("rawtypes")
    private final Comparable[] apl;

    @SuppressWarnings("rawtypes")
    private Version(int[] vers, Comparable[] apl, String metadata) {
        this.vers = vers;
        this.apl = apl;
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return "Version[ver=" + Arrays.toString(vers) + ", sub=" + Arrays.toString(apl) + ", metadata=" + metadata + ", sem=" + this.toSemanticVersion() + ']';
    }

    /**
     * {@link int} / {@link String}
     *
     * @return {@link int}[] / {@link String}[]
     */
    public Object[] getPreReleaseVersion() {
        return apl.clone();
    }

    public String toSemanticVersion() {
        StringBuilder bui = new StringBuilder();
        boolean a = false;
        for (int i : vers) {
            if (a) {
                bui.append('.');
            }
            bui.append(i);
            a = true;
        }
        if (apl != null) {
            bui.append('-');
            a = false;
            for (Object o : apl) {
                if (a) {
                    bui.append('.');
                }
                a = true;
                bui.append(o);
            }
        }
        if (metadata != null) {
            bui.append('+').append(metadata);
        }
        return bui.toString();
    }

    public int[] getNormalVersion() {
        return vers.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Version) {
            Version n = (Version) o;
            return Objects.equals(n.apl, apl)
                    && Objects.equals(metadata, n.metadata)
                    && Objects.equals(n.vers, vers);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 2;
        hash = 27 * hash + Arrays.hashCode(this.vers);
        hash = 19 * hash + Objects.hashCode(this.metadata);
        hash = 13 * hash + Arrays.deepHashCode(this.apl);
        return hash;
    }

    @Override
    @SuppressWarnings({"rawtypes", "AccessingNonPublicFieldOfAnotherObject"})
    public int compareTo(Version o) {
        if (!equals(o)) {
            int[] ov = o.vers;
            int ed = Math.max(vers.length, ov.length);
            for (int i = 0; i < ed; i++) {
                int a = gt(vers, i);
                int b = gt(ov, i);
                if (a == b) {
                } else {
                    return a - b;
                }
            }
            Comparable[] os = o.apl;
            if (apl == null) {
                if (os == null) {
                    return String.valueOf(metadata).compareTo(String.valueOf(o.metadata));
                }
                return 1;
            }
            ed = Math.max(apl.length, os.length);
            for (int i = 0; i < ed; i++) {
                Comparable a = gt(apl, i);
                Comparable b = gt(os, i);
                if (a instanceof String || b instanceof String) {
                    int pp = String.valueOf(a).compareTo(String.valueOf(b));
                    if (pp != 0) {
                        return pp;
                    }
                } else {
                    int pp = (int) a - (int) b;
                    if (pp != 0) {
                        return pp;
                    }
                }
            }
            return String.valueOf(metadata).compareTo(String.valueOf(o.metadata));
        }
        return 0;
    }
}

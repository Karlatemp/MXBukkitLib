/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.http.handlers.data;

import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.StringTokenizer;

/**
 *
 * @author 32798
 */
public class Handler extends URLStreamHandler {

    public static String getCharset(String modes) {
        StringTokenizer st = new StringTokenizer(modes, ";");
        while (st.hasMoreTokens()) {
            String t = st.nextToken();
            if (t != null && !t.isEmpty()) {
                t = t.trim();
                if (t.toLowerCase().startsWith("charset")) {
                    t = t.substring(7).trim();
                    if (!t.isEmpty()) {
                        if (t.charAt(0) == '=') {
                            return t.substring(1).trim();
                        }
                    }
                }
            }
        }
        return null;
    }

    public static String[] cut(String s) {
        return cut(s, new String[2]);
    }

    public static String[] cut(String s, String[] ss) {
        int id = s.indexOf(',');
        if (id == -1) {
            ss[0] = null;
            ss[1] = s;
        } else {
            ss[0] = s.substring(0, id);
            ss[1] = s.substring(id + 1, s.length());
        }
        return ss;
    }

    @Override
    protected String toExternalForm(URL u) {
        return u.getRef();
    }

    @Override
    protected void parseURL(URL u, String spec, int start, int limit) {
        String code = spec.substring(start, limit);
//        System.out.format("FORMAT %s%n%s %s %s%n%s%n", u, spec, start, limit, code);
        String[] ct = cut(code);
        super.setURL(u, "data", null, 0, ct[0], null, ct[1], null, code);
    }

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        return new DataConnection(u, this);
    }

    @Override
    protected URLConnection openConnection(URL u, Proxy p) throws IOException {
        return this.openConnection(u);
    }

}

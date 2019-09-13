package cn.mcres.gyhhy.MXLib.http.handlers.rcon;

import cn.mcres.gyhhy.MXLib.http.DisConnectURLConnection;
import cn.mcres.gyhhy.MXLib.http.URLStreamManager;
import cn.mcres.gyhhy.MXLib.io.LineOutputStream;
import cn.mcres.gyhhy.MXLib.io.MemoryInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Handler extends URLStreamHandler {

    public static void main(String[] args) throws Throwable {
        URLStreamManager.registerHandler("rcon", new Handler());
        URL u = new URL("rcon://rcon.minecraft.example.com:25575/RCONPASSWD");

        System.out.println(u);
    }

    @Override
    protected int getDefaultPort() {
        return 25575;
    }

    @Override
    protected String toExternalForm(URL u) {
        return u.getProtocol() + "://" + u.getRef();
    }

    private String trim(String p) {
        f:
        while (!p.isEmpty()) {
            switch (p.charAt(0)) {
                case ' ':
                case '/':
                case '\\': {
                    p = p.substring(1);
                    break;
                }
                default:
                    break f;
            }
        }
        return p;
    }

    private String[] www(String pp) {
        pp = trim(pp);
        int a = pp.indexOf('/');
        int b = pp.indexOf('\\');
        String host = pp;
        String pwd = null;
        if (a == -1) {
            if (b == -1) {
            } else {
                host = pp.substring(0, b++);
                pwd = pp.substring(b);
            }
        } else if (b == -1) {
            host = pp.substring(0, a++);
            pwd = pp.substring(a);
        } else if (a > b) {
            host = pp.substring(0, b++);
            pwd = pp.substring(b);
        } else {
            host = pp.substring(0, a++);
            pwd = pp.substring(a);
        }
        String port = null;
        String[] datas = {null, null, null, host};
        a = host.indexOf(':');
        if (a != -1) {
            pp = host;
            host = pp.substring(0, a++);
            port = pp.substring(a);
        }
        datas[0] = host;
        datas[1] = port;
        datas[2] = pwd;
        return datas;
    }

    @Override
    protected void parseURL(URL u, String spec, int start, int limit) {
        String spl = spec.substring(start, limit);
        String[] datas = www(spl);
//        System.out.println(Arrays.toString(datas));
        int port = getDefaultPort();
        if (datas[1] != null) {
            port = Integer.decode(datas[1]);
        }
        super.setURL(u,
                u.getProtocol(), datas[0], port,
                null, datas[2], null, null, datas[3]);
//        spl = spl.substring(spl.indexOf(':'));
//        System.out.println(spl);
//        super.parseURL(u, spec, start, limit);
    }

    @Override
    protected URLConnection openConnection(URL u, Proxy p) throws IOException {
        return openConnection(u);
    }

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        return new RconConnection(u);
    }

}

/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: RconConnection.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.http.handlers.rcon;

import cn.mcres.gyhhy.MXLib.fcs.B3C;
import cn.mcres.gyhhy.MXLib.http.DisConnectURLConnection;
import cn.mcres.gyhhy.MXLib.io.LineOutputStream;
import cn.mcres.gyhhy.MXLib.io.MemoryInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;

public class RconConnection extends DisConnectURLConnection {

    private final int port;
    private final String host;
    private String pwd;
    private RconClient client;
    private MemoryInputStream buffer;
    private LineOutputStream los;
    private boolean bb;
    private byte[] line_separator;

    RconConnection(URL u) throws IOException {
        super(u);
        this.host = u.getHost();
        this.port = u.getPort();
        this.pwd = u.getUserInfo();
    }

    public MemoryInputStream getInputStream() throws IOException {
        connect();
        return buffer;
    }

    @Override
    public void connect() throws IOException {
        if (client == null) {
            this.client = RconClient.open(host, port, pwd);
            connected = true;
            if (cwt != null) {
                client.setCharset(cwt);
            }
            buffer = new MemoryInputStream(bb);
            los = new LineOutputStream(new B3C() {
                @Override
                public synchronized void c(byte[] arr, int f, int l) throws IOException {
                    int s = l - f;
                    if (s > 0) {
                        System.out.println("Pass command: " + new String(arr, f, s));
                        buffer.write(client.send_(RconClient.TYPE_COMMAND, arr, f, s));
                        buffer.flush();
                    }
                }
            });
        }
    }
    Charset cwt;

    @Override
    public LineOutputStream getOutputStream() throws IOException {
        connect();
        return los;
    }

    @Override
    public void disconnect() throws IOException {
        client.close();
        buffer.close();
    }

    private void pre(String a, String b) {
        switch (a.toLowerCase()) {
            case "line.separator":
            case "lineseparator":
            case "ls":
            case "line-separator": {
                if (cwt == null) {
                    line_separator = b.getBytes(StandardCharsets.UTF_8);
                } else {
                    line_separator = b.getBytes(cwt);
                }
                break;
            }
            case "passwd":
            case "password":
            case "pwd":
            case "passcode": {
                pwd = b;
                break;
            }
            case "blocking": {
                this.bb = Boolean.parseBoolean(b);
                break;
            }
            case "content-type": {
                StringTokenizer st = new StringTokenizer(b, ";");
                while (st.hasMoreTokens()) {
                    String t = st.nextToken();
                    int aw = t.indexOf('=');
                    if (aw != -1) {
                        if ("charset".equals(t.substring(0, aw++).trim().toLowerCase())) {
                            cwt = Charset.forName(t.substring(aw).trim());
                        }
                    }
                }
                break;
            }
        }
    }

    @Override
    public void addRequestProperty(String key, String value) {
        super.addRequestProperty(key, value); //To change body of generated methods, choose Tools | Templates.
        pre(key, value);
    }

    @Override
    public void setRequestProperty(String key, String value) {
        super.setRequestProperty(key, value); //To change body of generated methods, choose Tools | Templates.
        pre(key, value);
    }

    public RconClient getClient() {
        return client;
    }

    @Override
    public String getContentEncoding() {
        if (client != null) {
            return client.getCharset().name();
        }
        if (cwt != null) {
            return cwt.name();
        }
        return null;
    }

}

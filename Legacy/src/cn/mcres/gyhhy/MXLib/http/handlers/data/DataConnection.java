/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.http.handlers.data;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import cn.mcres.gyhhy.MXLib.encode.Base64Actuator;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.StringJoiner;
import java.util.StringTokenizer;

/**
 *
 * @author 32798
 */
public class DataConnection extends URLConnection {

    private String mode;
    private byte[] bytes;
    private String charset;

    private static String awwixn(String mode, boolean[] cb) {
        StringTokenizer st = new StringTokenizer(mode, ";");
        StringJoiner sj = new StringJoiner(";");
        cb[0] = false;
        while (st.hasMoreTokens()) {
            String next = st.nextToken();
            if (next != null) {
                if (next.trim().equalsIgnoreCase("base64")) {
                    cb[0] = true;
                    continue;
                }
                sj.add(next);
            }
        }
        return sj.toString();
    }

    public DataConnection(URL u, Handler h) {
        super(u);
        String mode = u.getAuthority();
        String data = u.getPath();
        byte[] bytes;
        if (mode == null) {
            throw new NullPointerException();
        }
        boolean[] cccc = new boolean[1];
        mode = awwixn(mode, cccc);
        if (cccc[0]) {
            bytes = Base64Actuator.getInstance().decode(data);
        } else {
            String charset = Handler.getCharset(mode);
            if (charset != null && !charset.isEmpty()) {
                try {
                    bytes = data.getBytes(charset);
                    this.charset = charset;
                } catch (UnsupportedEncodingException thr) {
                    bytes = data.getBytes(UTF_8);
                    this.charset = "utf8";
                }
            } else {
                bytes = data.getBytes(UTF_8);
            }
        }
        this.mode = mode;
        this.bytes = bytes;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(bytes);
    }

    public byte[] getDatas() {
        return bytes.clone();
    }

    @Override
    public long getContentLengthLong() {
        return bytes.length;
    }

    @Override
    public int getContentLength() {
        return bytes.length;
    }

    @Override
    public String getContentType() {
        return mode;
    }

    @Override
    public String getContentEncoding() {
        return charset;
    }

    @Override
    public void connect() throws IOException {
    }

}

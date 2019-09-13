/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.http.handlers.base64;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import cn.mcres.gyhhy.MXLib.encode.Base64Actuator;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 *
 * @author 32798
 */
public class Base64Connection extends URLConnection {

    private byte[] bytes;

    public Base64Connection(URL u) {
        super(u);
        String a = u.getAuthority();
        if (a == null) {
            a = u.getPath();
        } else {
            a += u.getPath();
        }
        setData(a);
    }

    public Base64Connection(String base64) {
        super(null);
        setData(base64);
    }

    public void setData(String base) {
        Base64Actuator ua = Base64Actuator.getInstance();
        this.bytes = ua.decode(base);
    }

    public void setData(byte[] data) {
        this.bytes = Base64Actuator.getInstance().decode(data);
    }

    public void setDatas(byte[] data) {
        bytes = data;
    }

    public byte[] getDataDecoded() {
        return bytes;
    }

    @Override
    public void setDoOutput(boolean dooutput) {
        if (dooutput == true) {
            throw new UnsupportedOperationException("Base64Connection cannot set output.");
        }
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(bytes);
    }

    @Override
    public void setDoInput(boolean doinput) {
        if (doinput == true) {
            throw new UnsupportedOperationException("Base64Connection cannot set non input.");
        }
    }

    public boolean isUseCaches() {
        return false;
    }

    public boolean isDoOutput() {
        return true;
    }

    public boolean isDoInput() {
        return false;
    }

    public boolean isConnected() {
        return true;
    }

    @Override
    public void connect() throws IOException {
    }
}

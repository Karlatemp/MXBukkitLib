/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.http.handlers.base64;

import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class Handler extends URLStreamHandler {

    @Override
    protected String toExternalForm(URL u) {
        return "base64:" + u.getAuthority() + u.getPath();
    }

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        return new Base64Connection(u);
    }

    @Override
    protected URLConnection openConnection(URL u, Proxy p) throws IOException {
        return openConnection(u);
    }

}

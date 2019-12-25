/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: DisConnectURLConnection.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.http;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author 32798
 */
public abstract class DisConnectURLConnection extends URLConnection implements Closeable, AutoCloseable {

    protected DisConnectURLConnection(URL u) {
        super(u);
    }

    public void disconnect() throws IOException {
    }

    public void close() throws IOException {
        disconnect();
    }
}

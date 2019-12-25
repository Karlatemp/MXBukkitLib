/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Tester.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.http;

import cn.mcres.gyhhy.MXLib.encode.Base64Actuator;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;

/**
 *
 * @author 32798
 */
public class Tester {

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

    public static void main(String[] args) throws MalformedURLException, IOException {
        URLStreamManager.load();
        PrintStream out = System.out;
        write(new URL("data:text/html,FuckYou Little Man\nFAQ").openStream(), out);
    }

    public static void write(InputStream io, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        while (true) {
            int leng = io.read(buffer);
            if (leng == -1) {
                break;
            }
            out.write(buffer, 0, leng);
        }
    }

}

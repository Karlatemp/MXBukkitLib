/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ImageUtil.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.image;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
 * Create: 2019-01-25, Added in version 0.5.2
 *
 * @author 32798
 */
public class ImageUtil {

    public static boolean isImage(URL url, int timeout) throws IOException {
        try (InputStream io = openHttp(url, timeout)) {
            return isImage(getIIS(io));
        }
    }

    public static boolean isImage(URL url) throws IOException {
        try (InputStream io = openHttp(url)) {
            return isImage(getIIS(io));
        }
    }

    public static boolean isImage(ImageInputStream iis) {
        return getReader(iis) != null;
    }

    private static void preHttp(URLConnection connect) {
        if (connect instanceof HttpURLConnection) {
            HttpURLConnection http = (HttpURLConnection) connect;
            http.setRequestProperty("accept", "*/*");
            http.setRequestProperty("connection", "Keep-Alive");
            http.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        }
    }

    public static InputStream openHttp(URL url, int timeout) throws IOException {
        URLConnection connect = url.openConnection();
        preHttp(connect);
        connect.setConnectTimeout(timeout);
        connect.setReadTimeout(timeout);
        return connect.getInputStream();
    }

    public static InputStream openHttp(URL url) throws IOException {
        URLConnection connect = url.openConnection();
        preHttp(connect);
        return connect.getInputStream();
    }

    public static FileInputStream openFile(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            return null;
        }
    }

    public static ImageInputStream getIIS(InputStream stream) throws IOException {
        return ImageIO.createImageInputStream(stream);
    }

    public static ImageReader getReader(ImageInputStream iis) {
        synchronized (iis) {
            Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
            if (!iter.hasNext()) {
                return null;
            }
            return iter.next();
        }
    }

    @Deprecated
    public static BufferedImage getTextureHead(ImageInputStream iis) throws IOException {
        return getTextureAvatar(iis);
    }

    /**
     * @version 1.11
     */
    public static BufferedImage getTextureAvatar(ImageInputStream iis) throws IOException {
        try (final ImageInputStream iis_ = iis) {
            Iterator<ImageReader> iter = ImageIO.getImageReaders(iis_);
            if (!iter.hasNext()) {
                return null;
            }

            ImageReader reader = iter.next();
            return getTextureAvatar(reader, iis_);
        }
    }

    /**
     * @param reader The reader
     * @param input The input Stream, Can be null
     * @version 1.11
     */
    public static BufferedImage getTextureAvatar(ImageReader reader, Object input) throws IOException {
        synchronized (reader) {
            if (input != null) {
                reader.setInput(input, true);
            }
            int width = reader.getWidth(0);
            int size = width / 8;
            //设置输入流

            //读取参数
            ImageReadParam param = reader.getDefaultReadParam();

            //创建要截取的矩形范围
//        Rectangle rect = new Rectangle(size, size, size, size);
            //设置截取范围参数
            param.setSourceRegion(new Rectangle(size, size, size, size));

            //读取截图数据
            BufferedImage bi = reader.read(0, param);
            //设置截取范围参数
            param.setSourceRegion(new Rectangle(size * 5, size, size, size));

            BufferedImage bx = reader.read(0, param);
            Graphics2D gp = bi.createGraphics();
            gp.drawImage(bx, 0, 0, null);
            gp.dispose();
            return bi;
        }
    }
}

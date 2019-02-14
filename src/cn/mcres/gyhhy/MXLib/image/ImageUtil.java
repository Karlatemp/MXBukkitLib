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

    public static InputStream openHttp(URL url) throws IOException {
        URLConnection connect = url.openConnection();
        if (connect instanceof HttpURLConnection) {
            HttpURLConnection http = (HttpURLConnection) connect;
            http.setRequestProperty("accept", "*/*");
            http.setRequestProperty("connection", "Keep-Alive");
            http.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        }
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

    public static BufferedImage getTextureHead(ImageInputStream iis) throws IOException {
        try (final ImageInputStream iis_ = iis) {
            Iterator<ImageReader> iter = ImageIO.getImageReaders(iis_);
            if (!iter.hasNext()) {
                return null;
            }

            ImageReader reader = iter.next();
            synchronized (reader) {
                reader.setInput(iis_, true);
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
}


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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import cn.mcres.gyhhy.MXLib.http.WebHelper;
import cn.mcres.gyhhy.MXLib.image.ImageUtil;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author 32798
 */
public class MTest {

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
    public static void a() throws IOException{
        // 打开输入流
        InputStream stream = openHttp(WebHelper.url("*"));
        // 获取图像输入流
        ImageInputStream iis =getIIS(stream);
        // 获取皮肤头部图像
        BufferedImage head = ImageUtil.getTextureHead(iis);
    }
    public static void main(String[] args) throws IOException {
        File file = new File(
                "F:\\GYHHY\\mod\\abc\\MCreator 1.5.9\\external\\mcskin3d\\Skins\\aaa.png");
        ImageInputStream iis = getIIS(openFile(file));
        Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
        if (!iter.hasNext()) {
            return;
        }

        ImageReader reader = iter.next();
        reader.setInput(iis, true);
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

        // 保存图片
        ImageIO.write(bi, "png", new File("G:\\Tempx\\app\\awa.png"));
    }

}

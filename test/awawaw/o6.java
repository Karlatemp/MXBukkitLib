/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package awawaw;

import java.io.File;

import cn.mcres.gyhhy.MXLib.Base64;
import cn.mcres.gyhhy.MXLib.bukkit.profile.Profile;
import cn.mcres.gyhhy.MXLib.bukkit.profile.ProfileHelper;
import cn.mcres.gyhhy.MXLib.bukkit.profile.Textures;
import cn.mcres.gyhhy.MXLib.image.ImageUtil;
import org.bukkit.entity.Player;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class o6 {

    public static String getHeadUrl(Player player) {
        //Player player = null;//事先获取的玩家对象
        Profile profile = ProfileHelper.getPlayerProfile(player); // 获取玩家的 Profile 属性
        if (profile != null) {
            Textures textures = profile.getTextures(); // 皮肤组对象
            if (textures != null) {
                Textures.Texture skin = textures.getSkin(); // 皮肤对象(皮肤信息)
                if (skin != null) {
                    try {
                        URL url = skin.getURL(); //获取连接
                        ImageInputStream iis = ImageUtil.getIIS( // 获取 IIS
                                ImageUtil.openHttp(url) // 打开输入流
                        );
                        Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
                        if (!iter.hasNext()) {
                            ImageReader reader = iter.next();
                            int width;
                            synchronized (reader) {
                                reader.setInput(iis, true);
                                width = reader.getWidth(0);
                            }
                            if (width > 256) {
                                return "[local]team_head.png";
                            }
                        }
                        String base64 = toBase64( // 转base64
                                ImageUtil.getTextureHead( // 获取皮肤头部
                                        ImageUtil.getIIS( // 获取 IIS
                                                ImageUtil.openHttp(url) // 打开输入流
                                        )
                                )
                        );
                        return base64;
                    } catch (IOException ex) {
                    }
                }
            }
        }
        return "[local]team_head.png";
    }

    private static class A extends OutputStream {

        private byte[] buff;
        private int index;
        private List<byte[]> bytes;

        public A() {
            this.buff = new byte[1024];
            bytes = new ArrayList<>();
            index = 0;
        }

        public void flush() {
            byte[] nw = new byte[index];
            System.arraycopy(buff, 0, nw, 0, index);
            bytes.add(nw);
            index = 0;
        }

        @Override
        public void write(int b) throws IOException {
            if (index == buff.length) {
                flush();
            }
            buff[index] = (byte) b;
            index++;
        }

        public byte[] gt() {
            int size = index;
            for (byte[] b : bytes) {
                size += b.length;
            }
            byte[] buffer = new byte[size];
            int pos = 0;
            for (byte[] b : bytes) {
                System.arraycopy(b, 0, buffer, pos, b.length);
                pos += b.length;
            }
            if (index > 0) {
                System.arraycopy(buff, 0, buffer, pos, index);
            }
            return buffer;
        }
    }

    public static String toBase64(BufferedImage img) throws IOException {
        A buff = new A();
        ImageIO.write(img, "jpeg", buff);
        byte[] data = buff.gt();
        return "data:image/jpeg;base64," + Base64.encode(data);
    }

    public static String run() throws IOException {
        FileInputStream stream = ImageUtil.openFile(new File("F:\\GYHHY\\mod\\abc\\MCreator 1.5.9\\external\\mcskin3d\\Skins\\aaa.png"));
        ImageInputStream iis = ImageUtil.getIIS(stream);
        BufferedImage buff = ImageUtil.getTextureHead(iis);
        return toBase64(buff);
//        BASE64Encoder encoder = new BASE64Encoder();
//        return encoder.encode(data);
    }

    public static void main(String[] args) throws Exception {
        File file = new File("F:\\GYHHY\\mod\\abc\\MCreator 1.5.9\\external\\mcskin3d\\Skins\\test.png");
        String req = "";
        ImageInputStream iis = ImageUtil.getIIS( // 获取 IIS
                ImageUtil.openFile(file) // 打开输入流
        );
        Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
        if (!iter.hasNext()) {
            ImageReader reader = iter.next();
            int width;
            synchronized (reader) {
                reader.setInput(iis, true);
                width = reader.getWidth(0);
            }
            if (width > 256) {
                req = "[local]team_head.png";
            }
        }
        req = toBase64( // 转base64
                ImageUtil.getTextureHead( // 获取皮肤头部
                        iis
                )
        );
        System.out.println(req);
        System.out.println(req.length());
    }

}

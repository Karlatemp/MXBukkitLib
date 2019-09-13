/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: BinOutputStream.java@author: karlatemp@vip.qq.com: 19-9-11 下午1:12@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.io;

import java.io.DataOutputStream;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class BinOutputStream extends OutputStream {

    public static interface BinWriter {

        int getLength() throws IOException;

        void write(BinOutputStream out) throws IOException;
    }

    public static interface BytesGetter {

        byte[] getBytes() throws IOException;
    }

    private final OutputStream bin;

    public void write(BinWriter wr) throws IOException {
        writeInteger(wr.getLength());
        wr.write(this);
    }

    public void writeImage(RenderedImage im, String formatName) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        ImageIO.write(im, formatName, out);
        writeBytes(out.toByteArray());
    }

    public void writeFromInputStream(InputStream io) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        byte[] buff = new byte[1024];
        while (true) {
            int leng = io.read(buff);
            if (leng == -1) {
                break;
            }
            out.write(buff, 0, leng);
        }
        writeBytes(out.toByteArray());
    }

    public void write(BytesGetter gt) throws IOException {
        byte[] bs = gt.getBytes();
        writeBytes(bs);
    }

    @Override
    public void close() throws IOException {
        bin.close();
    }

    public void writeBoolean(boolean b) throws IOException {
        writeByte(b ? (byte) 1 : (byte) 0);
    }

    public BinOutputStream(OutputStream bin) {
        this.bin = bin;
    }

    @Override
    public void write(int b) throws IOException {
        bin.write(b);
    }

    @Override
    public void flush() throws IOException {
        bin.flush(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void write(byte[] b) throws IOException {
        bin.write(b); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        bin.write(b, off, len); //To change body of generated methods, choose Tools | Templates.
    }

    public void writeString(String string) throws IOException {
        writeString(string, StandardCharsets.UTF_8);
    }

    public void writeBytes(byte[] bytes) throws IOException {
        writeInteger(bytes.length);
        write(bytes);
    }

    public void writeString(String string, Charset charset) throws IOException {
        writeBytes(string.getBytes(charset));
    }

    @SuppressWarnings("FinalMethod")
    public final void writeInt(int i) throws IOException {
        writeInteger(i);
    }

    public void writeLong(long l) throws IOException {
        writeInteger((int) ((l >> 32) & 0xFFFFFFFF));
        writeInteger((int) (l & 0xFFFFFFFF));
    }

    public void writeShort(short s) throws IOException {
        writeInteger(Short.toUnsignedInt(s));
    }

    public void writeDouble(double d) throws IOException {
        writeLong(Double.doubleToLongBits(d));
    }

    public void writeInteger(int i) throws IOException {
        write((byte) ((i >> 24) & 0xff));
        write((byte) ((i >> 16) & 0xff));
        write((byte) ((i >> 8) & 0xff));
        write((byte) ((i) & 0xff));
    }

    public void writeByte(byte b) throws IOException {
        write(b);
    }

    public static void main(String[] test) throws Exception {
        File out = new File("E:\\BaiduNetdiskDownload\\V-Ray 4.10.02\\fa.bin");
        out.createNewFile();
        FileOutputStream st = new FileOutputStream(out);
        BinOutputStream b = new BinOutputStream(st);
        {
            b.writeBoolean(false);
            b.writeBoolean(true);
            b.writeString("Fuck you litte man.");
            b.writeInt(233);
            b.writeLong(3279826484L);
            b.writeImage(ImageIO.read(new File("C:\\Users\\32798\\Desktop\\800x800.png")), "png");
            b.writeImage(ImageIO.read(new File("C:\\Users\\32798\\Desktop\\ax.jpg")), "jpg");
        }
        b.close();
        BinInputStream i = new BinInputStream(new FileInputStream(out));
        {
            System.out.println(i.readBoolean());
            System.out.println(i.readBoolean());
            {
                InputStream is = i.openStream();
                System.out.println(is.available());
                while (true) {
                    int read = is.read();
                    if (read == -1) break;
                    System.out.write(read);
                }
                is.close();
                System.out.println();
//                System.out.println(i.readString());
            }
            System.out.println(i.readInt());
            System.out.println(i.readLong());
            ImageIO.write(i.readImage(), "png", new File("E:\\BaiduNetdiskDownload\\V-Ray 4.10.02\\im.png"));
            {
                int lg = i.readInt();
                System.out.println("EX " + i.available());
                System.out.println("LG " + lg);
                InputStream is = i.openStream(lg);
                System.out.println("EO " + is.available());
                ImageIO.write(ImageIO.read(is), "jpg", new File("E:\\BaiduNetdiskDownload\\V-Ray 4.10.02\\im2.png"));
            }
        }
        i.close();
    }
}

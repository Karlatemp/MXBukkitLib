/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ZipDecompressorFactory.java@author: karlatemp@vip.qq.com: 2019/12/27 下午7:25@version: 2.0
 */
package cn.mcres.karlatemp.mxlib.data.jimage;


import java.io.IOException;
import java.util.Properties;

/**
 *
 * ZIP decompressor factory
 *
 * @implNote This class needs to maintain JDK 8 source compatibility.
 *
 * It is used internally in the JDK to implement jimage/jrtfs access,
 * but also compiled and delivered as part of the jrtfs.jar to support access
 * to the jimage file provided by the shipped JDK by tools running on JDK 8.
 */
public final class ZipDecompressorFactory extends ResourceDecompressorFactory {
    public static final String NAME = "zip";
    public ZipDecompressorFactory() {
        super(NAME);
    }

    @Override
    public ResourceDecompressor newDecompressor(Properties properties)
            throws IOException {
        return new ZipDecompressor();
    }
}

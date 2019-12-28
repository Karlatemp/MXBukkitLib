/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ResourceDecompressor.java@author: karlatemp@vip.qq.com: 2019/12/27 下午7:26@version: 2.0
 */
package cn.mcres.karlatemp.mxlib.data.jimage;

/**
 *
 * JLink Image Decompressor.
 *
 * @implNote This class needs to maintain JDK 8 source compatibility.
 *
 * It is used internally in the JDK to implement jimage/jrtfs access,
 * but also compiled and delivered as part of the jrtfs.jar to support access
 * to the jimage file provided by the shipped JDK by tools running on JDK 8.
 */
public interface ResourceDecompressor {

    public interface StringsProvider {
        public String getString(int offset);
    }
    /**
     * Decompressor unique name.
     * @return The decompressor name.
     */
    public String getName();

    /**
     * Decompress a resource.
     * @param strings The String provider
     * @param content The resource content
     * @param offset Resource content offset
     * @param originalSize Uncompressed size
     * @return Uncompressed resource
     * @throws Exception
     */
    public byte[] decompress(StringsProvider strings, byte[] content, int offset,
                             long originalSize) throws Exception;
}

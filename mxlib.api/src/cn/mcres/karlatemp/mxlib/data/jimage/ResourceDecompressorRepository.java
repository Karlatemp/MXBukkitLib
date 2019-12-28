/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ResourceDecompressorRepository.java@author: karlatemp@vip.qq.com: 2019/12/27 下午7:25@version: 2.0
 */
package cn.mcres.karlatemp.mxlib.data.jimage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 * JLink Decompressors. All decompressors must be registered in the static
 * initializer of this class.
 *
 * @implNote This class needs to maintain JDK 8 source compatibility.
 *
 * It is used internally in the JDK to implement jimage/jrtfs access,
 * but also compiled and delivered as part of the jrtfs.jar to support access
 * to the jimage file provided by the shipped JDK by tools running on JDK 8.
 */
public final class ResourceDecompressorRepository {

    private ResourceDecompressorRepository() {
    }

    private static final Map<String, ResourceDecompressorFactory> factories = new HashMap<>();

    static {
        registerReaderProvider(new ZipDecompressorFactory());
        registerReaderProvider(new StringSharingDecompressorFactory());
    }

    /**
     * Build a new decompressor for the passed name.
     * @param properties Contains plugin configuration.
     * @param name The plugin name to build.
     * @return A decompressor or null if not found
     * @throws IOException
     */
    public static ResourceDecompressor newResourceDecompressor(Properties properties,
                                                               String name) throws IOException {

        ResourceDecompressorFactory fact = factories.get(name);
        if (fact != null) {
            return fact.newDecompressor(properties);
        }
        return null;
    }

    private static void registerReaderProvider(ResourceDecompressorFactory factory) {
        factories.put(factory.getName(), factory);
    }


}

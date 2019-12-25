/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ResourceLoader.java@author: karlatemp@vip.qq.com: 19-9-26 下午9:27@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tools;

import cn.mcres.karlatemp.mxlib.annotations.ProhibitBean;
import cn.mcres.karlatemp.mxlib.annotations.ProhibitType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;

/**
 * A Resource Loader.
 */
@ProhibitBean(ProhibitType.ONLY_CURRENT)
public interface ResourceLoader {
    /**
     * This is for lambda.
     * <p>
     * If you are calling {@link #found(String, ClassLoader)} directly, please do this
     * {@code public InputStream found0(@NotNull String path, @Nullable ClassLoader loader){return null;}}
     * <p>
     * Do not call {@link #found(String, ClassLoader)} in this method.
     * Otherwise it will lead to an infinite loop
     * </p>
     * <p>
     * Use found if it is used externally, do <b>not</b> call this method.
     * <pre>
     * {@code
     * ResourceLoader loader = ....;
     * // loader.found0(......);
     * InputStream res = loader.found("META-INF/MANIFEST.MF", ClassLoader.getSystemClassLoader());
     * }
     * </pre>
     * </p>
     * <p>
     * Call specification: Never use {@link #found0(String, ClassLoader)}</p>
     *
     * @param path   The path of need found.
     * @param loader The class loader using
     * @return The resource
     * @see #found(String, ClassLoader)
     */
    @Nullable
    @Contract(pure = true)
    InputStream found0(@NotNull String path, @Nullable ClassLoader loader);

    /**
     * Find the resource with path.
     * <p>
     * Call specification: Never use {@link #found0(String, ClassLoader)}
     *
     * @param path   The resource path.
     * @param loader ResourceLoader?
     * @return The resource founded.
     */
    @Nullable
    @Contract(pure = true)
    default InputStream found(@NotNull String path, @Nullable ClassLoader loader) {
        InputStream ref = found0(path, loader);
        if (ref != null) return ref;
        if (loader == null) return ClassLoader.getSystemResourceAsStream(path);
        InputStream is = loader.getResourceAsStream(path);
        if (is == null) is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        return is;
    }
}

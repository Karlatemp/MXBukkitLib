/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: FileListener.java@author: karlatemp@vip.qq.com: 19-11-15 下午5:34@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.files;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;

/**
 * A File listener. call by provider.
 */
public interface FileListener {
    /**
     * Called when the listening file created.
     *
     * @param file The created file.
     * @throws IOException IO Exception
     */
    default void onFileCreate(@NotNull Path file) throws IOException {
    }

    /**
     * Called when the listening dir created.
     *
     * @param file The created dir.
     * @throws IOException IO Exception
     */
    default void onDirCreate(@NotNull Path dir) throws IOException {
    }

    /**
     * Called when the listening file/dir deleted.
     *
     * @param file The deleted file/dir.
     * @throws IOException IO Exception
     */
    default void onDelete(@NotNull Path fileOrDir) throws IOException {
    }

    /**
     * Called when the listening file modified.
     *
     * @param file    The modified file.
     * @param newSize The current file size
     * @param oldSize before changed's size
     * @throws IOException IO Exception
     */
    default void onFileChange(@NotNull Path file, long oldSize, long newSize) throws IOException {
    }

    /**
     * Call when the listening dir created a new sub file.
     *
     * @param parent The listening dir
     * @param file   The created sub file
     * @throws IOException IO Exception
     */
    default void onSubFileCreate(@NotNull Path parent, @NotNull Path file) throws IOException {
    }

    /**
     * Call when the listening dir created a new sub dir.
     *
     * @param parent The listening dir
     * @param file   The created sub dir
     * @throws IOException IO Exception
     */
    default void onSubDirCreate(@NotNull Path parent, @NotNull Path dir) throws IOException {
    }

    /**
     * Call when the listening dir deleted a file or dir.
     *
     * @param parent The listening dir
     * @param file   The deleted sub file/dir.
     * @throws IOException IO Exception
     */
    default void onSubFileDelete(@NotNull Path parent, @NotNull Path fileOrDir) throws IOException {
    }
}

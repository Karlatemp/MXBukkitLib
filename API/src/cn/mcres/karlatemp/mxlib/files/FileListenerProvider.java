/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: FileProvider.java@author: karlatemp@vip.qq.com: 19-11-15 下午7:39@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.files;

import cn.mcres.karlatemp.mxlib.annotations.Bean;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

/**
 * The FileListenerProvider.
 * <p>
 * Use like:
 * <pre>{@code
 *  FileListenerProvider provider = MXBukkitLib.getBeanManager().getBeanNonNull(FileListenerProvider.class);
 *  provider.register(new File("plugins/MyPlugin/configs").toPath(),new ConfigsFileListener());
 * }</pre>
 */
@Bean
public interface FileListenerProvider {
    void register(@NotNull Path path, @NotNull FileListener listener);

    boolean unregister(@Nullable Path path, @NotNull FileListener listener);

    void doTick();

    void unregisterAll(Object sudo_token);
}

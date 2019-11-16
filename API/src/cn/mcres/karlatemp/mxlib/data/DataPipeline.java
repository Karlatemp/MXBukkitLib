/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: DataPipeline.java@author: karlatemp@vip.qq.com: 19-11-15 下午11:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.data;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

public interface DataPipeline {

    DataPipeline addFirst(@NotNull String namespace, @NotNull DataHandler... handlers);

    DataPipeline addLast(@NotNull String namespace, @NotNull DataHandler... handlers);

    DataPipeline addBefore(@NotNull String namespace, @NotNull String target, @NotNull DataHandler... handlers);

    DataPipeline addAfter(@NotNull String namespace, @NotNull String target, @NotNull DataHandler... handlers);

    DataPipeline remove(@NotNull String namespace);

    DataPipeline remove(@NotNull DataHandler handler);

    <T extends DataHandler> DataPipeline remove(@NotNull Class<T> type);

    DataPipeline replace(@NotNull String namespace, @NotNull String target, @NotNull DataHandler... handlers);

    DataProcessContext createContext();
}

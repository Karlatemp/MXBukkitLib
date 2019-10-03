/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: IClassScanner.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tools;

import cn.mcres.karlatemp.mxlib.annotations.Bean;
import cn.mcres.karlatemp.mxlib.exceptions.ScanException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URL;
import java.util.List;

/**
 * 类搜索器，在BeanManager获取
 */
@Bean
public interface IClassScanner {
    @Contract
    @NotNull
    List<String> scan(@NotNull File file, @NotNull List<String> list) throws ScanException;

    @Contract
    @NotNull
    List<String> scan(@NotNull Class c, @NotNull List<String> list) throws ScanException;

    @Contract
    @NotNull
    List<String> scan(@NotNull URL url, @NotNull List<String> list) throws ScanException;
}

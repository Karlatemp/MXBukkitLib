/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ResourceInjectTest.java@author: karlatemp@vip.qq.com: 19-9-27 下午5:18@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tester.plugintester;

import cn.mcres.karlatemp.mxlib.annotations.Resource;
import cn.mcres.karlatemp.mxlib.tools.IMemberScanner;

public class ResourceInjectTest {
    @Resource
    private static IMemberScanner scanner;

    private static void $init() {
        PluginTester.plugin.getLogger().info("[ResourceInjectTest] $init(): " + scanner);
    }
}

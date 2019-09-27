/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: TestingAutoConfig.java@author: karlatemp@vip.qq.com: 19-9-27 下午5:17@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tester.plugintester;

import cn.mcres.karlatemp.mxlib.annotations.Configuration;

@Configuration
public class TestingAutoConfig {
    void boot() {
        PluginTester.plugin.getLogger().info("[AutoConfig] Plugin's Auto Config loaded.");
    }
}

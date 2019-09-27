/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: DependResidence.java@author: karlatemp@vip.qq.com: 19-9-27 下午5:39@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tester.plugintester;

import cn.mcres.karlatemp.mxlib.annotations.AutoInstall;
import cn.mcres.karlatemp.mxlib.annotations.Depend;

@Depend("Residence")
@AutoInstall
public class DependResidence {
    static {
        PluginTester.plugin.getLogger().info("[DependResidence] It loaded.");
    }
}

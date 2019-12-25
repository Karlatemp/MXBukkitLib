/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: BukkitDependChecker.java@author: karlatemp@vip.qq.com: 19-9-27 下午12:44@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.share;

import cn.mcres.karlatemp.mxlib.annotations.Depend;
import cn.mcres.karlatemp.mxlib.tools.DependChecker;
import cn.mcres.karlatemp.mxlib.util.Version;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class BukkitDependChecker implements DependChecker {
    @Override
    public boolean isLoaded(Depend depend) {
        if (depend == null) return true;
        String dep = depend.value();
        PluginManager pm = Bukkit.getPluginManager();
        Plugin p = pm.getPlugin(dep);
        if (p == null) return false;
        String ver = depend.version();
        if (ver.isEmpty()) return true;
        int cp = depend.compare();
        String pver = p.getDescription().getVersion();
        switch (cp) {
            case -1: {
                return Version.compare(pver, ver) < 1;
            }
            case -2: {
                return Version.compare(pver, ver) < 0;
            }
            case 0: {
                return pver.equals(ver);
            }
            case 1: {
                return Version.compare(ver, pver) < 1;
            }
            case 2: {
                return Version.compare(ver, pver) < 0;
            }
        }
        return true;
    }
}

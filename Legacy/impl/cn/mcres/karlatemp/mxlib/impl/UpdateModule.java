/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: UpdateModule.java@author: karlatemp@vip.qq.com: 19-9-20 下午8:31@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.impl;

import cn.mcres.gyhhy.MXLib.Core;
import cn.mcres.gyhhy.MXLib.fcs.F3c;
import cn.mcres.gyhhy.MXLib.http.WebHelper;
import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import cn.mcres.karlatemp.mxlib.share.MXBukkitLibPluginStartup;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;

import cn.mcres.karlatemp.mxlib.share.$MXBukkitLibConfiguration;

public class UpdateModule {
    public static void run(MXBukkitLibPluginStartup pl) {
        File file = pl.getFile();
        File temp = new File(pl.getDataFolder(), "download.jar");
        if ($MXBukkitLibConfiguration.configuration.update.autoupdate) {
            return;
        }
        Server sr = Bukkit.getServer();
        File uf = sr.getUpdateFolderFile();
        new File(uf, file.getName()).delete(); // 删除更新文件

        VersionInfo.updates.add((lastest) -> {
            F3c<HttpURLConnection, InputStream> fc = (code, conn, inp) -> {
                MXBukkitLib.getLogger().printf("[Update] Starting download update.");
                if (code == 200) {
                    byte[] buffer = new byte[1 << 4];
                    temp.createNewFile();
                    try (FileOutputStream fout = new FileOutputStream(temp)) {
                        long time = System.currentTimeMillis();
                        long downsize = 0;
                        while (true) {
                            int lg = inp.read(buffer);
                            if (lg == -1) {
                                break;
                            }
                            downsize += lg;
                            fout.write(buffer, 0, lg);
                            if (System.currentTimeMillis() - time > 1000) {
                                time = System.currentTimeMillis();
                                if (downsize < 1024) {
                                    MXBukkitLib.getLogger().format("[Update] File download %s B", downsize);
                                } else if (downsize < 1024 * 1024) {
                                    MXBukkitLib.getLogger().format("[Update] File download %s KB", downsize / 1024);
                                } else {
                                    MXBukkitLib.getLogger().format("[Update] File download %s MB", downsize / 1024 / 1024);
                                }
                            }
                        }
                    }
                    MXBukkitLib.getLogger().printf("Lastest version(" + lastest + ") downloaded.");
                    uf.mkdirs();
                    File to = new File(uf, file.getName());
//                    Core.getBL().printf(to);
                    to.delete();
                    temp.renameTo(to);
                    temp.delete();
                }
            };
            WebHelper.http("https://dev.tencent.com/u/GYHHY/p/MXBukkitLib/git/raw/master/dist/MXBukkitLib.jar")
                    .response(fc).onCatch((w) -> {
                WebHelper.http("https://github.com/Karlatemp/MXBukkitLib/raw/master/dist/MXBukkitLib.jar")
                        .response(fc).onCatch((wx) -> {
                    MXBukkitLib.getLogger().error("Download lastest version failed");
                }).connect();
            }).connect();
        });
    }
}

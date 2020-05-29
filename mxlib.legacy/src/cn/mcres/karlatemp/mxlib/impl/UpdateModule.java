/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: UpdateModule.java@author: karlatemp@vip.qq.com: 19-9-20 下午8:31@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.impl;

import cn.mcres.gyhhy.MXLib.fcs.F3c;
import cn.mcres.gyhhy.MXLib.http.WebHelper;
import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import cn.mcres.karlatemp.mxlib.share.$MXBukkitLibConfiguration;
import cn.mcres.karlatemp.mxlib.share.MXBukkitLibPluginStartup;
import cn.mcres.karlatemp.mxlib.tools.StringHelper;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import org.bukkit.Bukkit;
import org.bukkit.Server;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicReference;

public class UpdateModule {
    public static String foundSha1() {
        // https://gyhhy.coding.net/p/MXBukkitLib/d/MXBukkitLib/git/raw/master/dist/MXBukkitLib.jar.sh1.txt
        // https://github.com/Karlatemp/MXBukkitLib/raw/master/dist/MXBukkitLib.jar.sha1.txt
        var result = new AtomicReference<String>();
        F3c<HttpURLConnection, InputStream> fc = (code, conn, inp) -> {
            var output = new ByteArrayOutputStream();
            Toolkit.IO.writeTo(conn.getInputStream(), output);
            result.set(new String(output.toByteArray(), StandardCharsets.UTF_8));
        };
        WebHelper.http("https://gyhhy.coding.net/p/MXBukkitLib/d/MXBukkitLib/git/raw/master/dist/MXBukkitLib.jar.sha1.txt")
                .response(fc).onCatch((w) -> {
            WebHelper.http("https://github.com/Karlatemp/MXBukkitLib/raw/master/dist/MXBukkitLib.jar.sha1.txt")
                    .response(fc).onCatch((wx) -> {
                MXBukkitLib.getLogger().error("Failed to get latest version's sha1");
                result.set(null);
            }).connect();
        }).connect();
        return result.get();
    }

    public static void run(MXBukkitLibPluginStartup pl) {
        File file = pl.getFile();
        File temp = new File(pl.getDataFolder(), "download.jar");
        if (!$MXBukkitLibConfiguration.configuration.update.autoupdate) {
            return;
        }
        Server sr = Bukkit.getServer();
        File uf = sr.getUpdateFolderFile();
        new File(uf, file.getName()).delete(); // 删除更新文件

        VersionInfo.updates.add((lastest) -> {
            var sha1 = foundSha1();
            if (sha1 == null) {
                return;
            }
            F3c<HttpURLConnection, InputStream> fc = (code, conn, inp) -> {
                MXBukkitLib.getLogger().printf("[MXLib] [Update] Starting download update.");
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
                                    MXBukkitLib.getLogger().format("[MXLib] [Update] File download %s B", downsize);
                                } else if (downsize < 1024 * 1024) {
                                    MXBukkitLib.getLogger().format("[MXLib] [Update] File download %s KB", downsize / 1024);
                                } else {
                                    MXBukkitLib.getLogger().format("[MXLib] [Update] File download %s MB", downsize / 1024 / 1024);
                                }
                            }
                        }
                    }
                    MXBukkitLib.getLogger().printf("[MXLib] Lastest version(" + lastest + ") downloaded.");
                    uf.mkdirs();
                    try {
                        var sha1Downloaded = StringHelper.byteArrayToHexString(Toolkit.IO.digest(MessageDigest.getInstance("SHA-1"), temp));
                        if (!sha1Downloaded.equalsIgnoreCase(sha1.trim())) {
                            temp.delete();
                            throw new IOException("Sha1 not match. SHA-1 of web is " + sha1 + ", but downloaded " + sha1Downloaded);
                        }
                    } catch (NoSuchAlgorithmException e) {
                        throw new IOException(e);
                    }
                    File to = new File(uf, file.getName());
//                    Core.getBL().printf(to);
                    to.delete();
                    temp.renameTo(to);
                    temp.delete();
                }
            };
            WebHelper.http("https://gyhhy.coding.net/p/MXBukkitLib/d/MXBukkitLib/git/raw/master/dist/MXBukkitLib.jar")
                    .response(fc).onCatch((w) -> {
                WebHelper.http("https://github.com/Karlatemp/MXBukkitLib/raw/master/dist/MXBukkitLib.jar")
                        .response(fc).onCatch((wx) -> {
                    MXBukkitLib.getLogger().error("Download latest version failed");
                    MXBukkitLib.getLogger().printStackTrace(wx);
                }).connect();
            }).connect();
        });
    }
}

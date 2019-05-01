/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.bukkit;

import cn.mcres.gyhhy.MXLib.http.WebHelper;
import cn.mcres.gyhhy.MXLib.fcs.F3c;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import org.bukkit.Server;
import org.bukkit.Bukkit;

/**
 *
 * @author 32798
 */
public class AutoUpdate {

    static void run(Plugin pl) {
        File file = pl.getFile();
        File temp = new File(pl.getDataFolder(), "download.jar");
        if (!pl.getConfig().getBoolean("update.autoupdate", true)) {
            return;
        }
        Server sr = Bukkit.getServer();
        File uf = sr.getUpdateFolderFile();

        UpdateCheck.updatecheck.add((lastest) -> {
            F3c<HttpURLConnection, InputStream> fc = (code, conn, inp) -> {
                if (code == 200) {
                    byte[] buffer = new byte[1 << 4];
                    temp.createNewFile();
                    FileOutputStream fout = new FileOutputStream(temp);
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
                                Plugin.exl.format("File download %s B", downsize);
                            } else if (downsize < 1024 * 1024) {
                                Plugin.exl.format("File download %s KB", downsize / 1024);
                            } else {
                                Plugin.exl.format("File download %s MB", downsize / 1024 / 1024);
                            }
                        }
                    }
                    Plugin.exl.printf("Lastest version({0}) downloaded.", lastest);
                    uf.mkdirs();
                    File to = new File(uf, file.getName());
//                    Plugin.exl.printf(to);
                    temp.renameTo(to);
                }
            };
            WebHelper.http("https://github.com/Karlatemp/MXBukkitLib/raw/master/dist/MXBukkitLib.jar")
                    .response(fc).onCatch((w) -> {
                WebHelper.http("https://dev.tencent.com/u/GYHHY/p/MXBukkitLib/git/raw/master/dist/MXBukkitLib.jar")
                        .response(fc).onCatch((wx) -> {
                    Plugin.exl.error("Download lastest version failed");
                }).connect();
            }).connect();
        });
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.bukkit;

import org.bukkit.plugin.Plugin;
import cn.mcres.gyhhy.MXLib.http.WebHelper;
import cn.mcres.gyhhy.MXLib.fcs.F3c;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Scanner;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import static cn.mcres.gyhhy.MXLib.bukkit.Plugin.write;
import org.bukkit.entity.Player;

public class UpdateCheck {

    public static final String github = "https://raw.githubusercontent.com/GYHHY/MXBukkitLib/master/version.txt",
            tencent = "https://dev.tencent.com/u/GYHHY/p/MXBukkitLib/git/raw/master/version.txt";

    public static void check(Plugin pl) {
        Server s = Bukkit.getServer();
        s.getScheduler().runTaskLaterAsynchronously(pl, () -> setup(pl), 0);
    }

    public static void setup(Plugin pl) {
        @SuppressWarnings("Convert2Lambda")
        F3c<HttpURLConnection, InputStream> func = (a, b, c) -> {
            String lastest;
            try (Scanner scanner = new Scanner(c)) {
                lastest = scanner.nextLine();
            }
            b.disconnect();
            if (!MXAPI.getVersion().trim().equalsIgnoreCase(lastest.trim())) {
                final String st = "This lib is out of style.\nPlease download the lastest from https://dev.tencent.com/u/GYHHY/p/MXBukkitLib/git/blob/master/dist/MXBukkitLib.jar\nor https://github.com/GYHHY/MXBukkitLib/blob/master/dist/MXBukkitLib.jar\n"
                        + "Current version: " + MXAPI.getVersion()+"\nLastest version: " + lastest;
                write(st);
                Server s = Bukkit.getServer();
                s.getOnlinePlayers().stream()
                        .filter((x) -> x.hasPermission("mxlib.update"))
                        .forEach((w) -> {
                            w.sendMessage(st);
                        });
                s.getPluginManager().registerEvents(new org.bukkit.event.Listener() {
                    @org.bukkit.event.EventHandler
                    public void a(org.bukkit.event.player.PlayerJoinEvent pje) {
                        Player p = pje.getPlayer();
                        if (p.hasPermission("mxlib.update")) {
                            p.sendMessage(st);
                        }
                    }
                }, pl);
            } else {
                write("This lib is the lastest version.");
            }
        };
        WebHelper.http(github).response(func).onCatch((cat) -> {
            try {
                cn.mcres.gyhhy.MXLib.bukkit.Plugin.getLoggerEX().error("Checkup error from github.");
            } catch (Throwable thrx) {
                System.err.println("Checkup error from github.");
            }
            WebHelper.http(tencent).response(func).onCatch((tw) -> {
                try {
                    cn.mcres.gyhhy.MXLib.bukkit.Plugin.getLoggerEX().printStackTrace(tw, false);
                } catch (Throwable thrx) {
                    System.err.println(tw);
                }
            }).connect();
        }).connect();
    }
}

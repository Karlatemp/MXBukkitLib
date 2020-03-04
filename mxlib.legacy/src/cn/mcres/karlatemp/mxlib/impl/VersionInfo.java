/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: VersionInfo.java@author: karlatemp@vip.qq.com: 19-9-20 下午8:28@version: 2.0
 */
package cn.mcres.karlatemp.mxlib.impl;

import cn.mcres.gyhhy.MXLib.Version;
import cn.mcres.gyhhy.MXLib.bukkit.MXAPI;
import cn.mcres.gyhhy.MXLib.fcs.F3c;
import cn.mcres.gyhhy.MXLib.http.WebHelper;
import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import cn.mcres.karlatemp.mxlib.share.BukkitToolkit;
import cn.mcres.karlatemp.mxlib.share.MXBukkitLibPluginStartup;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * @author Karlatemp
 */
public class VersionInfo {
    public static final List<Consumer<String>> updates = new ArrayList<>();

    private static void write(String s) {
        MXBukkitLibPluginStartup.plugin.getLogger().info(s);
    }

    public static void check(Plugin pl) {
        F3c<HttpURLConnection, InputStream> func = (a, b, c) -> {
            VersionInfo fi = new VersionInfo();
            Map<String, List<String>> versions;
            try (InputStreamReader reader = new InputStreamReader(c, StandardCharsets.UTF_8)) {
                try (Scanner scanner = new Scanner(reader)) {
                    versions = fi.read(scanner);
                }
            }
            String lastest = versions.keySet().iterator().next();
            b.disconnect();
            String now = MXAPI.getVersion().trim();
            int compare = Version.compare(now, lastest.trim());
            if (compare < 0) {
                StringBuilder sb = new StringBuilder()
                        .append(
                                ""
                                        + "This lib is out of style.\n"
                                        + "Please download the latest from https://gyhhy.coding.net/p/MXBukkitLib/d/MXBukkitLib/git/blob/master/dist/MXBukkitLib.jar\n"
                                        + "or https://github.com/GYHHY/MXBukkitLib/blob/master/dist/MXBukkitLib.jar\n"
                                        + "Current version: ")
                        .append(MXAPI.getVersion()).append("\nLastest version: ").append(lastest).append('\n');
                for (Map.Entry<String, List<String>> entry : versions.entrySet()) {
                    String ver = entry.getKey();
                    if (Version.compare(now, ver.trim()) < 0) {
                        sb.append("========== ").append(ver).append(" ==========\n");
                        for (String s : entry.getValue()) {
                            sb.append(s).append('\n');
                        }
                    }
                }
                final String st = sb.toString().trim();
                write(st);
                Server s = Bukkit.getServer();
                BukkitToolkit.getOnlinePlayers().stream()
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
                updates.forEach(x -> x.accept(lastest));
            } else {
                write("This lib is the latest version with " + lastest + " [" + compare + "]");
            }
        };
        WebHelper.http(
                "https://raw.githubusercontent.com/Karlatemp/MXBukkitLib/master/UPDATE.TXT"
        ).response(func).onCatch((cat) -> {
            try {
                MXBukkitLibPluginStartup.plugin.getLogger().warning("Checkup error from github.");
            } catch (Throwable thrx) {
                System.err.println("Checkup error from github.");
            }
            WebHelper.http(
                    "https://gyhhy.coding.net/p/MXBukkitLib/d/MXBukkitLib/git/raw/master/UPDATE.TXT"
            ).response(func).onCatch((tw) -> {
                try {
                    MXBukkitLibPluginStartup.plugin.getLogger().log(Level.SEVERE, null, tw);
                } catch (Throwable thrx) {
                    System.err.println(tw);
                }
            }).connect();
        }).connect();
    }

    public void skip(Map<String, List<String>> data) {
        if (data != null) {
            Set<Map.Entry<String, List<String>>> set = data.entrySet();
            for (Map.Entry<String, List<String>> et : set) {
                List<String> l = et.getValue();
                int c = Integer.MAX_VALUE;
                for (String s : l) {
                    final int a = s.length();
                    int i = 0;
                    for (; i < a; i++) {
                        if (!Character.isSpaceChar(s.charAt(i))) {
                            break;
                        }
                    }
                    if (i > 0) {
                        c = Math.min(i, c);
                    }
                }
//                System.out.println("MIN " + c);
                final int x = l.size();
                for (int i = 0; i < x; i++) {
                    String line = l.remove(0);
                    if (line.length() < c) {
                        l.add("");
                    } else {
                        l.add(line.substring(c));
                    }
                }
            }
        }
    }

    public Map<String, List<String>> read(Scanner scanner) {
        Map<String, List<String>> vers = new LinkedHashMap<>();
        String v = null;
        List<String> arr = null;
        while (scanner.hasNextLine()) {
            String next = scanner.nextLine();
            if (v == null) {
                v = next;
                arr = new ArrayList<>();
                vers.put(v, arr);
                continue;
            }
            if (next.length() == 0) {
                arr.add("");
                continue;
            }
            char fir = next.charAt(0);
            if (Character.isSpaceChar(fir)) {
                arr.add(next);
            } else {
                v = next;
                arr = new ArrayList<>();
                vers.put(v, arr);
            }
        }
        skip(vers);
        return vers;
    }
}

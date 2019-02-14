/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.Server;

/**
 *
 * @author 32798
 */
public class Info {

    private static Info info = null;

    static {
        Server ser = Bukkit.getServer();
        if (ser != null) {
            String mv = ser.getVersion();
            String[] sp = mv.split("MC\\:");
            mv = sp[sp.length-1].replace(")","").trim();
            setInfo(
                    new Info(
                            ser.getClass().getName().split("\\.")[3],
                            mv,
                            ser.getBukkitVersion() + " (" + ser.getVersion() + ")"
                    )
            );
        }
    }

    public static Info getInfo() {
        return info;
    }

    static void setInfo(Info info) {
        Info.info = info;
    }
    public final String serverVersion, mv, fv;

    Info(String sv, String mv, String fv) {
        this.serverVersion = sv;
        this.mv = mv;
        this.fv = fv;
    }

    public String getServerMinecraftVersion() {
        return mv;
    }

    public String getFullVersion() {
        return fv;
    }

    public String getServerNMSVersion() {
        return serverVersion;
    }
}

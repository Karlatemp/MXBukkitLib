/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.bukkit;

import java.util.logging.Level;
import java.util.logging.Logger;
import cn.mcres.gyhhy.MXLib.http.WebHelper;
import java.util.Scanner;
/**
 *
 * @author 32798
 */
public class Plugin extends org.bukkit.plugin.java.JavaPlugin {
    private static final String github = "https://raw.githubusercontent.com/GYHHY/MXBukkitLib/master/version.txt";
    static {
        try {
            Class.forName(MXAPI.class.getName());
        } catch (ClassNotFoundException ex) {
        }
    }
    public static Plugin plugin = null;

    public Plugin() {
        plugin = this;
    }

    public void onEnable() {
        Info info = Info.getInfo();
        String sp
                = String.format("MXLib Version: %s\nMineCraft version: %s\nFull version: %s\nNMS version: %s\nTitle API class path: %s",
                        MXAPI.getVersion(),info.getServerMinecraftVersion(), info.getFullVersion(), info.getServerNMSVersion(),MXAPI.getTitleAPI().getClass().getName());
        write(sp);
        getServer().getScheduler().runTaskLaterAsynchronously(this, this::checkup, 0);
//        checkup();
    }

    public void write(String sp) {
        Logger logger = getLogger();
        if (sp.indexOf('\n') > -1) {
            String[] lines = sp.split("\\n");
            for (String line : lines) {
                logger.log(Level.INFO, line);
            }
        } else {
            logger.log(Level.INFO, sp);
        }
    }

    private void checkup() {
        WebHelper.http(github).response((a,b,c)->{
            Scanner scanner = new Scanner(c);
            String lastest = scanner.nextLine();
            scanner.close();
            b.disconnect();
            
            if(!MXAPI.getVersion().equals(lastest)){
                haveNew();
            } else {
                write("This lib is the lastest version.");
            }
            
        }).connect();
    }

    private void haveNew() {
        write("This Lib is not the lastest.\ndownload the lastest from https://github.com/GYHHY/MXBukkitLib");
    }
}

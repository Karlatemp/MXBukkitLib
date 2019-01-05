/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.bukkit;

import cn.mcres.gyhhy.MXLib.StringHelper;
import cn.mcres.gyhhy.MXLib.http.WebHelper;
import cn.mcres.gyhhy.MXLib.spigot.SpigotHelper;
import cn.mcres.gyhhy.MXLib.log.Logger;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author 32798
 */
public class Plugin extends org.bukkit.plugin.java.JavaPlugin {
    public static final String github = "https://raw.githubusercontent.com/GYHHY/MXBukkitLib/master/version.txt";
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
        Map<String,Object> argc = new HashMap<>();
        argc.put("","\u00a7e");
        argc.put("b","\u00a7b");
        String sp
                = StringHelper.variable(String.format("{}MXLib Version: {b}%s\n{}MineCraft version: {b}%s\n{}Full version: {b}%s\n{}NMS version: {b}%s\n{}Title API class path: {b}%s\n{}Spigot: {b}%s",
                        MXAPI.getVersion(),info.getServerMinecraftVersion(),
                        info.getFullVersion(), info.getServerNMSVersion(),
                        MXAPI.getTitleAPI().getClass().getName(),SpigotHelper.isSupportSpigot()),argc);
        write(sp);
        getServer().getScheduler().runTaskLaterAsynchronously(this, this::checkup, 0);
//        checkup();
        rundeb();
    }

    public void write(String sp) {
        Logger logger = Logger.getOrCreateLogger(this);
        if (sp.indexOf('\n') > -1) {
            String[] lines = sp.split("\\n");
            for (String line : lines) {
                logger.printf(line);
            }
        } else {
            logger.printf(sp);
        }
    }

    private void checkup() {
        WebHelper.http(github).response((a,b,c)->{
            Scanner scanner = new Scanner(c);
            String lastest = scanner.nextLine();
            scanner.close();
            b.disconnect();
            
            if(!MXAPI.getVersion().trim().equalsIgnoreCase(lastest.trim())){
                haveNew();
                write("The lastest version: " + lastest);
            } else {
                write("This lib is the lastest version.");
            }
            
        }).connect();
    }

    private void haveNew() {
        write("This Lib is not the lastest.\ndownload the lastest from https://github.com/GYHHY/MXBukkitLib");
    }
    private void debug(){
        cn.mcres.gyhhy.MXLib.log.Logger lgr = cn.mcres.gyhhy.MXLib.log.Logger.getOrCreateLogger(this);
        lgr.error("this is error message");
        lgr.error("this is error message with argc: {0} {1}", "Arg0", "Arg1");
        lgr.errorformat("this is error message with errorformat: %s", "Format!");
        lgr.printf("this is a message.");
        lgr.printf("this is a message with argc: {0} {1}", "arg0","arg1");
        PrintStream pr = lgr.getPrintStream();
        pr.println("This is a message with print stream.");
        lgr.printStackTrace(new Error());
    }

    private void rundeb() {
        boolean db = false;
        if(db) debug();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.bukkit;

import cn.mcres.gyhhy.MXLib.StringHelper;
import cn.mcres.gyhhy.MXLib.http.WebHelper;
import cn.mcres.gyhhy.MXLib.log.Logger;
import cn.mcres.gyhhy.MXLib.spigot.SpigotHelper;
import cn.mcres.gyhhy.MXLib.system.ConfSave;
import cn.mcres.gyhhy.MXLib.system.VMHelper;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * The Plugin Class
 * @author 32798
 */
public class Plugin extends org.bukkit.plugin.java.JavaPlugin {
    /**
     * GitHub address for this project
     */
    public static final String github = "https://raw.githubusercontent.com/GYHHY/MXBukkitLib/master/version.txt";

    public static Plugin plugin = null;
    public static Logger exl = Logger.createRawLogger(null, null, "MXBukkitLib");
    static {
        try {
            Class.forName(MXAPI.class.getName());
        } catch (ClassNotFoundException ex) {
        }
    }

    public Plugin() {
        plugin = this;
    }
    @Override
    public File getFile() {
        return super.getFile(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig(); //To change body of generated methods, choose Tools | Templates.
        ConfSave.reload(getConfig());
    }
    public static void setup(org.bukkit.plugin.Plugin thiz){
    
        Info info = Info.getInfo();
        Map<String, Object> argc = new HashMap<>();
        argc.put("", "\u00a7e");
        argc.put("b", "\u00a7b");
        String sp
                = StringHelper.variable(String.format("{}MXLib Version: {b}%s\n{}MineCraft version: {b}%s\n{}Full version: {b}%s\n{}NMS version: {b}%s\n{}Title API class path: {b}%s\n{}Spigot: {b}%s%nIsPay version: %s",
                        MXAPI.getVersion(), info.getServerMinecraftVersion(),
                        info.getFullVersion(), info.getServerNMSVersion(),
                        MXAPI.getTitleAPI().getClass().getName(), SpigotHelper.isSupportSpigot(),
                        MXAPI.isPayMode()), argc);
        write(sp);
        UpdateCheck.check(thiz);
    }
    public void onEnable() {
        VMHelper.load();
        System.out.println(VMHelper.class.getClassLoader());
        super.saveDefaultConfig();
        this.reloadConfig();
        setup(this);
//        checkup();
        rundeb();
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public void onDisable() {
        VMHelper.vhelper.onDisable();
    }
    public static Logger getLoggerEX(){
        return exl;
    }
    public static void write(String sp) {
        Logger logger = getLoggerEX();
        if (sp.indexOf('\n') > -1) {
            String[] lines = sp.split("\\n");
            for (String line : lines) {
                logger.printf(line);
            }
        } else {
            logger.printf(sp);
        }
    }


    private void debug() {
    }

    private void rundeb() {
        boolean db = false;
        if (db) {
            debug();
        }
    }
}

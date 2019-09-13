/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: CommandHelper.java@author: karlatemp@vip.qq.com: 19-9-11 下午1:29@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.bukkit;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;
import cn.mcres.gyhhy.MXLib.StringHelper;

/**
 * @author Karlatemp
 */
public class CommandHelper {

    private static final CommandHelper helper = new CommandHelper();

    public String get(String[] args, int index) {
        return getArg(args, index);
    }

    public List<String> format(List<String> list, String last) {
        return format(list, last, false);
    }

    public List<String> format(List<String> list, String last, boolean cast) {
        String llx = last;
        last = last.toLowerCase();// 转为小写
        for (int i = 0; i < list.size(); i++) { //最好时候原始的 int 循环
            String val = list.get(i);
            if (!val.toLowerCase().startsWith(last)) {
                list.remove(i); //不符合mc的正常tab规则
                i--;
            }
        }
        if (cast) {
            if (!list.contains(last)) {
                list.add(last);
            }
            last = last.toUpperCase();
            if (!list.contains(last)) {
                list.add(last);
            }
            if (!list.contains(llx)) {
                list.add(llx);
            }
        }
        java.util.Collections.sort(list); // 排序
        return list;
    }

    public String getArg(String[] args, int index) {
        return StringHelper.get(args, index);
    }

    public static CommandHelper getHelper() {
        return helper;
    }

    protected CommandMap cmap;

    public CommandMap getCommandMap() {
        if (cmap == null) {
            loadMap();
        }
        return cmap;
    }

    protected void loadMap() {
        if (cmap != null) {
            return;
        }
        Server s = Bukkit.getServer();
        try {
            Method met = s.getClass().getMethod("getCommandMap");
            met.setAccessible(true);
            cmap = (CommandMap) met.invoke(s);
        } catch (Throwable ex) {
        }
        if (cmap == null) {
            PluginManager pm = Bukkit.getPluginManager();
            if (pm instanceof SimplePluginManager) {
                Class<?> cw = SimplePluginManager.class;
                for (Field f : cw.getDeclaredFields()) {
                    Class<?> type = f.getType();
                    if (CommandMap.class.isAssignableFrom(type)) {
                        f.setAccessible(true);
                        try {
                            cmap = (CommandMap) f.get(pm);
                            break;
                        } catch (IllegalArgumentException | IllegalAccessException ex) {
                        }
                    }
                }
            }
        }
    }

    /*
    public void remove(Command cm){
        CommandMap map = getCommandMap();
        
    }*/
}

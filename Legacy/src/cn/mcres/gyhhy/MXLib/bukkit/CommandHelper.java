/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: CommandHelper.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.bukkit;

import java.util.List;

import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import org.bukkit.command.CommandMap;
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
    protected boolean unsupported = false;

    public CommandMap getCommandMap() {
        if (cmap == null && !unsupported) {
            loadMap();
        }
        return cmap;
    }

    protected void loadMap() {
        if (cmap != null || unsupported) {
            return;
        }
        cmap = MXBukkitLib.getBeanManager().getOptional(CommandMap.class).orElseGet(() -> {
            MXBukkitLib.getLogger().error("[Legacy][CommandHelper] Cannot get CommandMap of server.");
            unsupported = true;
            return null;
        });
    }

    /*
    public void remove(Command cm){
        CommandMap map = getCommandMap();
        
    }*/
}

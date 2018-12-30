/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.bukkit;

import cn.mcres.gyhhy.MXLib.StringHelper;
import java.util.List;

/**
 *
 * @author 32798
 */
public class CommandHelper {
    private static final CommandHelper helper = new CommandHelper();
    public String get(String[] args,int index){return getArg(args,index);}
    public List<String> format(List<String> list, String last) {
        last = last.toLowerCase();// 转为小写
        for (int i = 0; i < list.size(); i++) { //最好时候原始的 int 循环
            String val = list.get(i);
            if (!val.toLowerCase().startsWith(last)) {
                list.remove(i); //不符合mc的正常tab规则
                i--;
            }
        }
        if(!list.contains(last)){
            list.add(last);
        }
        java.util.Collections.sort(list); // 排序
        return list;
    }
    public String getArg(String[] args,int index){
        return StringHelper.get(args, index);
    }
    public static CommandHelper getHelper() {
        return helper;
    }
}

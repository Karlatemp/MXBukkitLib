/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.bukkit;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 32798
 */
public class MXAPI {
    public static final String version = "0.4.1";
    public static CommandHelper getCommandHelper(){return CommandHelper.getHelper();}
    public static String getVersion(){return version;}
    public static Info getInfo(){return Info.getInfo();}
    static TitleAPI titleAPI;
    public static TitleAPI getTitleAPI(){return titleAPI;}
    static{
        Info info = getInfo();
        if(info != null){
            String pag = MXAPI.class.getPackage().getName() + "." + info.getServerNMSVersion();
            String cname = pag+".TitleAPI";
            try {
                Class<? extends TitleAPI> cx = Class.forName(cname).asSubclass(TitleAPI.class);
                titleAPI = cx.newInstance();
            } catch (Exception ex) {
                titleAPI = new DefaultTitleAPI();
            }
        }
    }
}

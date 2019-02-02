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
    /**
     * Get NMS Class<br>
     * Add in version 0.6
     */
    public static Class<?> getNMSClass(String clazz){
        try {
            return Class.forName("net.minecraft.server."+getInfo().getServerNMSVersion() + "." + clazz);
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }
    /**
     * Get Obc Class<br>
     * Add in version 0.6
     */
    public static Class<?> getOBCClass(String clazz){
        try {
            return Class.forName("org.bukkit.craftbukkit."+getInfo().getServerNMSVersion() + "." + clazz);
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }
    
    /**
     * Lib Current Version
     */
    public static final String version = "0.6";
    public static CommandHelper getCommandHelper(){return CommandHelper.getHelper();}
    /**
     * Lib Current Version
     */
    public static String getVersion(){return version;}
    /**
     * Get Server Info
     */
    public static Info getInfo(){return Info.getInfo();}
    static TitleAPI titleAPI;
    public static TitleAPI getTitleAPI(){return titleAPI;}
    /**
     * Is there Gson support?<br>
     * Added in version 0.5
     */
    public static final boolean gson;
    static{
        boolean gson$ = false;
        try{
            Class.forName("com.google.gson.Gson");
            gson$=true;
        }catch(Exception ex){}
        gson = gson$;
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
    /**
     * Check if the provided version number information is supported in the current version.<br>
     * <hr>E.g.(If current version is 0.5.1)<br>
     * <table border="1"><tbody>
     * <tr><td>Input</td><td>Return</td></tr>
     * <tr><td>"0.5"</td><td>true</td></tr>
     * <tr><td>"0.5.1"</td><td>true</td></tr>
     * <tr><td>"0.4"</td><td>true</td></tr>
     * <tr><td>"0.5.2"</td><td>false</td></tr>
     * <tr><td>"0.6"</td><td>false</td></tr>
     * </tbody></table>
     * Added in version 0.5.1
     * @param ver Version to be tested
     * @return Whether to support (more than)
     */
    public static boolean support(final String ver) {
        return support(version,ver);
    }
    public static boolean support(final String now, final String need) {
//        final String now = version,need = ver;
        if(need.equals(now))return true;
        final String[] a = need.split("\\."), b = now.split("\\.");
        int off = Math.max(a.length, b.length);
        for (int i = 0; i < off; i++) {
            if (i < a.length && i < b.length) {
                String aa = a[i], bb = b[i];
                int ca = Integer.parseInt(aa), cb = Integer.parseInt(bb);
                if (ca > cb) {
                    return false;
                }
                if (ca < cb) {
                    return true;
                }
            } else if (i >= a.length && i < b.length) {
                return true;
            } else {
                return false;
            }

        }
        return false;
    }
}

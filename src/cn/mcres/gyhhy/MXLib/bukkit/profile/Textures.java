/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.bukkit.profile;
import cn.mcres.gyhhy.MXLib.http.WebHelper;
import com.google.gson.annotations.SerializedName;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author 32798
 */
public class Textures {
    @SerializedName("timestamp")
    private final long timestamp;
    @SerializedName("profileId")
    private final String pid;
    @SerializedName("profileName")
    private final String pname;
    @SerializedName("isPublic")
    private final boolean pubg;
    public long getTimestamp(){
        return timestamp;
    }
    public Textures(){
        this(0,null,null,false);
    }
    public Textures(long timestamp,String pid,String pname,boolean isPublic){
        this.timestamp = timestamp;
        this.pid = pid;
        this.pname = pname;
        this.pubg = isPublic;
    }
    public String getPid(){return pid;}
    public String getProfileId(){return getPid();}
    public String getProfileName(){return pname;}
    public String getPname(){return getProfileName();}
    public boolean isPublic(){return pubg;}
    public boolean hasSkin(){return txt!=null&&txt.skin!=null;}
    public boolean hasCape(){return txt!=null&&txt.cape!=null;}
    public Texture getSkin(){
        if(txt!=null)return txt.skin;
        return null;
    }
    public boolean isSlim(){
        Texture ure = getSkin();
        if(ure!=null)return ure.isSlim();
        return false;
    }
    public boolean isSteve(){
        return!isSlim();
    }
    public Texture getCape(){
        if(txt!=null)return txt.cape;
        return null;
    }
    @SerializedName("textures")
    private TexturesEX txt;
    public static class Texture{
        @SerializedName("url")
        private String uri;
        @SerializedName("metadata")
        private Map<String,String> data;
        public String getUrl(){
            return uri;
        }
        public URL getURL(){
            return WebHelper.url(uri);
        }
//        private boolean isSteve(){return !isSlim();}
        private boolean isSlim(){
            if(data != null){
                String datax = data.get("model");
                return "slim".equals(datax);
            }
            return false;
        }
        public Map<String,String> getMetadata(){
            if(data == null)return null;
            return new HashMap<>(data);
        }
    }
    private static class TexturesEX{
        @SerializedName("SKIN")
        private Texture skin;
        @SerializedName("CAPE")
        private Texture cape;
    }
    
}

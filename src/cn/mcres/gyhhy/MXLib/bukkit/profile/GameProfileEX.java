/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.bukkit.profile;

import cn.mcres.gyhhy.MXLib.Base64;
import cn.mcres.gyhhy.MXLib.bukkit.MXAPI;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import java.util.Collection;
import java.util.UUID;
import com.google.gson.Gson;

/**
 *
 * @author 32798
 */
public class GameProfileEX implements Profile {

    public String toString() {
        return pro.toString();
    }
    private final GameProfile pro;

    public GameProfileEX(GameProfile pro) {
        if (pro == null) {
            throw new java.lang.NullPointerException();
        }
        this.pro = pro;
    }
    private Textures text;

    @Override
    public Textures getTextures() {
        if (text == null) {
            if (MXAPI.gson) {
                String json = null;
                Collection<Property> textures = textures();
                for(Property p : textures){
                    if(p.getName().equals("textures")){
                        String base = p.getValue();
                        if(base!=null){
                            json = Base64.decode(base);
                        }
                    }
                }
                if (json != null) {
                    Gson gson = new Gson();
                    text = gson.fromJson(json, Textures.class);
                }
            }
        }
        return text;
    }

    public Collection<Property> textures() {
        return this.getProperties().get("textures");
    }

    public PropertyMap getProperties() {
        return pro.getProperties();
    }

    @Override
    public UUID getId() {
        return pro.getId();
    }

    @Override
    public String getName() {
        return pro.getName();
    }

    @Override
    public boolean isComplete() {
        return pro.isComplete();
    }

    @Override
    public boolean isLegacy() {
        return pro.isLegacy();
    }

}

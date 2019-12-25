/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: GameProfileEX.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.bukkit.profile;

import cn.mcres.gyhhy.MXLib.Base64;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;

import java.util.Collection;
import java.util.UUID;

import com.google.gson.Gson;

/**
 * @author 32798
 */
public class GameProfileEX implements Profile {

    public String toString() {
        return pro.toString();
    }

    private final GameProfile pro;

    public GameProfileEX(GameProfile pro) {
        if (pro == null) {
            throw new NullPointerException();
        }
        this.pro = pro;
    }

    private Textures text;

    @Override
    public Textures getTextures() {
        if (text == null) {
            String json = null;
            Collection<Property> textures = textures();
            for (Property p : textures) {
                if (p.getName().equals("textures")) {
                    String base = p.getValue();
                    if (base != null) {
                        json = Base64.decode(base);
                    }
                }
            }
            if (json != null) {
                Gson gson = new Gson();
                text = gson.fromJson(json, Textures.class);
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

/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Textures.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.yggdrasil.beans;

import java.net.URL;
import java.util.Map;

/**
 *
 * @author 32798
 */
public class Textures {

    public boolean signatureRequired;

    public Model model = Model.STEVE;
    public long timestamp;
    public UnsignedUUID profileId;
    public String profileName;
    public Texture skin, cape;

    public static enum Model {
        STEVE, ALEX;
        public static Model from(String s){
            switch(s.toLowerCase()){
                case "slim": return ALEX;
            }
            return STEVE;
        }
    }

    public static class Texture {

        public URL url;
        public Map<String, String> metadata;
    }
}

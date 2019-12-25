/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Profile.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.bukkit.profile;

import java.util.UUID;

/**
 *
 * @author 32798
 */
public interface Profile {
    UUID getId();
    String getName();
    boolean isComplete();
    boolean isLegacy();
    Textures getTextures();
}

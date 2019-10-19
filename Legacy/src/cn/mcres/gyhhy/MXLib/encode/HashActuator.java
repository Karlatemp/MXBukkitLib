/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: HashActuator.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.encode;

import cn.mcres.karlatemp.mxlib.encrypt.HashEncryptor;

public interface HashActuator extends Actuator, HashEncryptor {
    @Override
    default boolean isSupportDecoder() {
        return false;
    }

    @Override
    default Actuator.Decoder getDecoder() {
        throw new UnsupportedOperationException();
    }
}

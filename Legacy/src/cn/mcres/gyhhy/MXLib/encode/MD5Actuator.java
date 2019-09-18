/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MD5Actuator.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.encode;

import cn.mcres.gyhhy.MXLib.StringHelper;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 *
 * @author 32798
 */
public class MD5Actuator implements HashActuator, Actuator.Encoder {

    public static final MD5Actuator actuator = new MD5Actuator();

    public static MD5Actuator getInstance() {
        return actuator;
    }

    @Override
    public MD5Actuator getEncoder() {
        return this;
    }

    @Override
    public Decoder getDecoder() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isSupportEncoder() {
        return true;
    }

    @Override
    public boolean isSupportDecoder() {
        return false;
    }

    @Override
    public String encodeToString(byte[] b) throws EncodeException {
        return encodeToString(new String(b, UTF_8));
    }

    @Override
    public String encodeToString(String b) throws EncodeException {
        return StringHelper.md5(b).toLowerCase();
    }

    @Override
    public byte[] encode(byte[] b) throws EncodeException {
        return encodeToString(b).getBytes(UTF_8);
    }

    @Override
    public byte[] encode(String b) throws EncodeException {
        return encodeToString(b).getBytes(UTF_8);
    }

}

/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Tester.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.encode;

import java.security.KeyPair;

/**
 *
 * @author 32798
 */
public class Tester {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        KeyPair kp = RSAActuator.getRSAEncoder().getKeyPairGenerator().genKeyPair();
        RSAActuator r = new RSAActuator(kp.getPrivate().getEncoded(), kp.getPublic().getEncoded());
        r.setEncodeUsePublicKey(true);
        String data = r.encodeToString("要素察觉");
        System.out.println(data);
        System.out.println(r.decodeToString(data));
//        System.out.println(i.getDecoder().decodeToString(data));
    }
    
}

/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Tester.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.karlatemp.mxlib.encryption;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Base64;

/**
 *
 * @author 32798
 */
public class Tester {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final RSAEncoder re = new RSAEncoder();
        final SecureRandom sr = new SecureRandom("Karlatemp@mcres.cn/MXBukkitLib".getBytes());
        final KeyPair kp = Encoder.generateKeyPair(re.getKeyPairGenerator(), 1024, sr);
        final PrivateKey pr = kp.getPrivate();
        final PublicKey pu = kp.getPublic();
        
        final byte[] enc = "444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444448wEJIONiwheEHNIEYiHWEHNIOUWEhmOIEU (@#*E MUHRW(* MEHU H HEIU Y*#&$ NGHDN* $YG*IET Y*NI#HBJ FE&*TY #NGE* &JT#MG*EN&GBE&U G F^*EJT &*F^SD*F TSD&* ^SDF S*&DF ^S(* JSUFY *S&F^ &SF JS FSF JS FHSFH HSF HSFH*SFU *SF SF*& &*SF* &S*&F &*SF* SF& SF^&S &F^^SF^ S&F *SF ^SFR%& SUIF HIUSF ^S*F BUHS F%SF* GS *".getBytes();
        byte[] end = re.encode(enc, pr);
        System.out.println(Base64.getEncoder().encodeToString(end));
        System.out.println(new String(re.decode(end, pu)));
    }
    
}

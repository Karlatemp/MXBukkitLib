/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.yggdrasil;

import cn.mcres.gyhhy.MXLib.http.WebHelper;
import cn.mcres.gyhhy.MXLib.yggdrasil.beans.FailedMessage;
import cn.mcres.gyhhy.MXLib.yggdrasil.beans.Profile;
import cn.mcres.gyhhy.MXLib.yggdrasil.beans.Textures;
import cn.mcres.gyhhy.MXLib.yggdrasil.beans.custom.CustomYggdrasilInfo;
import cn.mcres.gyhhy.MXLib.yggdrasil.callbacks.Callback;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;

public class Tester {

    public static void main(String... args) throws Throwable {
        CustomYggdrasil ygg = new CustomYggdrasil("https://i.timewk.cn");
        JsonHelper.gson.toJson(ygg.getInfo(), System.out);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.yggdrasil.tt;

import cn.mcres.gyhhy.MXLib.yggdrasil.beans.FailedMessage;
import cn.mcres.gyhhy.MXLib.yggdrasil.beans.custom.CustomYggdrasilInfo;
import cn.mcres.gyhhy.MXLib.yggdrasil.beans.mojang.NameHistory;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.TypeAdapters;

/**
 *
 * @author 32798
 */
public class RD {

    public static GsonBuilder ft(GsonBuilder gb) {
        return gb.registerTypeAdapterFactory(TypeAdapters.newFactory(NameHistory.class, NameHistoryTA.i))
                .registerTypeAdapterFactory(TypeAdapters.newFactory(FailedMessage.class, FMTA.i))
                .registerTypeAdapterFactory(TypeAdapters.newFactory(CustomYggdrasilInfo.class, CustomYggdrasilInfo.getAdapter()));
    }
}

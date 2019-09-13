/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.yggdrasil;

import cn.mcres.gyhhy.MXLib.ThrowHelper;
import cn.mcres.gyhhy.MXLib.encode.URIActuator;
import cn.mcres.gyhhy.MXLib.ext.java.util.KV;
import cn.mcres.gyhhy.MXLib.http.WebHelper;
import cn.mcres.gyhhy.MXLib.yggdrasil.beans.mojang.NameHistory;
import java.io.InputStreamReader;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * <a href="https://wiki.vg/Mojang_API">
 * https://wiki.vg/Mojang_API
 * </a>
 */
public class MojangYggdrasil extends Yggdrasil {

    public String toString() {
        return "Yggdrasil[type=Mojang]";
    }

    public NameHistory queryUserNameHistory(String uuid) {
        KV<NameHistory, Void> kv = new KV<>();
        WebHelper.http("https://api.mojang.com/user/profiles/" + uuid + "/names")
                .onCatch(e -> ThrowHelper.getInstance().thr(e))
                .response((code, ct, io) -> {
                    if (code == 200) {
                        kv.k = JsonHelper.gson.fromJson(new InputStreamReader(io, UTF_8), NameHistory.class);
                    }
                })
                .connect();
        return kv.k;
    }

    @Override
    public String queryPlayerHasJoined_make_link(String username, String serverId, String player_ip) {
        URIActuator u = URIActuator.getInstance();
        String link = "https://sessionserver.mojang.com/session/minecraft/hasJoined?username="
                + u.encodeToString(username) + "&sererId=" + u.encodeToString(serverId);
        if (player_ip != null) {
            link += "&ip=" + u.encodeToString(player_ip);
        }
        return link;
    }

    @Override
    public String queryProfile_make_url(String uuid, boolean unsigned) {
        return "https://sessionserver.mojang.com/session/minecraft/profile/" + URIActuator.getInstance().encodeToString(uuid) + "?unsigned=" + unsigned;
    }

    @Override
    public String queryProfiles_make_url() {
        return "https://api.mojang.com/profiles/minecraft";
    }

}

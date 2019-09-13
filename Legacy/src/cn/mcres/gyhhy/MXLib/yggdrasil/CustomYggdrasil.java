/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.yggdrasil;

import java.net.URL;

import cn.mcres.gyhhy.MXLib.encode.URIActuator;
import cn.mcres.gyhhy.MXLib.http.WebHelper;
import cn.mcres.gyhhy.MXLib.yggdrasil.beans.custom.CustomYggdrasilInfo;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * <a href="https://github.com/yushijinhun/authlib-injector/wiki/Yggdrasil%E6%9C%8D%E5%8A%A1%E7%AB%AF%E6%8A%80%E6%9C%AF%E8%A7%84%E8%8C%83">
 * Yggdrasil服务端技术规范
 * </a><br>
 * <a href="https://github.com/bs-community/blessing-skin-server">
 * Blessing Skin Server
 * </a>
 */
public class CustomYggdrasil extends Yggdrasil {

    private static URL getALI(boolean run, URL root) {
        if (run) {
            return Helper.getALI(root);
        }
        return root;
    }

    private final String root;
    private CustomYggdrasilInfo info;

    public CustomYggdrasil(String root) throws MalformedURLException {
        this(root, true);
    }

    public CustomYggdrasil(String root, boolean checkALI) throws MalformedURLException {
        this(new URL(root), checkALI);
    }

    @Deprecated
    public CustomYggdrasil(String root, Void unused) {
        String rt = root;
        if (rt.endsWith("/")) {
            rt = rt.substring(0, rt.length() - 1);
        }
        this.root = rt;
    }

    public CustomYggdrasil(URL root, boolean checkALI) {
        this(getALI(checkALI, root).toString(), null);
    }

    public CustomYggdrasil(URL root) {
        this(root, true);
    }

    public String getRoot() {
        return root;
    }

    public CustomYggdrasilInfo getInfo() {
        if (info == null) {
            Helper.openWeb(WebHelper.url(root), null, CustomYggdrasilInfo.class, new CB<CustomYggdrasilInfo>() {
                @Override
                public void onSuccessful(CustomYggdrasilInfo t, int NetCode) throws IOException {
                    info = t;
                }
            });
        }
        return info;
    }

    @Override
    public String queryPlayerHasJoined_make_link(String username, String serverId, String player_ip) {
        URIActuator u = URIActuator.getInstance();
        String link = root
                + "/sessionserver/session/minecraft/hasJoined?username="
                + u.encodeToString(username) + "&serverId=" + serverId;
        if (player_ip != null) {
            link += "&ip=" + link;
        }
        return link;
    }

    @Override
    public String queryProfile_make_url(String uuid, boolean unsigned) {
        return root + "/sessionserver/session/minecraft/profile/" + uuid + "?unsigned=" + unsigned;
    }

    @Override
    public String queryProfiles_make_url() {
        return root + "/api/profiles/minecraft";
    }

    public String toString() {
        return "Yggdrasil[type=custom, root=" + root + ']';
    }
}

/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Yggdrasil.java@author: karlatemp@vip.qq.com: 19-9-11 下午5:16@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.yggdrasil;

import cn.mcres.gyhhy.MXLib.Core;
import cn.mcres.gyhhy.MXLib.RefUtil;
import cn.mcres.gyhhy.MXLib.ThrowHelper;
import cn.mcres.gyhhy.MXLib.bukkit.MXAPI;
import cn.mcres.gyhhy.MXLib.ext.java.util.KV;
import cn.mcres.gyhhy.MXLib.http.ContextType;
import cn.mcres.gyhhy.MXLib.http.WebHelper;
import cn.mcres.gyhhy.MXLib.yggdrasil.annotations.Optional;
import cn.mcres.gyhhy.MXLib.yggdrasil.beans.FailedMessage;
import cn.mcres.gyhhy.MXLib.yggdrasil.beans.Profile;
import cn.mcres.gyhhy.MXLib.yggdrasil.beans.UnsignedUUID;
import cn.mcres.gyhhy.MXLib.yggdrasil.callbacks.Callback;
import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;

import static java.nio.charset.StandardCharsets.UTF_8;

import moe.yushi.authlibinjector.AuthlibInjector;

/* AuthlibInjector Download Link: https://authlib-injector.yushi.moe/~download/ */
public abstract class Yggdrasil {

    private static final Yggdrasil server_yggdrasil;
    public static final boolean IS_SUPPORT = isSupport();

    static {
        Yggdrasil y = null;
        Class<?> ig = RefUtil.findLoadedClass(ClassLoader.getSystemClassLoader(), "moe.yushi.authlibinjector.AuthlibInjector");
        if (ig != null) {
            try {
                y = new CustomYggdrasil(System.getProperty(AuthlibInjector.PROP_API_ROOT));
            } catch (Exception ex) {
                MXBukkitLib.getLogger().printStackTrace(ex);
            }
        }
        /// @version 1.13
        if (y == null && RefUtil.findLoadedClass(ClassLoader.getSystemClassLoader(), "org.to2mbn.authlibinjector.AuthlibInjector") != null) {
            String sp = System.getProperty("nide8auth.uuid");
            if (sp != null) {
                try {
                    y = new CustomYggdrasil("https://auth2.nide8.com:233/" + System.getProperty("nide8auth.uuid"));
                } catch (Exception e) {
                    MXBukkitLib.getLogger().printStackTrace(e);
                }
            }
        }
        if (y == null) {
            y = new MojangYggdrasil();
        }
        server_yggdrasil = y;
    }

    public static Yggdrasil getServerYggdrasil() {
        return server_yggdrasil;
    }

    public static boolean isSupport() {
        return RefUtil.classExists("com.google.gson.Gson");
    }

    private static byte[] readAll(InputStream c) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        while (true) {
            int lg = c.read(buff);
            if (lg == -1) {
                break;
            }
            baos.write(buff, 0, lg);
        }
        return baos.toByteArray();
    }

    public String toString() {
        return "Yggdrasil[type=<unknown>]";
    }

    public abstract String queryPlayerHasJoined_make_link(String username, String serverId, @Optional String player_ip);

    /**
     * GET
     * /sessionserver/session/minecraft/hasJoined?username={username}&serverId={serverId}&ip={ip}<br>
     * <a href="https://github.com/yushijinhun/authlib-injector/wiki/Yggdrasil%E6%9C%8D%E5%8A%A1%E7%AB%AF%E6%8A%80%E6%9C%AF%E8%A7%84%E8%8C%83#%E6%9C%8D%E5%8A%A1%E7%AB%AF%E9%AA%8C%E8%AF%81%E5%AE%A2%E6%88%B7%E7%AB%AF">
     * https://github.com/yushijinhun/authlib-injector/wiki/Yggdrasil服务端技术规范
     * </a>
     *
     * @param username
     * @param serverId
     * @param player_ip
     * @param callback
     */
    public void queryPlayerHasJoined(String username, String serverId, @Optional String player_ip, Callback<Profile> callback) {
        Helper.openWeb(WebHelper.url(this.queryPlayerHasJoined_make_link(username, serverId, player_ip)),
                null, Profile.class, callback);
    }

    public Profile queryPlayerHasJoined(String username, String serverId, @Optional String player_ip) {
        KV<Profile, Void> store = new KV<>();
        queryPlayerHasJoined(username, serverId, player_ip, new CB<Profile>() {
            @Override
            public void onSuccessful(Profile t, int NetCode) throws IOException {
                store.k = t;
            }
        });
        /*
        WebHelper.http(this.queryPlayerHasJoined_make_link(username, serverId, player_ip))
                .onCatch(a -> ThrowHelper.getInstance().thr(a))
                .response((code, connection, io) -> {
//                    byte[] buff;
//                    System.out.write(buff=readAll(io));
                    if (code == 200) {
                        store.k = JsonHelper.gson.fromJson(new InputStreamReader(io, UTF_8), Profile.class);
                    } else {
//                        System.out.write(readAll(io));
                    }
                })
                .connect();
         */
        return store.k;
    }

    public abstract String queryProfile_make_url(String uuid, boolean unsigned);

    public void queryProfile(String uuid, boolean unsigned, Callback<Profile> callback) {
        Helper.openWeb(WebHelper.url(this.queryProfile_make_url(uuid, unsigned)), null, Profile.class, callback);
    }

    public Profile queryProfile(UnsignedUUID uuid, boolean unsigned) {
        return this.queryProfile(String.valueOf(uuid), unsigned);
    }

    public void queryProfile(UnsignedUUID uuid, boolean unsigned, Callback<Profile> callback) {
        this.queryProfile(String.valueOf(uuid), unsigned, callback);
    }

    public Profile queryProfile(String uuid, boolean unsigned) {
        KV<Profile, Void> store = new KV<>();
        queryProfile(uuid, unsigned, new CB<Profile>() {
            @Override
            public void onSuccessful(Profile t, int NetCode) throws IOException {
                store.k = t;
            }
        });
        return store.k;
    }

    public abstract String queryProfiles_make_url();

    protected static abstract class CB<T> implements Callback<T> {

        @Override
        public void onFailed(FailedMessage err, int NetCode) throws IOException {
            ThrowHelper.getDefault().thr(err.toThrowable());
        }

        @Override
        public void onFailed(byte[] datas, int http_code) throws IOException {
            Callback.super.onFailed(datas, http_code); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void onError(Throwable thrown) {
            ThrowHelper.getDefault().thr(thrown);
        }
    }

    public void queryProfiles(Callback<Profile[]> callback, String... pnames) {
        Helper.openWeb(WebHelper.url(this.queryProfiles_make_url()), pnames, Profile[].class, callback);
    }

    public Profile[] queryProfiles(String... pnames) {
        KV<Profile[], Void> store = new KV<>();
        queryProfiles(new CB<Profile[]>() {
            @Override
            public void onSuccessful(Profile[] t, int NetCode) throws IOException {
                store.k = t;
            }
        }, pnames);
        /*
        String url = this.queryProfiles_make_url();
//        System.out.println(url);
        WebHelper.post(url)
                .write(out -> {
                    try {
                        try (OutputStreamWriter osw = new OutputStreamWriter(out, UTF_8)) {
                            JsonHelper.gson.toJson(pnames, String[].class, osw);
                            osw.flush();
                        }
                    } catch (Throwable thr) {
                        ThrowHelper.getInstance().thr(thr);
                    }
                    return ContextType.application_json;
                })
                .onCatch(a -> ThrowHelper.getInstance().thr(a))
                .response((a, b, c) -> {
                    if (a == 200) {
                        store.k = JsonHelper.gson.fromJson(
                                new InputStreamReader(
                                        c, UTF_8), Profile[].class);
                    } else {
                        System.out.write(readAll(c));
                    }
                })
                .connect();
         */
        if (store.k == null) {
            return new Profile[0];
        }
        return store.k;
    }

    public static class Start {

        public static void main(String[] args) throws Exception {
            Gson g = JsonHelper.gson;
//            System.out.println(g.getAdapter(Profile.class));
//            Yggdrasil ygg = new CustomYggdrasil(WebHelper.url("https://timemc.cn/api/yggdrasil"));
            //noinspection deprecation
            Yggdrasil ygg = new CustomYggdrasil("https://timemc.cn/api/yggdrasil", null);
            Profile[] EffectiveProfiles = ygg.queryProfiles("outtime", "Karlatemp", "Rev_aria", "terry_ma", "FxxkINGCYA");

            for (Profile user : EffectiveProfiles) {
                StringWriter sw = new StringWriter();
                System.out.println(user.name + ':' + user.id);

                Profile profile = ygg.queryProfile(user.id.toString(), false);
                JsonHelper.gson.toJson(profile, sw/*JsonHelper.inlineWriter(sw)*/);
                System.out.println(sw);
                System.out.println();
            }
//            System.out.println(JsonHelper.gson.toJson(pfs));
        }
    }
}

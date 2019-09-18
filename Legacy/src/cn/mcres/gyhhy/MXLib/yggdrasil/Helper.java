/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Helper.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.yggdrasil;

import cn.mcres.gyhhy.MXLib.ThrowHelper;
import cn.mcres.gyhhy.MXLib.ext.java.util.KV;
import cn.mcres.gyhhy.MXLib.http.ContextType;
import cn.mcres.gyhhy.MXLib.http.WebHelper;
import cn.mcres.gyhhy.MXLib.yggdrasil.beans.FailedMessage;
import cn.mcres.gyhhy.MXLib.yggdrasil.beans.Profile;
import cn.mcres.gyhhy.MXLib.yggdrasil.beans.mojang.NameHistory;
import cn.mcres.gyhhy.MXLib.yggdrasil.tt.FMTA;
import cn.mcres.gyhhy.MXLib.yggdrasil.callbacks.Callback;
import com.google.gson.JsonNull;
import com.google.gson.stream.JsonReader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.net.URL;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author 32798
 */
public class Helper {

    public static URL getALI(URL ur) {
        return getALI(ur, 5);
    }

    public static URL getALI(URL ur, int maxConnectCount) {
        if (maxConnectCount < 0) {
            return ThrowHelper.getInstance().thr(new java.net.ConnectException("Redirected too many times"));
        }
        KV<URL, Void> kv = new KV<>(ur, null);
        WebHelper.http(ur).response((a, b, c) -> {
            Map<String, List<String>> ffd = b.getHeaderFields();
            List<String> lt = ffd.get("X-Authlib-Injector-API-Location");
            if (lt == null || lt.isEmpty()) {
                return;
            }
            if (lt.size() != 1) {
                throw new IOException("Cannot find unique X-Authlib-Injector-API-Location");
            }
            String gtt = lt.get(0);
            URL addx = new URL(ur, gtt);
            if (addx.equals(kv.k)) {
                return;
            }
            kv.k = getALI(addx, maxConnectCount - 1);
        }).onCatch(ThrowHelper.getInstance()::thr).connect();
        return kv.k;
    }

    public static void main(String[] args) {
        openWeb(WebHelper.url("https://timemc.cn/api/yggdrasil/api/profiles/minecraft"),
                new String[]{"Karlatemp", "outtime"},
                Profile[].class,
                new Callback<Profile[]>() {
                    @Override
                    public void onSuccessful(Profile[] t, int NetCode) throws IOException {
                        if (t != null) {
                            for (Profile pp : t) {
                                StringWriter sw = new StringWriter();
                                Object data = pp;
                                if (data == null) {
                                    data = JsonNull.INSTANCE;
                                }
                                JsonHelper.gson.toJson(data, data.getClass(), JsonHelper.inlineWriter(sw));
                                System.out.println(sw);
                            }
                        }
                    }

                    @Override
                    public void onFailed(FailedMessage err, int NetCode) throws IOException {
                        System.out.println(err);
                        System.out.println("Net code: " + NetCode);
                    }
                });
    }

    private static boolean errorCode(byte[] res) {
        try {
            return FMTA.i.check(openReader(res));
        } catch (IOException | IllegalStateException ioe) {
            return false;
        }
    }

    private static JsonReader openReader(byte[] res) {
        return new JsonReader(new InputStreamReader(open(res), UTF_8));
    }

    private Helper() {
    }

    public static ByteArrayInputStream open(byte[] data) {
        return new ByteArrayInputStream(data);
    }

    public static byte[] readAll(InputStream is) throws IOException {
        return readAll(is, true);
    }

    public static byte[] readAll(InputStream is, boolean autoClose) throws IOException {
        ByteArrayOutputStream bais = new ByteArrayOutputStream();
        try (Closeable clo = autoClose ? is : bais) {
            byte[] buffer = new byte[1024];
            while (true) {
                int lg = is.read(buffer);
                if (lg == -1) {
                    break;
                }
                bais.write(buffer, 0, lg);
            }
            clo.close();
            return bais.toByteArray();
        }
    }

    public static <T> void openWeb(URL url, Object data, Class<T> ref, Callback<T> cb) {
        openWeb(url, data, (Type) ref, cb);
    }

    public static <T> void openWeb(URL url, Object data, Type ref, Callback<T> cb) {
        url.getClass();
        ref.getClass();
        cb.getClass();
        WebHelper.HttpHelper client;
        if (data == null) {
            client = WebHelper.http(url);
        } else {
            client = WebHelper.post(url).write(baos -> {
                try (OutputStreamWriter osw = new OutputStreamWriter(baos, UTF_8)) {
                    JsonHelper.gson.toJson(data, osw);
                }
                return ContextType.application_json;
            });
        }
        client.header(connect -> {
            connect.setConnectTimeout(5000);
            connect.setReadTimeout(5000);
        });
        client.response((code, connect, io) -> {
            byte[] res = readAll(io, false);
            if (errorCode(res)) {
                cb.onFailed(FMTA.i.read(openReader(res)), code);
                return;
            }
            if (ref == Void.class) {
                cb.onFailed(res, code);
                return;
            }
            if (code == 200) {
                cb.onSuccessful(JsonHelper.gson.fromJson(openReader(res), ref), code);
            } else {
                cb.onFailed(res, code);
            }
        }).onCatch(cb::onError).connect();
    }
}

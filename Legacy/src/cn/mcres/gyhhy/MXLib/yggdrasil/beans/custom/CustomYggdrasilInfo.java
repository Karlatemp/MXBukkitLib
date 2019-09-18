/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: CustomYggdrasilInfo.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.yggdrasil.beans.custom;

import cn.mcres.gyhhy.MXLib.Core;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.Map;
import cn.mcres.gyhhy.MXLib.ext.lookup.Looker;
import com.google.gson.stream.JsonToken;
import java.lang.invoke.MethodHandle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import cn.mcres.gyhhy.MXLib.encode.Base64Actuator;
import cn.mcres.gyhhy.MXLib.encode.RSAActuator;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomYggdrasilInfo {

    static final Adapter adapter = new Adapter();

    public static TypeAdapter<CustomYggdrasilInfo> getAdapter() {
        return adapter;
    }

    static class Adapter extends TypeAdapter<CustomYggdrasilInfo> {

        private static final Looker lk = new Looker(Looker.openLookup(Adapter.class, ~0));
        private static final MethodHandle setMetas = lk.findSetter(CustomYggdrasilInfo.class, "metas", Map.class),
                setDomains = lk.findSetter(CustomYggdrasilInfo.class, "domains", List.class),
                setSk = lk.findSetter(CustomYggdrasilInfo.class, "sk", RSAPublicKey.class);

        private CustomYggdrasilInfo create(Map<String, Object> metas, List<String> domains, byte[] rsa) {
            CustomYggdrasilInfo info = new CustomYggdrasilInfo();
            try {
                if (metas != null) {
                    setMetas.invoke(info, Collections.unmodifiableMap(metas));
                }
                if (domains != null) {
                    setDomains.invoke(info, Collections.unmodifiableList(domains));
                }
                if (rsa != null) {
                    setSk.invoke(info, RSAActuator.getRSAEncoder().getPublicKey(rsa));
                }
            } catch (Throwable ex) {
                Core.getLogger().log(Level.SEVERE, null, ex);
            }
            return info;
        }

        @Override
        public void write(JsonWriter out, CustomYggdrasilInfo value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }
            out.beginObject();
            Object dox = value.domains;
            if (dox != null) {
                out.name("skinDomains");
                write(dox, out);
            }
            dox = value.metas;
            if (dox != null) {
                out.name("meta");
                write(dox, out);
            }
            RSAPublicKey k = value.sk;
            if (k != null) {
                StringBuilder bui = new StringBuilder("-----BEGIN PUBLIC KEY-----\n");
                StringReader rd = new StringReader(Base64Actuator.getInstance().encodeToString(k.getEncoded()));
                char[] buffer = new char[64];
                while (true) {
                    int lg = rd.read(buffer);
                    if (lg == -1) {
                        break;
                    }
                    bui.append(buffer, 0, lg).append('\n');
                }
                out.name("signaturePublickey").value(bui.append("-----END PUBLIC KEY-----\n").toString());
            }
            out.endObject();
        }

        @Override
        public CustomYggdrasilInfo read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            Map<String, Object> metas = new HashMap<>();
            List<String> domains = new ArrayList<>();
            byte[] rsa = null;
            in.beginObject();
            while (in.hasNext()) {
                String name = in.nextName();
                switch (name) {
                    case "meta": {
                        readMap(metas, in);
                        break;
                    }
                    case "skinDomains": {
                        in.beginArray();
                        while (in.hasNext()) {
                            domains.add(in.nextString());
                        }
                        in.endArray();
                        break;
                    }
                    case "signaturePublickey": {
                        rsa = parse(in.nextString());
                        break;
                    }

                    default: {
                        in.skipValue();
                    }
                }
            }
            in.endObject();
            return create(metas, domains, rsa);
        }

        private byte[] parse(String ky) {
            StringBuilder sb = new StringBuilder();
            try (Scanner s = new Scanner(ky)) {
                while (s.hasNextLine()) {
                    if (s.nextLine().equalsIgnoreCase("-----BEGIN PUBLIC KEY-----")) {
                        break;
                    }
                }
                while (s.hasNextLine()) {
                    String next = s.nextLine();
                    if (next.equalsIgnoreCase("-----END PUBLIC KEY-----")) {
                        break;
                    }
                    sb.append(next);
                }
            }
            return Base64Actuator.getInstance().decode(sb.toString());
        }

        private void readList(List<Object> list, JsonReader in) throws IOException {
            in.beginArray();
            while (in.hasNext()) {
                list.add(openRead(in));
            }
            in.endArray();
        }

        private Object openRead(JsonReader in) throws IOException {
            JsonToken jt = in.peek();
            if (jt == JsonToken.NULL) {
                in.nextNull();
                return null;
            } else if (jt == JsonToken.BEGIN_OBJECT) {
                Map<String, Object> obj = new HashMap<>();
                readMap(obj, in);
                return obj;
            } else if (jt == JsonToken.BEGIN_ARRAY) {
                List<Object> l = new ArrayList<>();
                readList(l, in);
                return l;
            } else if (jt == JsonToken.BOOLEAN) {
                return in.nextBoolean();
            } else if (jt == JsonToken.STRING) {
                return in.nextString();
            } else if (jt == JsonToken.NUMBER) {
                return in.nextDouble();
            } else {
                in.skipValue();
                return null;
            }
        }

        private void readMap(Map<String, Object> metas, JsonReader in) throws IOException {
            in.beginObject();
            while (in.hasNext()) {
                metas.put(in.nextName(), openRead(in));
            }
            in.endObject();
        }

        private void write(Object data, JsonWriter out) throws IOException {
            if (data == null) {
                out.nullValue();
            } else if (data instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> mpp = (Map<String, Object>) data;
                out.beginObject();
                for (Map.Entry<String, Object> se : mpp.entrySet()) {
                    out.name(se.getKey());
                    write(se.getValue(), out);
                }
                out.endObject();
            } else if (data instanceof List) {
                @SuppressWarnings("unchecked")
                List<Object> datas = (List<Object>) data;
                out.beginArray();
                for (Object o : datas) {
                    write(o, out);
                }
                out.endArray();
            } else if (data instanceof String) {
                out.value(data.toString());
            } else if (data instanceof Number) {
                out.value((Number) data);
            } else if (data instanceof Boolean) {
                out.value((Boolean) data);
            } else {
                out.nullValue();
            }
        }
    }
    private final Map<String, Object> metas = null;
    private final List<String> domains = null;
    private final RSAPublicKey sk = null;

    public Map<String, Object> getMetas() {
        return metas;
    }

    public RSAPublicKey getPublicKey() {
        return sk;
    }

    public List<String> getSkinDomains() {
        return domains;
    }
}

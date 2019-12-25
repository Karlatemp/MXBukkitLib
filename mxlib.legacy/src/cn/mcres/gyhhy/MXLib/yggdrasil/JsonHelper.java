/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: JsonHelper.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.yggdrasil;

import cn.mcres.gyhhy.MXLib.encode.Base64Actuator;
import cn.mcres.gyhhy.MXLib.yggdrasil.beans.Profile;
import cn.mcres.gyhhy.MXLib.yggdrasil.beans.Properties;
import cn.mcres.gyhhy.MXLib.yggdrasil.beans.Textures;
import cn.mcres.gyhhy.MXLib.yggdrasil.beans.UnsignedUUID;
import cn.mcres.gyhhy.MXLib.yggdrasil.tt.RD;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JsonHelper {

    public static Gson gson = RD.ft(new GsonBuilder())
            .setPrettyPrinting()
            .registerTypeAdapterFactory(TypeAdapters.newFactory(UnsignedUUID.class, UnsignedUUIDTypeAdapter.i))
            .registerTypeAdapterFactory(TypeAdapters.newFactory(Properties.class, PropertiesTypeAdapter.i))
            .registerTypeAdapterFactory(TypeAdapters.newFactory(Profile.class, ProfileTypeAdapter.i))
            .registerTypeAdapterFactory(TypeAdapters.newFactory(Textures.class, TexturesTypeAdapter.i))
            .create();

    public static JsonWriter inlineWriter(Writer writer) throws IOException {
        return new JsonWriter(writer);
    }

    public static void main(String... rags) {

//        Map mp = gson.fromJson("{}", Map.class);
//        System.out.println(mp);
        System.out.println(
                gson.toJson(
                        new Properties((Map) System.getProperties())
                )
        );
    }

    public static class ProfileTypeAdapter extends TypeAdapter<Profile> {

        public static final ProfileTypeAdapter i = new ProfileTypeAdapter();

        @Override
        public void write(JsonWriter out, Profile value) throws IOException {
            out.beginObject();
            UnsignedUUIDTypeAdapter.i.write(out.name("id"), value.id);
            out.name("name").value(value.name);
            if (value.properties != null) {
                PropertiesTypeAdapter.i.write(out.name("properties"), value.properties);
            }
            out.endObject();
        }

        @Override
        public Profile read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            in.beginObject();
            Profile pf = new Profile();
            while (in.hasNext()) {
                String name = in.nextName();
                switch (name) {
                    case "id": {
                        pf.id = UnsignedUUIDTypeAdapter.i.read(in);
                        break;
                    }
                    case "name": {
                        pf.name = in.nextString();
                        break;
                    }
                    case "properties": {
                        Properties pp = pf.properties = PropertiesTypeAdapter.i.read(in);
                        if (pp.containsKey("textures")) {
                            String b64 = pp.get("textures");
                            String data = (Base64Actuator.getInstance().decodeToString(b64));
                            JsonReader jr = new JsonReader(new StringReader(data));
//                            System.out.println(data);
//                            jr.setLenient(true);
                            pf.textures = TexturesTypeAdapter.i.read(jr);
                        }
                        break;
                    }
                    default:
                        in.skipValue();
                }
            }
            in.endObject();
            return pf;
        }
    }

    public static class PropertiesTypeAdapter extends TypeAdapter<Properties> {

        public static final PropertiesTypeAdapter i = new PropertiesTypeAdapter();

        @Override
        public void write(JsonWriter out, Properties value) throws IOException {
            out.beginArray();
            Set<Map.Entry<String, String>> es = value.entrySet();
            for (Map.Entry<String, String> entry : es) {
                out.beginObject()
                        .name("name").value(entry.getKey())
                        .name("value").value(entry.getValue());
                String sign = value.getSign(entry.getKey());
                if (sign != null) {
                    out.name("signature").value(sign);
                }
                out.endObject();
            }
            out.endArray();
        }

        private void read(JsonReader in, Properties hm) throws IOException {
            JsonToken jt = in.peek();
            if (jt == null) {
                in.nextNull();
                return;
            }
            String name = null;
            String value = null;
            String sign = null;
            in.beginObject();
            while (in.hasNext()) {
                String nt = in.nextName();
                switch (nt) {
                    case "name": {
                        name = in.nextString();
                        break;
                    }
                    case "value": {
                        value = in.nextString();
                        break;
                    }
                    case "signature": {
                        sign = in.nextString();
                        break;
                    }
                    default: {
                        throw new JsonSyntaxException("Properties element should not have other item: " + nt);
                    }
                }
            }
            if (name == null || value == null) {
                throw new JsonSyntaxException("Invalid Properties element");
            }
            String replace = hm.put(name, value);
            if (replace != null) {
                throw new JsonSyntaxException("duplicate key: " + name);
            }
            if (sign != null) {
                hm.setSign(name, sign);
            }
            in.endObject();
        }

        @Override
        public Properties read(JsonReader in) throws IOException {
            JsonToken jt = in.peek();
            if (jt == null) {
                in.nextNull();
                return null;
            } else if (jt != JsonToken.BEGIN_ARRAY) {
                throw new JsonSyntaxException("Invalid bitset value type: " + jt);
            }
            in.beginArray();
            Properties hm = new Properties(new HashMap<>());
            while (in.hasNext()) {
                read(in, hm);
            }
            in.endArray();
            return hm;
        }

    }

    public static class UnsignedUUIDTypeAdapter extends TypeAdapter<UnsignedUUID> {

        public static final UnsignedUUIDTypeAdapter i = new UnsignedUUIDTypeAdapter();

        @Override
        public void write(JsonWriter out, UnsignedUUID value) throws IOException {
            out.value(value == null ? null : String.valueOf(value));
        }

        @Override
        public UnsignedUUID read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            return UnsignedUUID.parse(in.nextString());
        }

    }

    public static class TexturesTypeAdapter extends TypeAdapter<Textures> {

        public static final TexturesTypeAdapter i = new TexturesTypeAdapter();

        @Override
        public void write(JsonWriter out, Textures value) throws IOException {
            out.beginObject()
                    .name("timestamp").value(value.timestamp)
                    .name("profileName").value(value.profileName);
            UnsignedUUIDTypeAdapter.i.write(out.name("profileId"), value.profileId);
            out.name("textures").beginObject();
            write(out, value.skin, "SKIN");
            write(out, value.cape, "CAPE");
            out.endObject().name("signatureRequired").value(value.signatureRequired).endObject();
        }

        @Override
        public Textures read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            Textures tx = new Textures();
            in.beginObject();
            while (in.hasNext()) {
                String key = in.nextName();
                switch (key) {
                    case "timestamp": {
                        tx.timestamp = in.nextLong();
                        break;
                    }
                    case "profileName": {
                        tx.profileName = in.nextString();
                        break;
                    }
                    case "profileId": {
                        tx.profileId = UnsignedUUIDTypeAdapter.i.read(in);
                        break;
                    }
                    case "textures": {
                        readTextures(tx, in);
                        break;
                    }
                    case "signatureRequired": {
                        tx.signatureRequired = in.nextBoolean();
                        break;
                    }
                    default: {
                        in.skipValue();
                    }
                }
            }
            in.endObject();
            return tx;
        }

        private void readTextures(Textures tx, JsonReader in) throws IOException {
            in.beginObject();
            while (in.hasNext()) {
                String key = in.nextName();
                switch (key) {
                    case "SKIN": {
                        tx.skin = readTexture(tx, in, true);
                        break;
                    }
                    case "CAPE": {
                        tx.cape = readTexture(tx, in, false);
                        break;
                    }
                    default:
                        in.skipValue();
                }
            }
            in.endObject();
        }

        private Textures.Texture readTexture(Textures tx, JsonReader in, boolean a) throws IOException {
            in.beginObject();
            Textures.Texture tt = new Textures.Texture();
            while (in.hasNext()) {
                String key = in.nextName();
                switch (key) {
                    case "url": {
                        tt.url = TypeAdapters.URL.read(in);
                        break;
                    }
                    case "metadata": {
                        Map<String, String> mp = tt.metadata = readMap(in);
                        if (a) {
                            String model = mp.get("model");
                            if (model != null) {
                                tx.model = Textures.Model.from(model);
                            }
                        }
                        break;
                    }
                    default: {
                        in.skipValue();
                    }
                }
            }
            in.endObject();
            return tt;
        }

        private Map<String, String> readMap(JsonReader in) throws IOException {
            in.beginObject();
            HashMap<String, String> s = new HashMap<>();
            while (in.hasNext()) {
                s.put(in.nextName(), in.nextString());
            }
            in.endObject();
            return s;
        }

        private void write(JsonWriter out, Textures.Texture t, String name) throws IOException {
            if (t != null) {
                out.name(name).beginObject();
                TypeAdapters.URL.write(out.name("url"), t.url);
                if (t.metadata != null) {
                    write(out.name("metadata"), t.metadata);
                }
                out.endObject();
            }
        }

        private void write(JsonWriter out, Map<String, String> metadata) throws IOException {
            out.beginObject();
            if (metadata != null) {
                for (Map.Entry<String, String> e : metadata.entrySet()) {
                    out.name(e.getKey()).value(e.getValue());
                }
            }
            out.endObject();
        }
    }
}

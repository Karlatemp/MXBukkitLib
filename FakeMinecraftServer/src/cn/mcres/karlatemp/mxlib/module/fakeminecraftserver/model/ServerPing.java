/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ServerPing.java@author: karlatemp@vip.qq.com: 19-11-29 下午11:11@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.module.fakeminecraftserver.model;

import cn.mcres.karlatemp.mxlib.util.GsonHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.BaseComponentSerializer;
import net.md_5.bungee.chat.ComponentSerializer;

import java.io.IOException;
import java.util.Base64;
import java.util.Collection;
import java.util.UUID;

public class ServerPing {
    public static final Gson formatter = new GsonBuilder()
            .registerTypeAdapter(ServerPing.class, Serializer.INSTANCE)
            .create();
    public BaseComponent motd;
    public byte[] favicon;
    public ServerVersion version;
    public ServerPlayers players;

    public static class ServerVersion {
        /**
         * Server version
         */
        public String name;
        /**
         * Protocol version
         */
        public int protocol;
    }

    public static class ServerPlayers {
        public int max;
        public int online;
        public Collection<SamplePlayer> sample;

        public static class SamplePlayer {
            public String name;
            public UUID id;

            public SamplePlayer() {
            }

            public SamplePlayer(String name, UUID uid) {
                this.name = name;
                this.id = uid;
            }
        }
    }

    public static class Serializer extends TypeAdapter<ServerPing> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public void write(JsonWriter out, ServerPing value) throws IOException {
            out.beginObject();
            if (value.motd != null) {
                out.name("description").value(GsonHelper.toRawString(
                        ComponentSerializer.toString(value.motd)
                ));
            }
            if (value.favicon != null) {
                out.name("favicon").value(
                        "data:image/png;base64," + Base64.getEncoder().encodeToString(value.favicon)
                );
            }
            if (value.version != null) {
                out.name("version").beginObject().name("name").value(value.version.name)
                        .name("protocol").value(value.version.protocol).endObject();
            }
            if (value.players != null) {
                out.name("players").beginObject().name("max").value(value.players.max)
                        .name("online").value(value.players.online).name("sample").beginArray();
                final Collection<ServerPlayers.SamplePlayer> sample = value.players.sample;
                if (sample != null) {
                    for (ServerPlayers.SamplePlayer sp : sample) {
                        if (sp != null) {
                            out.beginObject().name("name").value(sp.name)
                                    .name("id").value(String.valueOf(sp.id))
                                    .endObject();
                        }
                    }
                }
                out.endArray().endObject();
            }
            out.endObject();
        }

        @Override
        public ServerPing read(JsonReader in) throws IOException {
            return null;
        }
    }
}

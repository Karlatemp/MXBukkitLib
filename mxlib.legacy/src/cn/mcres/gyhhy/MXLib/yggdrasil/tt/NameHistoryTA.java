/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: NameHistoryTA.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.yggdrasil.tt;

import cn.mcres.gyhhy.MXLib.yggdrasil.beans.mojang.NameHistory;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class NameHistoryTA extends TypeAdapter<NameHistory> {
    static final NameHistoryTA i = new NameHistoryTA();
    @Override
    public void write(JsonWriter out, NameHistory value) throws IOException {
        NameHistory.NameHistoryElement[] els = value.getElements();
        out.beginArray();
        for (NameHistory.NameHistoryElement a : els) {
            out.beginObject().name("name").value(a.getName());
            long time = a.getNameAt();
            if (time != 0) {
                out.name("changedToAt").value(time);
            }
            out.endObject();
        }
        out.endArray();
    }

    @Override
    public NameHistory read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        in.beginArray();
        List<NameHistory.NameHistoryElement> list = new ArrayList<>();
        while (in.hasNext()) {
            readElement(list, in);
        }
        in.endArray();
        return new NameHistory(list.toArray(new NameHistory.NameHistoryElement[list.size()]));
    }

    private void readElement(List<NameHistory.NameHistoryElement> list, JsonReader in) throws IOException {
        in.beginObject();
        //[{"name":"outtime_"},{"name":"outtime","changedToAt":1439990640000}]
        String name = null;
        long time = 0;
        while (in.hasNext()) {
            String n = in.nextName();
            switch (n) {
                case "name": {
                    name = in.nextString();
                    break;
                }
                case "changedToAt": {
                    time = in.nextLong();
                    break;
                }
            }
        }
        in.endObject();
        list.add(new NameHistory.NameHistoryElement(name, time));
    }

}

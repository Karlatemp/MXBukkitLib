/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: FMTA.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.yggdrasil.tt;

import cn.mcres.gyhhy.MXLib.yggdrasil.beans.FailedMessage;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FMTA extends TypeAdapter<FailedMessage> {

    public static final FMTA i = new FMTA();

    public boolean check(JsonReader reader) throws IOException, IllegalStateException {
        List<String> values = new ArrayList<>();
        if (reader.peek() != JsonToken.BEGIN_OBJECT) {
            return false;
        }
        reader.beginObject();
        while (reader.hasNext()) {
            values.add(reader.nextName());
            reader.skipValue();
        }
        reader.endObject();
        return values.stream().allMatch(s -> {
            switch (s) {
                case "error":
                case "errorMessage":
                case "cause":
                    return true;
            }
            return false;
        });
    }

    @Override
    public void write(JsonWriter out, FailedMessage value) throws IOException {
        if (value == null) {
            out.nullValue();
        }
        out.beginObject();
        String s = value.error;
        if (s != null) {
            out.name("error").value(value.error);
        }
        s = value.errorCause;
        if (s != null) {
            out.name("cause").value(s);
        }
        s = value.errorMsg;
        if (s != null) {
            out.name("errorMessage").value(s);
        }
        out.endObject();
    }

    @Override
    public FailedMessage read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        in.beginObject();
        FailedMessage fm = new FailedMessage();
        while (in.hasNext()) {
            String n = in.nextName();
            switch (n) {
                case "error": {
                    fm.error = in.nextString();
                    break;
                }
                case "errorMessage": {
                    fm.errorMsg = in.nextString();
                    break;
                }
                case "cause": {
                    fm.errorCause = in.nextString();
                    break;
                }
                default:
                    in.skipValue();
            }
        }
        in.endObject();
        return fm;
    }

}

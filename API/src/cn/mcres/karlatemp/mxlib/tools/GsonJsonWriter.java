/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: GsonJsonWriter.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tools;

import com.google.gson.Gson;

import java.io.IOException;

public class GsonJsonWriter implements IJsonWriter {
    private final Gson gson;

    public GsonJsonWriter(Gson g) {
        this.gson = g;
    }

    public GsonJsonWriter() {
        this(new Gson());
    }

    @Override
    public void write(Appendable appendable, Object obj) throws IOException {
        gson.toJson(obj, appendable);
    }
}

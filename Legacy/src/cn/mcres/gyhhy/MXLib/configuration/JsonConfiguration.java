/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: JsonConfiguration.java@author: karlatemp@vip.qq.com: 19-9-11 下午1:45@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.configuration;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

/**
 * @version 1.10
 */
public class JsonConfiguration extends FileConfiguration {

    public static boolean support() {
        return true;
    }

    private final JsonConstructor jc;

    @Override
    public void load(Reader reader) throws IOException, InvalidConfigurationException {
        try {
            this.jc.load(reader, this);
        } catch (JsonIOException thr) {
            throw new IOException(thr.getLocalizedMessage(), thr);
        } catch (JsonSyntaxException thr) {
            throw new InvalidConfigurationException(thr.getLocalizedMessage(), thr);
        }
    }

    @Override
    public void save(File file) throws IOException {
        super.save(file); //To change body of generated methods, choose Tools | Templates.
    }

    public JsonConfiguration() {
        this(new JsonConstructor(false));
    }

    public JsonConfiguration(boolean pretty) {
        this(new JsonConstructor(pretty));
    }

    @Deprecated
    public JsonConfiguration(Gson gson) {
        this(new JsonConstructor(gson));
    }

    public JsonConfiguration(@NotNull JsonConstructor jc) {
        this.jc = jc;
    }

    @Override
    public String saveToString() {
        return jc.build(this, map);
    }

    @Override
    public void loadFromString(String contents) throws InvalidConfigurationException {
        try {
            this.jc.load(contents, this);
        } catch (JsonSyntaxException thr) {
            throw new InvalidConfigurationException(thr.getLocalizedMessage(), thr);
        }
    }

    @Override
    protected String buildHeader() {
        return this.jc.buildHeader(this);
    }
}

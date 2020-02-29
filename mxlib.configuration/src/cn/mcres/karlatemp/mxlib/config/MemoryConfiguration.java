/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/29 15:57:22
 *
 * MXLib/mxlib.configuration/MemoryConfiguration.java
 */

package cn.mcres.karlatemp.mxlib.config;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Map;

public abstract class MemoryConfiguration extends MemoryConfigurationSection implements Configuration {
    protected String header;

    public MemoryConfiguration() {
    }

    protected MemoryConfiguration(Void us) {
        super(us);
    }

    protected MemoryConfiguration(@NotNull Map<String, Object> values) {
        super(values);
    }

    @Override
    public void load(InputStream stream) throws IOException {
        try (var reader = new InputStreamReader(new UnclosedInputStream(stream))) {
            load(reader);
        }
    }

    @Override
    public void store(OutputStream stream) throws IOException {
        try (var writer = new OutputStreamWriter(new UnclosedOutputStream(stream))) {
            store(writer);
        }
    }

    @Override
    public String header() {
        return header;
    }

    @Override
    protected abstract MemoryConfiguration newInstance();

    @Override
    public @NotNull MemoryConfiguration newContext() {
        return (MemoryConfiguration) super.newContext();
    }

    @Override
    public @NotNull Configuration clone() {
        return (MemoryConfiguration) super.clone();
    }

    @Override
    public Configuration header(String header) {
        this.header = header;
        return this;
    }
}

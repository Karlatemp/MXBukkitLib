/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: BooleanAdapterFactory.java@author: karlatemp@vip.qq.com: 2019/12/27 下午5:24@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.codec.xml.internal;

import org.jetbrains.annotations.NotNull;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.lang.reflect.Type;

public class BooleanAdapterFactory extends StringAdapterFactory {
    @Override
    public boolean accept(Type type) {
        return type == boolean.class || type == Boolean.class;
    }

    @Override
    public void writeObject(Object msg, @NotNull XMLStreamWriter writer) throws IOException, XMLStreamException {
        super.writeObject(String.valueOf(msg), writer);
    }

    @Override
    public Object readObject(@NotNull XMLStreamReader reader) throws IOException, XMLStreamException {
        return Boolean.parseBoolean(String.valueOf(super.readObject(reader)));
    }
}

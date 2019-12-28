/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: StringAdapterFactory.java@author: karlatemp@vip.qq.com: 2019/12/27 下午4:39@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.codec.xml.internal;

import cn.mcres.karlatemp.mxlib.codec.xml.XmlAdapter;
import cn.mcres.karlatemp.mxlib.codec.xml.XmlAdapterFactory;
import cn.mcres.karlatemp.mxlib.codec.xml.XmlProvider;
import org.jetbrains.annotations.NotNull;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.lang.reflect.Type;

public class StringAdapterFactory implements XmlAdapterFactory, XmlAdapter {
    @Override
    public boolean accept(Type type) {
        return type == String.class;
    }

    @Override
    public void writeObject(Object msg, @NotNull XMLStreamWriter writer) throws IOException, XMLStreamException {
        writer.writeCharacters(String.valueOf(msg));
    }

    @Override
    public Object readObject(@NotNull XMLStreamReader reader) throws IOException, XMLStreamException {
        while (reader.getEventType() != XMLStreamConstants.START_ELEMENT) {
            reader.next();
        }
        return reader.getElementText();
    }

    @Override
    public XmlAdapter getAdapter(@NotNull XmlProvider provider, Type type) {
        if (accept(type))
            return this;
        return null;
    }
}

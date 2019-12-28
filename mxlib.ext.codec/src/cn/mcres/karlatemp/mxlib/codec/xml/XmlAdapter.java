/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: XmlAdapter.java@author: karlatemp@vip.qq.com: 2019/12/27 下午4:12@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.codec.xml;

import org.jetbrains.annotations.NotNull;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.lang.reflect.Type;

public interface XmlAdapter {
    default boolean accept(Object msg) {
        if (msg == null) return accept((Type) null);
        return accept(msg.getClass());
    }

    boolean accept(Type type);

    void writeObject(Object msg, @NotNull XMLStreamWriter writer) throws IOException, XMLStreamException;

    Object readObject(@NotNull XMLStreamReader reader) throws IOException, XMLStreamException;
}

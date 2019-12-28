/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ReflectionAdapterFactory.java@author: karlatemp@vip.qq.com: 2019/12/27 下午4:18@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.codec.xml.internal;

import cn.mcres.karlatemp.mxlib.codec.xml.XmlAdapter;
import cn.mcres.karlatemp.mxlib.codec.xml.XmlAdapterFactory;
import cn.mcres.karlatemp.mxlib.codec.xml.XmlProvider;
import cn.mcres.karlatemp.mxlib.exceptions.MessageDump;
import cn.mcres.karlatemp.mxlib.tools.Toolkit;
import cn.mcres.karlatemp.mxlib.tools.Unsafe;
import org.jetbrains.annotations.NotNull;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ReflectionAdapterFactory implements XmlAdapterFactory {
    @Override
    public XmlAdapter getAdapter(@NotNull XmlProvider provider, Type type) {
        if (type instanceof Class<?> && !((Class<?>) type).isArray() && !((Class<?>) type).isInterface() && !Modifier.isAbstract(((Class<?>) type).getModifiers()))
            return new Adapter(type, provider);
        return null;
    }

    private static class Adapter implements XmlAdapter {
        private final Class<?> type;
        private final Map<String, XmlAdapter> adapterMap = new HashMap<>();
        private final Map<String, Field> fieldMap = new HashMap<>();
        private final XmlProvider provider;

        public Adapter(Type type, XmlProvider provider) {
            this.provider = provider;

            Class<?> dumping = this.type = (Class<?>) type;
            do {
                for (Field f : dumping.getDeclaredFields()) {
                    if (adapterMap.containsKey(f.getName())) {
                        // ???
                    } else {
                        int mod = f.getModifiers();
                        if (Modifier.isStatic(mod) || Modifier.isTransient(mod)) continue;
                        adapterMap.put(f.getName(), provider.getAdapter(f.getType()));
                        fieldMap.put(f.getName(), f);
                    }
                }
                dumping = dumping.getSuperclass();
            } while (dumping != null);
        }

        @Override
        public boolean accept(Type type) {
            return this.type == type;
        }

        @Override
        public void writeObject(Object msg, @NotNull XMLStreamWriter writer) throws IOException, XMLStreamException {
            if (accept(msg)) {
                for (Map.Entry<String, XmlAdapter> a : adapterMap.entrySet()) {
                    writer.writeStartElement(a.getKey());
                    a.getValue().writeObject(Toolkit.Reflection.getObjectValue(msg, fieldMap.get(a.getKey())), writer);
                    writer.writeEndElement();
                }
            }
        }

        @Override
        public Object readObject(@NotNull XMLStreamReader reader) throws XMLStreamException, IOException {
            Object result;
            try {
                result = Unsafe.getUnsafe().allocateInstance(type);
            } catch (InstantiationException e) {
                throw new IOException(e);
            }
            try {
                reader.next();
                while (reader.hasNext()) {
                    int current = reader.getEventType();
                    if (current == XMLStreamConstants.START_ELEMENT) {
                        String loc = reader.getLocalName();
                        if (adapterMap.containsKey(loc)) {
                            Toolkit.Reflection.setObjectValue(result, fieldMap.get(loc), adapterMap.get(loc).readObject(reader));
                        }
                        if (reader.getEventType() == XMLStreamConstants.END_ELEMENT) {
                            reader.next();
                            continue;
                        }
                        if (reader.getEventType() == XMLStreamConstants.END_DOCUMENT) {
                            throw new IOException("No Ending,,,,");
                        }
                        int i = 0;
                        rt:
                        while (true) {
                            switch (reader.next()) {
                                case XMLStreamConstants.START_ELEMENT: {
                                    i++;
                                    break;
                                }
                                case XMLStreamConstants.END_ELEMENT: {
                                    if (i-- == 0) break rt;
                                    break;
                                }
                            }
                        }
                        if (reader.getEventType() != XMLStreamConstants.END_ELEMENT) {
                            throw new IOException("XML Field not closed? " + reader.getLocation());
                        }
                        reader.next();
                    } else if (current == XMLStreamConstants.END_ELEMENT) {
                        return result;
                    } else {
                        reader.next();
                    }
                }
            } catch (Throwable t) {
                t.addSuppressed(MessageDump.create(String.valueOf(result + ", " + result.getClass())));
                throw t;
            }
            return result;
        }
    }
}

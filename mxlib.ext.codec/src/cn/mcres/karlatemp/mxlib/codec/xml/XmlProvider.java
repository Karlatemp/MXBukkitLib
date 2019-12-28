/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: XmlProvider.java@author: karlatemp@vip.qq.com: 2019/12/27 下午4:12@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.codec.xml;

import cn.mcres.karlatemp.mxlib.codec.xml.internal.BooleanAdapterFactory;
import cn.mcres.karlatemp.mxlib.codec.xml.internal.ReflectionAdapterFactory;
import cn.mcres.karlatemp.mxlib.codec.xml.internal.StringAdapterFactory;

import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentLinkedQueue;

public class XmlProvider {
    protected ConcurrentLinkedQueue<XmlAdapterFactory> userFactories = new ConcurrentLinkedQueue<>();
    private static ConcurrentLinkedQueue<XmlAdapterFactory> sysFactories = new ConcurrentLinkedQueue<>();

    static {
        sysFactories.add(new StringAdapterFactory());
        sysFactories.add(new BooleanAdapterFactory());
        sysFactories.add(new ReflectionAdapterFactory());
    }

    public void addFactory(XmlAdapterFactory factory) {
        userFactories.add(factory);
    }

    public XmlAdapter getAdapter(Type type) {
        for (XmlAdapterFactory f : userFactories) {
            final XmlAdapter adapter = f.getAdapter(this, type);
            if (adapter != null) return adapter;
        }
        for (XmlAdapterFactory f : sysFactories) {
            final XmlAdapter adapter = f.getAdapter(this, type);
            if (adapter != null) return adapter;
        }
        throw new UnsupportedOperationException("No factory for " + type);
    }
}

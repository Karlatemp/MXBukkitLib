/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PW.java@author: karlatemp@vip.qq.com: 2019/12/27 下午4:06@version: 2.0
 */

package cn.mcres.karlatemp.mxli.exts.Java12DocGenerator;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLInputFactory;
import java.io.Reader;

public class PW {
    public static void main(String[] args) throws Throwable {
        XMLStreamReader reader = XMLInputFactory.newDefaultFactory().createXMLStreamReader(Reader.nullReader());
    }
}

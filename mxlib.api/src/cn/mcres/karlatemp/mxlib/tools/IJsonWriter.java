/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: IJsonWriter.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.tools;

import java.io.IOException;
import java.io.StringWriter;

public interface IJsonWriter {
    void write(Appendable appendable, Object obj) throws IOException;

    default String write(Object obj) throws IOException {
        StringWriter sw = new StringWriter();
        write(sw, obj);
        return sw.toString();
    }
}

/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: DataPrinter.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */
package cn.mcres.gyhhy.MXLib.printer;

import java.io.IOException;

/**
 *
 * @author Karlatemp
 */
public interface DataPrinter {

    default void print(Object object, CharSequence prefix, Appendable append) throws IOException{
        print(object, prefix, 0, append);
    }

    void print(Object object, CharSequence prefix, int count, Appendable append)throws IOException;
}

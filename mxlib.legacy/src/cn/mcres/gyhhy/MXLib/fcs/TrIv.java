/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: TrIv.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.gyhhy.MXLib.fcs;

import java.io.IOException;

public interface TrIv<T, R> {

    R run(T tt) throws IOException;
}

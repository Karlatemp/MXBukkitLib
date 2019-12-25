/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Callback.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.yggdrasil.callbacks;

import cn.mcres.gyhhy.MXLib.yggdrasil.beans.FailedMessage;
import java.io.IOException;
import static java.nio.charset.StandardCharsets.UTF_8;

public interface Callback<T> {

    void onSuccessful(T t, int NetCode) throws IOException;

    void onFailed(FailedMessage err, int NetCode) throws IOException;

    default void onFailed(byte[] datas, int http_code) throws IOException {
        System.err.println("Error... [" + http_code + "]" + new String(datas, UTF_8));
    }

    default void onError(Throwable thrown) {
        thrown.printStackTrace(System.err);
    }
}

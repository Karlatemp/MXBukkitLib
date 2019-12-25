/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MXTask.java@author: karlatemp@vip.qq.com: 19-11-9 下午5:04@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.scheduler;

import java.util.concurrent.Future;

public interface MXTask<T> {
    boolean isSync();

    boolean isCancelled();

    void cancel();

    void hiddenExceptions();

    Future<T> getFuture();

    MXTaskState getState();
}

/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Cancellable.java@author: karlatemp@vip.qq.com: 19-12-21 下午5:35@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.event;

/**
 * Cancellable event.
 *
 * @since 2.9
 */
public interface Cancellable {
    boolean isCancelled();

    void setCancelled(boolean isCancelled);

    default void cancel() {
        setCancelled(true);
    }
}

/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: EventHandler.java@author: karlatemp@vip.qq.com: 19-9-27 下午1:26@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.event;

import cn.mcres.karlatemp.mxlib.annotations.ProhibitBean;

@ProhibitBean
public interface EventHandler<T extends Event> {
    default int priority() {
        return 5;
    }

    default void forcePost(Event event) {
        try {
            //noinspection unchecked
            post((T) event);
        } catch (ClassCastException ignore) {
        }
    }

    void post(T event);
}

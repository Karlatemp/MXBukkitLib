/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: MXLibBootEvent.java@author: karlatemp@vip.qq.com: 19-9-27 下午2:03@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.event.core;

import cn.mcres.karlatemp.mxlib.MXLibBootProvider;
import cn.mcres.karlatemp.mxlib.event.Event;
import cn.mcres.karlatemp.mxlib.event.HandlerList;

import java.util.Collection;

public class MXLibBootEvent extends Event {
    public static final HandlerList<MXLibBootEvent> handlers = new HandlerList<>();
    private Collection<MXLibBootProvider> providers;

    public MXLibBootEvent(Collection<MXLibBootProvider> providers) {
        this.providers = providers;
    }

    @Override
    public HandlerList<MXLibBootEvent> getHandlerList() {
        return handlers;
    }

    public Collection<MXLibBootProvider> getProviders() {
        return providers;
    }

    public void setProviders(Collection<MXLibBootProvider> providers) {
        this.providers = providers;
    }
}

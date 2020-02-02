/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: TranslatableMessage.java@author: karlatemp@vip.qq.com: 2020/1/30 下午8:01@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.util;

import cn.mcres.karlatemp.mxlib.MXBukkitLib;
import cn.mcres.karlatemp.mxlib.bean.IBeanManager;
import cn.mcres.karlatemp.mxlib.translate.MTranslate;
import cn.mcres.karlatemp.mxlib.translate.SystemTranslate;
import com.mojang.brigadier.Message;

public class TranslatableMessage implements Message {
    public static final ThreadLocal<MTranslate> TRANSLATE = new ThreadLocal<>();
    private final String message;
    private final Object[] params;
    private transient MTranslate translate;

    public MTranslate getTranslate() {
        return translate;
    }

    public void setTranslate(MTranslate translate) {
        this.translate = translate;
    }

    public TranslatableMessage(String message, Object... params) {
        this.message = message;
        this.params = params;
    }

    @Override
    public String getString() {
        return getMessage();
    }

    public String getMessage() {
        if (translate != null)
            return translate.asMessage(message, params);
        final IBeanManager manager = MXBukkitLib.getBeanManager();
        if (manager == null) return message;
        final MTranslate translate = TRANSLATE.get();
        if (translate != null) return translate.asMessage(message, params);
        final SystemTranslate systemTranslate = manager.getBean(SystemTranslate.class);
        if (systemTranslate != null)
            return systemTranslate.asMessage(message, params);
        return message;
    }
}

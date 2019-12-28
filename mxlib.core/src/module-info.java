/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: module-info.java@author: karlatemp@vip.qq.com: 2019/12/26 下午6:39@version: 2.0
 */
/**
 * There is API.interfaces default implements.
 */
open module mxlib.core {
    exports cn.mcres.karlatemp.mxlib.shared;
    requires mxlib.api;
    requires org.jetbrains.annotations;
    requires org.javassist;
    requires java.instrument;
    requires java.logging;
    requires com.google.gson;
    provides cn.mcres.karlatemp.mxlib.MXLibBootProvider with
            cn.mcres.karlatemp.mxlib.shared.SharedMXLibBootProvider,
            cn.mcres.karlatemp.mxlib.shared.SharedMXLibBootProvider.AutoConfigurationProcessor;
}
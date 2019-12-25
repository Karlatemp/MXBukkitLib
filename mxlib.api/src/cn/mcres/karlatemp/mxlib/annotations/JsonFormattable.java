/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: JsonFormattable.java@author: karlatemp@vip.qq.com: 19-10-3 下午2:19@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.annotations;

import com.google.gson.stream.JsonWriter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Settings should call {@link Object#toString()} as Json content in {@link cn.mcres.karlatemp.mxlib.util.GsonHelper#write(JsonWriter, Object)}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@SuppressWarnings("SpellCheckingInspection")
public @interface JsonFormattable {
}

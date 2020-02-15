/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 02:36:01
 *
 * MXLib/mxlib.message/STemplateAdapter.java
 */

package cn.mcres.karlatemp.mxlib.codec;

public interface STemplateAdapter {
    STemplate get(Class<?> type);

    STemplate get(Object val);
}

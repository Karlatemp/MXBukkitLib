/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: Renameable.java@author: karlatemp@vip.qq.com: 2020/1/13 下午9:17@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.class_info;

public interface Renameable {

    String getRenamedJavaName();

    String getRenamedInternalName();

    boolean isSupportRename();

    boolean rename(String internalName);
}

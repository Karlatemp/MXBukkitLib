/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/20 21:17:35
 *
 * MXLib/expend.plugin.base/StandardI18n.java
 */

package cn.mcres.karlatemp.mxlib.plugin.base;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class StandardI18n {
    public static final Map<String, String> EN_US, ZH_CN;
    public static final I18n en_us, zh_ch;

    static {
        var map = new HashMap<String, String>();
        map.put("not.a.valid.integer", "\"%s\" not a valid integer.");
        map.put("not.a.valid.float", "\"%s\" not a valid float.");
        map.put("not.a.valid.double", "\"%s\" not a valid double.");
        map.put("not.a.valid.long", "\"%s\" not a valid long.");
        map.put("not.a.valid.short", "\"%s\" not a valid byte.");
        map.put("argument.missing", "Need %s arguments or more.");
        map.put("world.not.found", "Unknown world \"%s\"");
        map.put("player.not.found", "Unknown player \"%s\"");
        map.put("payload", "%s");
        map.put("no.permission", "Sorry, but you don't have permission to perform that.");
        en_us = new I18n(EN_US = Collections.unmodifiableMap(map));
        var map2 = new HashMap<>(map);
        map.put("not.a.valid.integer", "\"%s\" 不是一个有效的整数.");
        map.put("not.a.valid.float", "\"%s\" 不是一个有效的浮点数.");
        map.put("not.a.valid.double", "\"%s\" 不是一个有效的双浮点数.");
        map.put("not.a.valid.long", "\"%s\" 不是一个有效的长整数.");
        map.put("not.a.valid.short", "\"%s\" 不是一个有效的字节.");
        map.put("argument.missing", "需要 %s (或以上) 个参数.");
        map.put("world.not.found", "未知世界 \"%s\"");
        map.put("player.not.found", "未知玩家 \"%s\"");
        zh_ch = new I18n(ZH_CN = Collections.unmodifiableMap(map2));
    }
}

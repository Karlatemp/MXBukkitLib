/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: PacketFormatter.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.bukkit;

public interface PacketFormatter {
    String format(String message);

    PacketFormatter RAW = s -> s;
    PacketFormatter DEFAULT = s -> "{\"text\":\"" + s + "\"}";
    PacketFormatter LOSSLESS = s -> {
        if (s == null) s = "";
        StringBuilder sb = new StringBuilder(s.length() + 10);
        sb.append("{\"text\":\"");
        for (char c : s.toCharArray()) {
            switch (c) {
                case '\\':
                case '\"':
                case '\'': {
                    sb.append('\\').append(c);
                    break;
                }
                case '\t': {
                    sb.append('\\').append('t');
                    break;
                }
                case '\r': {
                    sb.append('\\').append('r');
                    break;
                }
                case '\n': {
                    sb.append('\\').append('n');
                    break;
                }
                default:
                    sb.append(c);
            }
        }
        return sb.append("\"}").toString();
    };
}

/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: BStatsProvider.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.bstats;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public interface BStatsProvider {

    int getPlayerAmount();

    boolean isOnlineMode();

    String getBukkitVersion();

    String getBukkitName();

    void onCreateBStats(BStats bstats);

    List<Map<String, Object>> getPluginDataList();

    void sendData(String url, Map<String, Object> data);

    Logger logger();

}

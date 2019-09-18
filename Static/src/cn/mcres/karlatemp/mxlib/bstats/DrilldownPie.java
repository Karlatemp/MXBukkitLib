/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: DrilldownPie.java@author: karlatemp@vip.qq.com: 19-9-17 下午9:37@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.bstats;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Represents a custom drilldown pie.
 */
public class DrilldownPie extends CustomChart {

    private final Callable<Map<String, Map<String, Integer>>> callable;

    /**
     * Class constructor.
     *
     * @param chartId  The id of the chart.
     * @param callable The callable which is used to request the chart data.
     */
    public DrilldownPie(String chartId, Callable<Map<String, Map<String, Integer>>> callable) {
        super(chartId);
        this.callable = callable;
    }

    @Override
    public Map<String, Object> getChartData() throws Exception {
        Map<String, Object> data = new LinkedHashMap<>(),
                values = new LinkedHashMap<>();
        Map<String, Map<String, Integer>> map = callable.call();
        if (map == null || map.isEmpty()) {
            // Null = skip the chart
            return null;
        }
        boolean reallyAllSkipped = true;
        for (Map.Entry<String, Map<String, Integer>> entryValues : map.entrySet()) {
            Map<String, Object> value = new LinkedHashMap<>();
            boolean allSkipped = true;
            for (Map.Entry<String, Integer> valueEntry : map.get(entryValues.getKey()).entrySet()) {
                value.put(valueEntry.getKey(), valueEntry.getValue());
                allSkipped = false;
            }
            if (!allSkipped) {
                reallyAllSkipped = false;
                values.put(entryValues.getKey(), value);
            }
        }
        if (reallyAllSkipped) {
            // Null = skip the chart
            return null;
        }
        data.put("values", values);
        return data;
    }
}

/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: AdvancedPie.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.bstats;


import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Represents a custom advanced pie.
 */
public class AdvancedPie extends CustomChart {

    private final Callable<Map<String, Integer>> callable;

    /**
     * Class constructor.
     *
     * @param chartId  The id of the chart.
     * @param callable The callable which is used to request the chart data.
     */
    public AdvancedPie(String chartId, Callable<Map<String, Integer>> callable) {
        super(chartId);
        this.callable = callable;
    }

    @Override
    protected Map<String, Object> getChartData() throws Exception {
        Map<String, Object> data = new LinkedHashMap<>(),
                values = new LinkedHashMap<>();
        Map<String, Integer> map = callable.call();
        if (map == null || map.isEmpty()) {
            // Null = skip the chart
            return null;
        }
        boolean allSkipped = true;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 0) {
                continue; // Skip this invalid
            }
            allSkipped = false;
            values.put(entry.getKey(), entry.getValue());
        }
        if (allSkipped) {
            // Null = skip the chart
            return null;
        }
        data.put("values", values);
        return data;
    }
}

/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: AdvancedBarChart.java@author: karlatemp@vip.qq.com: 19-9-17 下午9:44@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.bstats;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Represents a custom advanced bar chart.
 */
public class AdvancedBarChart extends CustomChart {

    private final Callable<Map<String, int[]>> callable;

    /**
     * Class constructor.
     *
     * @param chartId  The id of the chart.
     * @param callable The callable which is used to request the chart data.
     */
    public AdvancedBarChart(String chartId, Callable<Map<String, int[]>> callable) {
        super(chartId);
        this.callable = callable;
    }

    @Override
    protected Map<String, Object> getChartData() throws Exception {
        Map<String, Object> data = new LinkedHashMap<>(),
                values = new LinkedHashMap<>();
        Map<String, int[]> map = callable.call();
        if (map == null || map.isEmpty()) {
            // Null = skip the chart
            return null;
        }
        boolean allSkipped = true;
        for (Map.Entry<String, int[]> entry : map.entrySet()) {
            if (entry.getValue().length == 0) {
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

/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: SimpleBarChart.java@author: karlatemp@vip.qq.com: 19-9-17 下午9:41@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.bstats;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Represents a custom simple bar chart.
 */
public class SimpleBarChart extends CustomChart {

    private final Callable<Map<String, Integer>> callable;

    /**
     * Class constructor.
     *
     * @param chartId  The id of the chart.
     * @param callable The callable which is used to request the chart data.
     */
    public SimpleBarChart(String chartId, Callable<Map<String, Integer>> callable) {
        super(chartId);
        this.callable = callable;
    }


    @Override
    protected Map<String, Object> getChartData() throws Exception {
        Map<String, Object> data = new LinkedHashMap<>(), values = new LinkedHashMap<>();
        Map<String, Integer> map = callable.call();
        if (map == null || map.isEmpty()) {
            // Null = skip the chart
            return null;
        }
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            List<Integer> categoryValues = new ArrayList<>();
            categoryValues.add(entry.getValue());
            values.put(entry.getKey(), categoryValues);
        }
        data.put("values", values);
        return data;
    }

}

/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: SingleLineChart.java@author: karlatemp@vip.qq.com: 19-9-17 下午9:39@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.bstats;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Represents a custom single line chart.
 */
public class SingleLineChart extends CustomChart {

    private final Callable<Integer> callable;

    /**
     * Class constructor.
     *
     * @param chartId  The id of the chart.
     * @param callable The callable which is used to request the chart data.
     */
    public SingleLineChart(String chartId, Callable<Integer> callable) {
        super(chartId);
        this.callable = callable;
    }

    @Override
    protected Map<String,Object> getChartData() throws Exception {
        Map<String,Object> data = new HashMap<>();
        int value = callable.call();
        if (value == 0) {
            // Null = skip the chart
            return null;
        }
        data.put("value", value);
        return data;
    }

}

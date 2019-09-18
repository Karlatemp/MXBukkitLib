/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: SimplePie.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.bstats;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Represents a custom simple pie.
 */
public class SimplePie extends CustomChart {

    private final Callable<String> callable;

    /**
     * Class constructor.
     *
     * @param chartId  The id of the chart.
     * @param callable The callable which is used to request the chart data.
     */
    public SimplePie(String chartId, Callable<String> callable) {
        super(chartId);
        this.callable = callable;
    }

    @Override
    protected Map<String,Object> getChartData() throws Exception {
        Map<String,Object> data = new LinkedHashMap<>();
        String value = callable.call();
        if (value == null || value.isEmpty()) {
            // Null = skip the chart
            return null;
        }
        data.put("value", value);
        return data;
    }
}

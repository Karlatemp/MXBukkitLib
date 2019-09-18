/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: CustomChart.java@author: karlatemp@vip.qq.com: 19-9-17 下午6:40@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.bstats;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Represents a custom chart.
 */
public abstract class CustomChart {

    // The id of the chart
    final String chartId;

    /**
     * Class constructor.
     *
     * @param chartId The id of the chart.
     */
    CustomChart(String chartId) {
        if (chartId == null || chartId.isEmpty()) {
            throw new IllegalArgumentException("ChartId cannot be null or empty!");
        }
        this.chartId = chartId;
    }

    private Map<String, Object> getRequestJsonObject() {
        Map<String, Object> chart = new LinkedHashMap<>();
        chart.put("chartId", chartId);
        try {
            Map<String, Object> data = getChartData();
            if (data == null) {
                // If the data is null we don't send the chart.
                return null;
            }
            chart.put("data", data);
        } catch (Throwable t) {
            if (BStats.logFailedRequests) {
                BStats.provider.logger().log(Level.WARNING, "Failed to get data for custom chart with id " + chartId, t);
            }
            return null;
        }
        return chart;
    }

    protected abstract Map<String, Object> getChartData() throws Exception;

}

package com.ntnikka.modules.orderManager.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName TradeBarChartEntity
 * @Author Liuq
 * @Description todo
 * @Date 2018/9/28 14:25
 **/
public class TradeBarChartEntity implements Serializable {

    private String dateTime;

    private double totalCount;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public double getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(double totalCount) {
        this.totalCount = totalCount;
    }
}

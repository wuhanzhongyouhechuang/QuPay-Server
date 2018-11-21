package com.ntnikka.modules.orderManager.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ntnikka.modules.orderManager.entity.TradeBarChartEntity;
import com.ntnikka.modules.orderManager.entity.TradeOrder;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName TradeOrderDao
 * @Author Liuq
 * @Description todo
 * @Date 2018/9/17 14:50
 **/
@Repository
public interface TradeOrderDao extends BaseMapper<TradeOrder> {
    List<Map<String, String>> queryOrderDataForBarChart(Map map);

    Map<String, String> queryTotalOrderCount(Map map);

    Map<String, String> queryToPayOrderCount(Map map);

    Map<String, String> queryFailOrderCount(Map map);

    Map<String, String> queryAllCountAndSum(Map map);

    Map<String, String> querySuccessCountAndSum(Map map);

    List<Map<String, String>> queryOrderDataForBarChartByMerchant(Map map);

    Map<String, String> queryAllCountAndSumByMerchant(Map map);

    Map<String, String> querySuccessCountAndSumByMerchant(Map map);

    Map<String, String> queryTotalOrderCountByMerchant(Map map);

    Map<String, String> queryToPayOrderCountByMerchant(Map map);

    Map<String, String> queryFailOrderCountByMerchant(Map map);

    List<TradeOrder> queryList(Map map);
}

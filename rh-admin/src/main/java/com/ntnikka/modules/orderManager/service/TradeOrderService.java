package com.ntnikka.modules.orderManager.service;

import com.baomidou.mybatisplus.service.IService;
import com.ntnikka.modules.orderManager.entity.TradeBarChartEntity;
import com.ntnikka.modules.orderManager.entity.TradeOrder;
import com.ntnikka.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * @ClassName TradeOrderService
 * @Author Liuq
 * @Description todo
 * @Date 2018/9/17 14:55
 **/
public interface TradeOrderService extends IService<TradeOrder> {
    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageForMerchant(Map<String, Object> params);

    List<TradeOrder> queryList(Map<String, Object> params);

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

    List<Long> queryMerchantDeptIdList(Map map);
}

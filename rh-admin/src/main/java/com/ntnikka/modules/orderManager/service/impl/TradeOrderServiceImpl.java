package com.ntnikka.modules.orderManager.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ntnikka.common.utils.EmptyUtil;
import com.ntnikka.common.utils.Query;
import com.ntnikka.modules.merchantManager.service.MerchantDeptService;
import com.ntnikka.modules.orderManager.dao.TradeOrderDao;
import com.ntnikka.modules.orderManager.entity.TradeBarChartEntity;
import com.ntnikka.modules.orderManager.entity.TradeOrder;
import com.ntnikka.modules.orderManager.service.TradeOrderService;
import com.ntnikka.utils.PageUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @ClassName TradeOrderServiceImpl
 * @Author Liuq
 * @Description todo
 * @Date 2018/9/17 15:02
 **/
@Service("TradeOrderService")
public class TradeOrderServiceImpl extends ServiceImpl<TradeOrderDao, TradeOrder> implements TradeOrderService {

    @Autowired
    TradeOrderDao tradeOrderDao;

    @Autowired
    private MerchantDeptService merchantDeptService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String tradeId = params.get("tradeid") == null ? "" : params.get("tradeid").toString();
        String orderId = params.get("orderid") == null ? "" : params.get("orderid").toString();
        String merchantId = params.get("merchantid") == null ? "" : params.get("merchantid").toString();
        String status = params.get("status") == null ? "" : params.get("status").toString();
        String star = params.get("starttime") == null ? "" : params.get("starttime").toString();
        String end = params.get("endtime") == null ? "" : params.get("endtime").toString();
        String merchantdept = params.get("merchantdept") == null ? "" : params.get("merchantdept").toString();
        String tradeno = params.get("tradeno") == null ? "" : params.get("tradeno").toString();
        List<Long> idList = new ArrayList<>();
        if (!merchantdept.isEmpty()) {
            idList = merchantDeptService.queryMerchantDeptIdList(Long.parseLong(merchantdept));
        }
        Page<TradeOrder> page = this.selectPage(new Query<TradeOrder>(params).getPage(),
                new EntityWrapper<TradeOrder>()
                        .eq(EmptyUtil.isNotEmpty(tradeId), "sys_trade_no", tradeId)
                        .eq(EmptyUtil.isNotEmpty(orderId), "order_id", orderId)
                        .eq(EmptyUtil.isNotEmpty(merchantId), "merchant_id", merchantId)
                        .eq(EmptyUtil.isNotEmpty(status), "status", status)
                        .eq(EmptyUtil.isNotEmpty(tradeno), "trade_no", tradeno)
                        .in(EmptyUtil.isNotEmpty(idList), "merchant_id", idList)
                        .ge(EmptyUtil.isNotEmpty(star), "create_time", star)
                        .le(EmptyUtil.isNotEmpty(end), "create_time", end));
        return new PageUtils(page);
    }

    @Override
    public List<TradeOrder> queryList(Map<String, Object> params) {
        String tradeId = params.get("tradeid") == null ? "" : params.get("tradeid").toString();
        String orderId = params.get("orderid") == null ? "" : params.get("orderid").toString();
        String merchantId = params.get("merchantid") == null ? "" : params.get("merchantid").toString();
        String status = params.get("status") == null ? "" : params.get("status").toString();
        String star = params.get("starttime") == null ? "" : params.get("starttime").toString();
        String end = params.get("endtime") == null ? "" : params.get("endtime").toString();
        String merchantdept = params.get("merchantdept") == null ? "" : params.get("merchantdept").toString();
//        List<Long> idList = new ArrayList<>();
//        if (!merchantdept.isEmpty()){
//            idList = merchantDeptService.queryMerchantDeptIdList(Long.parseLong(merchantdept));
//        }
//
//        List<TradeOrder> orderList = this.selectList(new EntityWrapper<TradeOrder>()
//                .eq(EmptyUtil.isNotEmpty(tradeId) , "sys_trade_no" , tradeId)
//                .eq(EmptyUtil.isNotEmpty(orderId) , "order_id" , orderId)
//                .eq(EmptyUtil.isNotEmpty(merchantId),"merchant_id", merchantId)
//                .eq(EmptyUtil.isNotEmpty(status), "status", 1)
//                .in(EmptyUtil.isNotEmpty(idList) , "merchant_id" ,idList)
//                .ge(EmptyUtil.isNotEmpty(star), "create_time",star)
//                .le(EmptyUtil.isNotEmpty(end), "create_time",end));
        List<TradeOrder> orderList = tradeOrderDao.queryList(params);
        return orderList;
    }

    @Override
    public List<Map<String, String>> queryOrderDataForBarChart(Map map) {
        return tradeOrderDao.queryOrderDataForBarChart(map);
    }

    @Override
    public Map<String, String> queryTotalOrderCount(Map map) {
        return tradeOrderDao.queryTotalOrderCount(map);
    }

    @Override
    public Map<String, String> queryAllCountAndSum(Map map) {
        return tradeOrderDao.queryAllCountAndSum(map);
    }

    @Override
    public Map<String, String> querySuccessCountAndSum(Map map) {
        return tradeOrderDao.querySuccessCountAndSum(map);
    }

    @Override
    public Map<String, String> queryToPayOrderCount(Map map) {
        return tradeOrderDao.queryToPayOrderCount(map);
    }

    @Override
    public Map<String, String> queryFailOrderCount(Map map) {
        return tradeOrderDao.queryFailOrderCount(map);
    }

    @Override
    public List<Map<String, String>> queryOrderDataForBarChartByMerchant(Map map) {
        String merchantdept = map.get("merchantdept") == null ? "" : map.get("merchantdept").toString();
        List<Long> idList = new ArrayList();
        if (!merchantdept.isEmpty()) {
            idList = merchantDeptService.queryMerchantDeptIdList(Long.parseLong(merchantdept));
        }
        map.put("ids", idList);
        return tradeOrderDao.queryOrderDataForBarChartByMerchant(map);
    }

    @Override
    public Map<String, String> queryAllCountAndSumByMerchant(Map map) {
        String merchantdept = map.get("merchantdept") == null ? "" : map.get("merchantdept").toString();
        List<Long> idList = new ArrayList();
        if (!merchantdept.isEmpty()) {
            idList = merchantDeptService.queryMerchantDeptIdList(Long.parseLong(merchantdept));
        }
        map.put("ids", idList);
        return tradeOrderDao.queryAllCountAndSumByMerchant(map);
    }

    @Override
    public Map<String, String> querySuccessCountAndSumByMerchant(Map map) {
        String merchantdept = map.get("merchantdept") == null ? "" : map.get("merchantdept").toString();
        List<Long> idList = new ArrayList();
        if (!merchantdept.isEmpty()) {
            idList = merchantDeptService.queryMerchantDeptIdList(Long.parseLong(merchantdept));
        }
        map.put("ids", idList);
        return tradeOrderDao.querySuccessCountAndSumByMerchant(map);
    }

    @Override
    public Map<String, String> queryTotalOrderCountByMerchant(Map map) {
        String merchantdept = map.get("merchantdept") == null ? "" : map.get("merchantdept").toString();
        List<Long> idList = new ArrayList();
        if (!merchantdept.isEmpty()) {
            idList = merchantDeptService.queryMerchantDeptIdList(Long.parseLong(merchantdept));
        }
        map.put("ids", idList);
        return tradeOrderDao.queryTotalOrderCountByMerchant(map);
    }

    @Override
    public Map<String, String> queryToPayOrderCountByMerchant(Map map) {
        String merchantdept = map.get("merchantdept") == null ? "" : map.get("merchantdept").toString();
        List<Long> idList = new ArrayList();
        if (!merchantdept.isEmpty()) {
            idList = merchantDeptService.queryMerchantDeptIdList(Long.parseLong(merchantdept));
        }
        map.put("ids", idList);
        return tradeOrderDao.queryToPayOrderCountByMerchant(map);
    }

    @Override
    public Map<String, String> queryFailOrderCountByMerchant(Map map) {
        String merchantdept = map.get("merchantdept") == null ? "" : map.get("merchantdept").toString();
        List<Long> idList = new ArrayList();
        if (!merchantdept.isEmpty()) {
            idList = merchantDeptService.queryMerchantDeptIdList(Long.parseLong(merchantdept));
        }
        map.put("ids", idList);
        return tradeOrderDao.queryFailOrderCountByMerchant(map);
    }

    @Override
    public PageUtils queryPageForMerchant(Map<String, Object> params) {
        String tradeId = params.get("tradeid") == null ? "" : params.get("tradeid").toString();
        String orderId = params.get("orderid") == null ? "" : params.get("orderid").toString();
        String merchantId = params.get("merchantid") == null ? "" : params.get("merchantid").toString();
        String status = params.get("status") == null ? "" : params.get("status").toString();
        String star = params.get("starttime") == null ? "" : params.get("starttime").toString();
        String end = params.get("endtime") == null ? "" : params.get("endtime").toString();
        String merchantdept = params.get("merchantdept") == null ? "" : params.get("merchantdept").toString();
        String tradeno = params.get("tradeno") == null ? "" : params.get("tradeno").toString();
        List<Long> idList = new ArrayList<>();
        if (!merchantdept.isEmpty()) {
            List<String> merchantDeptIdList = Arrays.asList(merchantdept.split(","));
            for (String merchantDeptId : merchantDeptIdList){
                idList.addAll(merchantDeptService.queryMerchantDeptIdList(Long.parseLong(merchantDeptId)));
            }
        }
        Page<TradeOrder> page = this.selectPage(new Query<TradeOrder>(params).getPage(),
                new EntityWrapper<TradeOrder>()
                        .eq(EmptyUtil.isNotEmpty(tradeId), "sys_trade_no", tradeId)
                        .eq(EmptyUtil.isNotEmpty(orderId), "order_id", orderId)
                        .eq(EmptyUtil.isNotEmpty(merchantId), "merchant_id", merchantId)
                        .eq(EmptyUtil.isNotEmpty(status), "status", status)
                        .eq(EmptyUtil.isNotEmpty(tradeno), "trade_no", tradeno)
                        .in(EmptyUtil.isNotEmpty(idList), "merchant_id", idList)
                        .ge(EmptyUtil.isNotEmpty(star), "create_time", star)
                        .le(EmptyUtil.isNotEmpty(end), "create_time", end));
        return new PageUtils(page);
    }
}

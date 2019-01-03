package com.ntnikka.modules.orderManager.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ntnikka.common.utils.EmptyUtil;
import com.ntnikka.common.utils.ExcelUtil;
import com.ntnikka.modules.orderManager.entity.TradeBarChartEntity;
import com.ntnikka.modules.orderManager.entity.TradeOrder;
import com.ntnikka.modules.orderManager.service.TradeOrderService;
import com.ntnikka.modules.pay.aliPay.utils.DateUtil;
import com.ntnikka.modules.sys.controller.AbstractController;
import com.ntnikka.utils.DateUtils;
import com.ntnikka.utils.PageUtils;
import com.ntnikka.utils.R;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName TradeOrderController
 * @Author Liuq
 * @Description todo
 * @Date 2018/9/17 14:51
 **/
@RestController
@RequestMapping(value = "/tradeOrder")
public class TradeOrderController extends AbstractController {

    private static final String[] title = {"序号", "商户ID", "商户名称", "支付方式", "商户订单号", "系统订单号", "银行订单号", "订单金额", "支付时间", "通知状态"};

    @Autowired
    TradeOrderService tradeOrderService;

    @RequestMapping(value = "list")
    public R queryOrderList(@RequestParam Map<String, Object> params) {
        System.out.println("params = [" + params + "]");
        PageUtils page = tradeOrderService.queryPage(params);
        return R.ok().put("page", page);
    }

    @RequestMapping(value = "listForMerchant")
    public R queryOrderListForMerchant(@RequestParam Map<String, Object> params){
        params.put("merchantdept",getUser().getMerchantDeptId());
        PageUtils page = tradeOrderService.queryPageForMerchant(params);
        return R.ok().put("page", page);
    }

    @RequestMapping(value = "listCheck")
    public R queryOrderListCheck(@RequestParam Map<String, Object> params) {
        System.out.println("params = [" + params + "]");
        String tradeId = params.get("tradeid") == null ? "" : params.get("tradeid").toString();
        if (EmptyUtil.isEmpty(tradeId)) {
            PageUtils pg = null;
            return R.ok().put("page", pg);
        }
        PageUtils page = tradeOrderService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 导出列表
     *
     * @return
     */
    @RequestMapping(value = "/export")
    public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {

        //excel文件名
        String fileName = "订单信息" + System.currentTimeMillis() + ".xls";
        //sheet名
        String sheetName = "订单信息";

        Map<String, Object> params = new HashMap();

        params.put("tradeid", request.getParameter("tradeid"));
        params.put("orderid", request.getParameter("orderid"));
        params.put("starttime", request.getParameter("starttime"));
        params.put("endtime", request.getParameter("endtime"));
        params.put("merchantid", request.getParameter("merchantid"));
        params.put("status", request.getParameter("status"));
        params.put("merchantdept", request.getParameter("merchantdept"));

        //创建HSSFWorkbook
        HSSFWorkbook orderWb = new HSSFWorkbook();

        List<TradeOrder> orderList = tradeOrderService.queryList(params);
        //根据部门id GroupBy
        Map<Long , List<TradeOrder>> resultListMap = orderList.stream().collect(Collectors.groupingBy(tradeOrder -> tradeOrder.getMerchantDeptId()));
        for (Map.Entry<Long, List<TradeOrder>> entry : resultListMap.entrySet()) {
            String[][] content = ExcelUtil.getContent(entry.getValue());
            String sheetNameTmp = entry.getValue().get(0).getMerchantDeptName();
            orderWb = ExcelUtil.getHSSFWorkbook(sheetNameTmp, title, content, orderWb);
        }
        //HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);
        //响应到客户端
        try {
            this.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            orderWb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //发送响应流方法
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setContentType("application/vnd.ms-excel;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @RequestMapping(value = "testCount")
    public R testCount(@RequestBody Map paramMap) {

        List<Map<String, String>> tradeBarChart = tradeOrderService.queryOrderDataForBarChart(paramMap);
        List<String> listDate = new ArrayList<>();
        List<Double> listAmount = new ArrayList<>();
        tradeBarChart.stream().forEach(stringMap -> {
            listDate.add(stringMap.get("dt"));
            listAmount.add(Double.parseDouble(String.valueOf(stringMap.get("total_count"))));
        });
        //饼图数据 查询 成功支付count 支付中count 失败count
        Map<String, String> totalCount = tradeOrderService.queryTotalOrderCount(paramMap);
        Map<String, String> toPayCount = tradeOrderService.queryToPayOrderCount(paramMap);
        Map<String, String> failCount = tradeOrderService.queryFailOrderCount(paramMap);
        //总订单
        Map<String, String> totalMap = tradeOrderService.queryAllCountAndSum(paramMap);
        Map<String, String> successMap = tradeOrderService.querySuccessCountAndSum(paramMap);
        return R.ok().put("nameList", listDate).put("amountList", listAmount)
                .put("totalMap", totalMap).put("successMap", successMap)
                .put("totalCount", totalCount)
                .put("toPayCount", toPayCount)
                .put("failCount", failCount);
    }

    @RequestMapping(value = "merchantCount")
    public R merchantCount(@RequestBody Map paramMap) {
        List<Map<String, String>> tradeBarChart = tradeOrderService.queryOrderDataForBarChartByMerchant(paramMap);
        List<String> listDate = new ArrayList<>();
        List<Double> listAmount = new ArrayList<>();
        tradeBarChart.stream().forEach(stringMap -> {
            listDate.add(stringMap.get("dt"));
            listAmount.add(Double.parseDouble(String.valueOf(stringMap.get("total_count"))));
        });
        //总订单
        Map<String, String> totalMap = tradeOrderService.queryAllCountAndSumByMerchant(paramMap);
        Map<String, String> successMap = tradeOrderService.querySuccessCountAndSumByMerchant(paramMap);
        //饼图数据 查询 成功支付count 支付中count 失败count
        Map<String, String> totalCount = tradeOrderService.queryTotalOrderCountByMerchant(paramMap);
        Map<String, String> toPayCount = tradeOrderService.queryToPayOrderCountByMerchant(paramMap);
        Map<String, String> failCount = tradeOrderService.queryFailOrderCountByMerchant(paramMap);
        return R.ok().put("nameList", listDate).put("amountList", listAmount)
                .put("totalMap", totalMap).put("successMap", successMap)
                .put("totalCount", totalCount)
                .put("toPayCount", toPayCount)
                .put("failCount", failCount);
    }

}

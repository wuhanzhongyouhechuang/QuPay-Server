package com.ntnikka.modules.merchantManager.controller;

import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ntnikka.common.utils.ExcelUtil;
import com.ntnikka.modules.merchantManager.entity.MerchantDept;
import com.ntnikka.modules.merchantManager.entity.MerchantEntity;
import com.ntnikka.modules.merchantManager.service.MerchantDeptService;
import com.ntnikka.modules.merchantManager.service.MerchantService;
import com.ntnikka.modules.orderManager.entity.TradeOrder;
import com.ntnikka.modules.pay.aliPay.utils.MD5Utils;
import com.ntnikka.utils.PageUtils;
import com.ntnikka.utils.R;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author Liuq
 * @email
 * @date 2018-09-18 16:41:11
 */
@RestController
@RequestMapping("/merchant/mgr")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private MerchantDeptService merchantDeptService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = merchantService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        MerchantEntity merchant = merchantService.selectById(id);

        return R.ok().put("merchant", merchant);
    }

    @RequestMapping(value = "/tradestatus", method = RequestMethod.POST)
    public R updateTradeStatus(@RequestBody Map params) {
        int tradeStatus = 0; //默认开启
        if (Integer.parseInt(params.get("tradeStatus").toString()) == 0) {//关闭
            tradeStatus = 1;
        }
        Long id = Long.parseLong(params.get("merchantId").toString());
        Map<String, Object> paramMap = new HashMap();
        paramMap.put("merchantId", id);
        paramMap.put("tradeStatus", tradeStatus);
        merchantService.updateTradeStatus(paramMap);
        return R.ok();
    }

    @RequestMapping(value = "/settlestatus", method = RequestMethod.POST)
    public R updateSettleStatus(@RequestBody Map params) {
        int settleStatus = 0; //默认开启
        if (Integer.parseInt(params.get("settleStatus").toString()) == 0) {//关闭
            settleStatus = 1;
        }
        Long id = Long.parseLong(params.get("merchantId").toString());
        Map<String, Object> paramMap = new HashMap();
        paramMap.put("merchantId", id);
        paramMap.put("settleStatus", settleStatus);
        merchantService.updateSettleStatus(paramMap);
        return R.ok();
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @Transactional
    public R save(@RequestBody MerchantEntity merchant) {
        merchant.setMerchantKey(MD5Utils.creatMerchantKey(merchant));
        merchantService.save(merchant);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MerchantEntity merchant) {
        merchantService.updateById(merchant);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        merchantService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

    @RequestMapping("/hasOrder")
    public R queryMerchantHasOrder(){
        List<MerchantEntity> merchantEntityList = merchantService.hasOrder();
        PageUtils page = new PageUtils(merchantEntityList,merchantEntityList.size(),merchantEntityList.size(),1);
        return R.ok().put("page",page);
    }

    @RequestMapping("/export")
    public void exportYesterdayByDeptId(HttpServletRequest request, HttpServletResponse response) throws Exception{
        //excel文件名deptName
        String tmpName = URLDecoder.decode(request.getParameter("deptName") , "utf-8");
        String fileName = tmpName + "订单信息" + System.currentTimeMillis() + ".xls";
        //sheet名
        String sheetName = "订单信息";

        Map<String, Object> params = new HashMap();
        params.put("deptId" ,request.getParameter("merchantDeptId"));
        List<TradeOrder> tradeOrderList = merchantService.queryYesterdayOrderList(params);

        String[][] content = ExcelUtil.getContent(tradeOrderList);

        //创建HSSFWorkbook
        HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName,ExcelUtil.title , content, null);

        //响应到客户端
        try {
            ExcelUtil.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

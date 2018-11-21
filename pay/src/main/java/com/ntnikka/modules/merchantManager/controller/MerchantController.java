package com.ntnikka.modules.merchantManager.controller;

import com.ntnikka.modules.merchantManager.entity.MerchantEntity;
import com.ntnikka.modules.merchantManager.service.MerchantDeptService;
import com.ntnikka.modules.merchantManager.service.MerchantService;
import com.ntnikka.modules.pay.aliPay.utils.MD5Utils;
import com.ntnikka.utils.PageUtils;
import com.ntnikka.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


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

}

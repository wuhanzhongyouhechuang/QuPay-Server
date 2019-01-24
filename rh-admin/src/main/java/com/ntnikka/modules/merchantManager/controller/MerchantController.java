package com.ntnikka.modules.merchantManager.controller;

import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.*;

import com.ntnikka.common.utils.EmptyUtil;
import com.ntnikka.common.utils.ExcelUtil;
import com.ntnikka.modules.merchantManager.entity.ChannelEntity;
import com.ntnikka.modules.merchantManager.entity.MerchantDept;
import com.ntnikka.modules.merchantManager.entity.MerchantEntity;
import com.ntnikka.modules.merchantManager.entity.MerchantSettleChannel;
import com.ntnikka.modules.merchantManager.service.MerchantDeptService;
import com.ntnikka.modules.merchantManager.service.MerchantService;
import com.ntnikka.modules.merchantManager.service.MerchantSettleService;
import com.ntnikka.modules.orderManager.entity.TradeOrder;
import com.ntnikka.modules.pay.aliPay.utils.MD5Utils;
import com.ntnikka.modules.sys.controller.AbstractController;
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
public class MerchantController extends AbstractController {

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private MerchantDeptService merchantDeptService;

    @Autowired
    private MerchantSettleService merchantSettleService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = merchantService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 个人码商户列表
     */
    @RequestMapping("/listPri")
    public R listPri(@RequestParam Map<String, Object> params) {
        PageUtils page = merchantService.queryPageForPriMerchant(params);
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

    /**
     * channel信息
     */
    @RequestMapping("/channelInfo/{id}")
    public R channelInfo(@PathVariable("id") Long id) {
        List<ChannelEntity> channelEntityList = merchantService.queryChannelList(id);
        return R.ok().put("specList", channelEntityList);
    }

    /**
     * channel信息
     */
    @RequestMapping("/channelInfo/list")
    public R queryChannelInfo(@RequestParam Map<String, Object> params) {
        List<ChannelEntity> channelEntityList = merchantService.queryChannelList(Long.parseLong(params.get("merchantId").toString()));
        PageUtils pageUtils = new PageUtils(channelEntityList , channelEntityList.size() , channelEntityList.size() ,1);
        return R.ok().put("page", pageUtils);
    }

    @RequestMapping("/settleInfo/list")
    public R querySettleChannelInfo(@RequestParam Map<String, Object> params){
        List<MerchantSettleChannel> merchantSettleChannelList = merchantSettleService.querySettleList(Long.parseLong(params.get("merchantId").toString()));
        PageUtils pageUtils = new PageUtils(merchantSettleChannelList , merchantSettleChannelList.size() , merchantSettleChannelList.size() , 1);
        return R.ok().put("page", pageUtils);
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
     * 修改(个人码商户)
     */
    @RequestMapping("/updatePri")
    public R updatePri(@RequestBody MerchantEntity merchant) {
        merchantService.updatePri(merchant);
        return R.ok();
    }

    /**
     * 修改通道list
     */
    @RequestMapping("/updatePriChannel")
    public R updatePriChannel(@RequestBody MerchantEntity merchant) {
        merchantService.updatePriChannel(merchant);
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

    @RequestMapping("/updateChannelFlag")
    public R closeChannel(@RequestBody Map params){
        int flag = 0; //默认开启
        if (Integer.parseInt(params.get("flag").toString()) == 0) {//关闭
            flag = 1;
        }
        Long id = Long.parseLong(params.get("id").toString());
        Map paramMap = new HashMap();
        paramMap.put("id" , id);
        paramMap.put("flag" , flag);
        merchantService.updateChannelFlag(paramMap);
        return R.ok();
    }

    @RequestMapping("/updateSettleChannelFlag")
    public R updateSettleChannel(@RequestBody Map params){
        int flag = 0; //默认开启
        if (Integer.parseInt(params.get("flag").toString()) == 0) {//关闭
            flag = 1;
        }
        Long id = Long.parseLong(params.get("id").toString());
        Map paramMap = new HashMap();
        paramMap.put("id" , id);
        paramMap.put("flag" , flag);
        merchantSettleService.updateSettleChannel(paramMap);
        return R.ok();
    }

    @RequestMapping("/deleteChannel")
    public R deleteChannel(@RequestBody Map params){
        Long id = Long.parseLong(params.get("id").toString());
        Map paramMap = new HashMap();
        paramMap.put("id" , id);
        merchantService.delChannel(paramMap);
        return R.ok();
    }

    @RequestMapping("/updatePolling")
    public R updatePolling(@RequestBody Map params){
        int flag = 0; //默认关闭
        if (Integer.parseInt(params.get("flag").toString()) == 0) {//关闭
            flag = 1;
        }
        Long id = Long.parseLong(params.get("id").toString());
        Map paramMap = new HashMap();
        paramMap.put("id" , id);
        paramMap.put("flag" , flag);
        merchantService.updatePolling(paramMap);
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

    /**
     * 个人码商户
     * @return
     */
    @RequestMapping("/selectMerchant")
    public R selectParent() {
        String deptIdList = getUser().getMerchantDeptId() == null ? "" : getUser().getMerchantDeptId();
        if (EmptyUtil.isEmpty(deptIdList)){
            return R.ok().put("merchantList" ,new ArrayList<MerchantEntity>());
        }
        List<String> idList = Arrays.asList(deptIdList.split(","));
        List<MerchantEntity> merchantEntityList = merchantService.queryMerchantList(idList);
        return R.ok().put("merchantList", merchantEntityList);
    }

    /**
     * 普通商户
     * @return
     */
    @RequestMapping("/selectMerchantNormal")
    public R selectParentNormal() {
        String deptIdList = getUser().getMerchantDeptId() == null ? "" : getUser().getMerchantDeptId();
        if (EmptyUtil.isEmpty(deptIdList)){
            return R.ok().put("merchantList" ,new ArrayList<MerchantEntity>());
        }
        List<String> idList = Arrays.asList(deptIdList.split(","));
        List<MerchantEntity> merchantEntityList = merchantService.queryMerchantListNormal(idList);
        return R.ok().put("merchantList", merchantEntityList);
    }

    @RequestMapping("saveOrUpdateSettleChannel")
    public R saveOrUpdateSettleChannel(@RequestBody List<MerchantSettleChannel> merchantSettleChannelList){
        merchantSettleService.saveOrUpdateSettleChannel(merchantSettleChannelList);
        return R.ok();
    }

    @RequestMapping("settleInfo/{merchantId}")
    public R settleInfo(@PathVariable("merchantId") Long merchantId){
        List<MerchantSettleChannel> merchantSettleChannelList = merchantSettleService.querySettleList(merchantId);
        return R.ok().put("list",merchantSettleChannelList);
    }

    @RequestMapping("/queryMerchantListForDept")
    public R queryMerchantListForDept(@RequestParam Map<String, Object> params){
        params.put("deptId" , getUser().getMerchantDeptId());
        PageUtils page = merchantService.queryPageAll(params);
        return R.ok().put("page", page);
    }
}

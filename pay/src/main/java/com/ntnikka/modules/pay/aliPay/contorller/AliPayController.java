package com.ntnikka.modules.pay.aliPay.contorller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.ntnikka.modules.common.Enum.AlipayTradeStatus;
import com.ntnikka.modules.common.Enum.PayTypeEnum;
import com.ntnikka.modules.common.utils.*;
import com.ntnikka.modules.merchantManager.entity.ChannelEntity;
import com.ntnikka.modules.merchantManager.entity.MerchantEntity;
import com.ntnikka.modules.merchantManager.entity.MerchantSettleChannel;
import com.ntnikka.modules.merchantManager.service.ChannelService;
import com.ntnikka.modules.merchantManager.service.MerchantService;
import com.ntnikka.modules.merchantManager.service.MerchantSettleService;
import com.ntnikka.modules.pay.aliPay.config.AlipayConfig;
import com.ntnikka.modules.pay.aliPay.entity.AliOrderEntity;
import com.ntnikka.modules.pay.aliPay.entity.TradeQueryParam;
import com.ntnikka.modules.pay.aliPay.service.AliOrderService;
import com.ntnikka.modules.pay.aliPay.utils.*;
import com.ntnikka.modules.sys.controller.AbstractController;
import com.ntnikka.utils.R;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.concurrent.CompletableFuture.runAsync;

/**
 * Created by liuq on 2018/9/11.
 */

@RestController
@RequestMapping("/api/v1")
public class AliPayController extends AbstractController {

    private static Logger logger = LoggerFactory.getLogger(AliPayController.class);

    @Autowired
    private AliOrderService aliOrderService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private MerchantSettleService merchantSettleService;

    private static final String Ali_Request_Url = "https://ds.alipay.com/?from=mobilecodec&scheme=";

    private static final String Ali_Prefix = "alipays://platformapi/startapp?appId=20000067&url=";


    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public R testController(@RequestBody AliOrderEntity aliOrderEntity, HttpServletRequest request) throws Exception {

        logger.info("进入支付宝下单-------------------------------------------");
        //1.校验必填参数
        if (!AliOrderEntity.checkParam(aliOrderEntity)) {
            return R.error(403000, "缺少参数");
        }
        MerchantEntity merchant = merchantService.queryById(aliOrderEntity.getMerchantId());
        if (merchant == null) {
            return R.error(403001, "不存在的商户");
        }
        aliOrderEntity.setPartner(merchant.getMerchantKey());
        //2.校验签名
        String sign = aliOrderEntity.getSign();
        String ParamStr = String.format("merchantId=%s&orderAmount=%s&orderId=%s&payMethod=%s&payType=%s&signType=%s&version=%s&priKey=%s", aliOrderEntity.getMerchantId(), aliOrderEntity.getOrderAmount(),
                aliOrderEntity.getOrderId(), aliOrderEntity.getPayMethod(), aliOrderEntity.getPayType(), aliOrderEntity.getSignType(),
                aliOrderEntity.getVersion(), merchant.getMerchantKey());
        if (!SignUtil.checkSign(sign, ParamStr)) {
            logger.error("验签失败, sign = {} , paramstr = {}", sign, ParamStr);
            return R.error(403013, "验签失败");
        }
        if (merchant.getTradeStatus() == 1) {
            return R.error(403016, "该商户已关闭交易权限，请联系客服人员");
        }
        int count = aliOrderService.checkRepeatId(aliOrderEntity.getOrderId());
        if (count > 0) {
            logger.error("订单流水号重复 , orderId = {}", aliOrderEntity.getOrderId());
            return R.error(403014, "订单流水号重复");
        }
        //如果为个人码支付，先判断是否配置了相对应绑定手机外网地址
        if (merchant.getPriFlag() == 0) {
            if (aliOrderEntity.getPayMethod().equals("221") || aliOrderEntity.getPayMethod().equals("321") || aliOrderEntity.getPayMethod().equals("421")) {
                String mobileUrl = merchant.getMobileUrl();
                if (StringUtils.isEmpty(mobileUrl)) {//如果为空 返回先配置url
                    logger.error("商户个人码绑定手机地址未配置 ， merchantId = {} ", merchant.getId());
                    return R.error(405000, "下单失败，个人码相关配置缺漏，请联系客服人员");
                }
            }
        }else {//个人码商户
            List<ChannelEntity> channelEntityList = channelService.queryUseableChannelByMerchantId(merchant.getId());
            if (EmptyUtil.isEmpty(channelEntityList)){
                logger.error("个人码商户无可用通道 ， merchantId = {} ", merchant.getId());
                return R.error(405000, "下单失败，无可用通道 ，请配置");
            }
        }
        //微信下单，先校验是否配置了第三方商户id和密钥
        if (aliOrderEntity.getPayMethod().equals("32")) {
            String wechatNum = merchant.getWechatNum();
            String wechatKey = merchant.getWechatKey();
            if (StringUtils.isEmpty(wechatNum) || StringUtils.isEmpty(wechatKey)) {
                logger.error("商户微信配置尚未配置 ， merchantId = {}", merchant.getId());
                return R.error(405000, "下单失败，微信相关配置缺漏，请联系客服人员");
            }
        }
        //3.订单信息入库
        aliOrderEntity.setSysTradeNo(aliOrderEntity.getPayMethod().equals("22") ? IdWorker.getSysTradeNum() : IdWorker.getSysTradeNumShort());//生成系统唯一订单号
        aliOrderEntity.setMerchantId(merchant.getId());
        aliOrderEntity.setMerchantName(merchant.getMerchantName());
        aliOrderEntity.setCreateTime(new Date());
        aliOrderEntity.setUpdateTime(new Date());
        aliOrderEntity.setMerchantDeptId(merchant.getMerchantDeptId());
        aliOrderEntity.setMerchantDeptName(merchant.getMerchantDeptName());
        aliOrderService.save(aliOrderEntity);
        //4.判断payMethod 22-支付宝 221-支付包免签 32-微信支付(第三方) 321-微信面前 421-QQ免签
        if (aliOrderEntity.getPayMethod() == "22" || aliOrderEntity.getPayMethod().equals("22")) {
            try {
                String result = AliPayRequest.doQrCodeAliRequest(aliOrderEntity.getSysTradeNo(), aliOrderEntity.getOrderAmount(), aliOrderEntity.getProductName(),
                        merchant.getAppid().toString(), merchant.getMerchantPriKey(), merchant.getAliPubKey(), merchant.getAuthCode(), merchant.getPid().toString(), merchant.getStoreNumber());
                JSONObject resultJson = JSON.parseObject(result).getJSONObject("alipay_trade_precreate_response");
                if (resultJson.getInteger("code") != 10000) {
                    aliOrderService.updateTradeStatusClosed(aliOrderEntity.getSysTradeNo());
                    return R.error(403017, "下单失败").put("sub_code", resultJson.getString("sub_code")).put("sub_msg", resultJson.getString("sub_msg"));
                }
                //4.调起支付宝下单接口 根据不同的payType处理不同下单方式
                //wap支付
                //直接返回支付宝返回的二维码url
                Map resultMap = new HashMap();
                if (PayTypeEnum.WAP.getMessage().equals(aliOrderEntity.getPayType()) || PayTypeEnum.WAP.getMessage() == aliOrderEntity.getPayType()) {//Wap
                    //预下单成功 返回预下单信息
                    resultMap.put("out_trade_no", resultJson.getString("out_trade_no"));
                    resultMap.put("qr_code", resultJson.getString("qr_code"));
                    return R.ok().put("data", resultMap);
                } else {
                    //二维码支付
                    //返回处理过的图片base64码 前端页面直接用img标签的src接受
                    String imgStr = ImageToBase64Util.createQRCode(resultJson.getString("qr_code"));
                    resultMap.put("out_trade_no", resultJson.getString("out_trade_no"));
                    resultMap.put("qr_code", imgStr);
                    return R.ok().put("data", resultMap);
                }
            } catch (Exception e) {
                aliOrderService.updateTradeStatusClosed(aliOrderEntity.getSysTradeNo());
                e.printStackTrace();
                return R.error(405000, "下单失败");
            }
        } else if (aliOrderEntity.getPayMethod().equals("32") || aliOrderEntity.getPayMethod() == "32") {//微信
            logger.info("==================进入wechant下单===================");
            String payType = "";
            if (aliOrderEntity.getPayType().equals(PayTypeEnum.WAP.getMessage())) {//Wap
                payType = "MWEB";
            } else {
                payType = "NATIVE";
            }
            String amount = BigDecimalUtil.changeY2F(aliOrderEntity.getOrderAmount().toString());
            String msg = WechatRequest.doWechatOrderCreate(merchant.getWechatNum(), NetworkUtil.getIpAddress(request), "MD5", "http://47.92.241.14/api/v1/wechatNotify",
                    aliOrderEntity.getSysTradeNo(), amount, payType, merchant.getWechatKey(), "123");
            JSONObject resultJson = JSON.parseObject(msg);
            String returnCode = resultJson.getString("return_code");
            if (!returnCode.equals("SUCCESS")) {
                logger.info("wechat下单失败 :  {}", msg);
                return R.error(405000, "下单失败");
            }
            //下单成功
            Map wechatMap = new HashMap();
            if (PayTypeEnum.WAP.getMessage().equals(aliOrderEntity.getPayType()) || PayTypeEnum.WAP.getMessage() == aliOrderEntity.getPayType()) {
                //wap
                wechatMap.put("out_trade_no", resultJson.getString("out_trade_no"));
                wechatMap.put("qr_code", resultJson.getString("mweb_url"));
                return R.ok().put("data", wechatMap);
            } else {
                //qrcode
                String imgStr = ImageToBase64Util.createQRCode(resultJson.getString("code_url"));
                wechatMap.put("out_trade_no", resultJson.getString("out_trade_no"));
                wechatMap.put("qr_code", imgStr);
                return R.ok().put("data", wechatMap);
            }
        } else {
            String payType = "";
            switch (aliOrderEntity.getPayMethod()) {
                case "221":
                    //支付宝免签逻辑
                    payType = "alipay";
                    logger.info("支付宝个人码下单");
                    break;
                case "321":
                    //微信免签逻辑
                    payType = "wechat";
                    logger.info("微信个人码下单");
                    break;
                case "421":
                    //QQ免签逻辑
                    payType = "qq";
                    logger.info("qq个人码下单");
                    break;
                default:
                    return R.error(407000, "请输入正确的payMethod值");
            }
            //1.获取免签手机外网地址
            //判断商户是普通或者个人码商户
            String mobileUrl = "";
            Long channelId = 0L;
            String aliUserId = "";
            if (merchant.getPriFlag() == 0){
                mobileUrl = merchant.getMobileUrl();
            }else {
                List<ChannelEntity> channelEntityList = channelService.queryUseableChannelByMerchantId(merchant.getId());
                if (merchant.getPollingFlag() == 1){//开启轮询
                    int index = PollingUtil.RandomIndex(channelEntityList.size());
                    mobileUrl = channelEntityList.get(index).getUrl();
                    channelId = channelEntityList.get(index).getId();
                    aliUserId = channelEntityList.get(index).getAliUserId();
                }else {//轮询关闭
                    mobileUrl = channelEntityList.get(0).getUrl();
                    channelId = channelEntityList.get(0).getId();
                    aliUserId = channelEntityList.get(0).getAliUserId();
                }
            }
            if (payType.equals("alipay")){//支付宝个人码(修改转账模式 轮询userid)
                String requestUrl = "alipayqr://platformapi/startapp?saId=10000007&qrcode=http://vcapay.com.cn/api/v1/trade?sysTradeNo="+aliOrderEntity.getSysTradeNo();
                String aliUrl = Ali_Request_Url + URLEncoder.encode(requestUrl,"utf-8");
                logger.info("支付宝个人码地址: {}" , requestUrl);
                Map resultMap = new HashMap();
                if (PayTypeEnum.WAP.getMessage().equals(aliOrderEntity.getPayType()) || PayTypeEnum.WAP.getMessage() == aliOrderEntity.getPayType()) {
                    //wap
                    resultMap.put("out_trade_no", aliOrderEntity.getSysTradeNo());
                    resultMap.put("qr_code", aliUrl);
                    return R.ok().put("data", resultMap);
                } else {
                    //qrcode
                    String imgStr = ImageToBase64Util.createQRCode(aliUrl);
                    resultMap.put("out_trade_no", aliOrderEntity.getSysTradeNo());
                    resultMap.put("qr_code", imgStr);
                    return R.ok().put("data", resultMap);
                }
            }else {//微信个人码
                String result = MobileRequest.createOrderMobile(mobileUrl, aliOrderEntity.getOrderAmount(), aliOrderEntity.getSysTradeNo(), payType);
                if (EmptyUtil.isEmpty(result)){
                    logger.info("个人码通道无返回 ，下单失败");
                    if (merchant.getPriFlag() == 1){
                        logger.info("个人码手机通道无返回  , 暂时关闭通道 ，请检查通道 : {} , 通道ID : {}" , merchant.getId(),channelId);
                        merchantService.closeChannel(channelId);
                    }
                    return R.error(405000, "个人码通道无返回 ，请检查相关配置");
                }
                JSONObject resultJson = JSON.parseObject(result);
                Map resultMap = new HashMap();
                if (resultJson.getString("msg").contains("获取成功")) {
                    if (PayTypeEnum.WAP.getMessage().equals(aliOrderEntity.getPayType()) || PayTypeEnum.WAP.getMessage() == aliOrderEntity.getPayType()) {
                        //wap
                        resultMap.put("out_trade_no", resultJson.getString("mark"));
                        resultMap.put("qr_code", resultJson.getString("payurl"));
                        return R.ok().put("data", resultMap);
                    } else {
                        //qrcode
                        String imgStr = ImageToBase64Util.createQRCode(resultJson.getString("payurl"));
                        resultMap.put("out_trade_no", resultJson.getString("mark"));
                        resultMap.put("qr_code", imgStr);
                        return R.ok().put("data", resultMap);
                    }
                } else {
                    logger.error("获取个人码失败 ， msg : {}", resultJson.getString("msg"));
                    return R.error(406000, "获取个人码失败");
                }
            }
        }
        //二维码支付
    }

    @RequestMapping(value = "/QrCodeTest", method = RequestMethod.POST)
    public R QcCodeTestController() {
        try {
            Map resultMap = new HashMap();
            MerchantEntity merchantEntity = merchantService.findByPriKey("6816CCBB9923D7B006A02C877B2D9F68");
            resultMap.put("data", merchantEntity);
            return R.ok(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.error(90000, "未知异常，请联系管理员");
    }

    @RequestMapping(value = "/AliNotify", method = RequestMethod.POST)
    public String NotifyController(HttpServletRequest request) {
        //用户支付后 接受支付宝回调 处理业务逻辑 通知下游商户
        Map<String, String> params = AliUtils.convertRequestParamsToMap(request); // 将异步通知中收到的待验证所有参数都存放到map中
        String paramsJson = JSON.toJSONString(params);
        logger.info("支付宝回调，{}", paramsJson);
        logger.info("支付宝回调参数，{}", request.getParameterMap());
        // TODO: 2018/9/26 alipay_public_key 根据回调参数查询 非写死
        String tradeId = params.get("out_trade_no");//此处返回的为本系统的sysTradeNo
        AliOrderEntity aliOrderEntity = aliOrderService.queryBySysTradeNo(tradeId);
        if (aliOrderEntity == null) {//订单不存在
            return "failure";
        }
        MerchantEntity merchantEntity = merchantService.findByPriKey(aliOrderEntity.getPartner());
        if (merchantEntity == null) {//商户不存在
            return "failure";
        }
        try {
            boolean signVerified = AlipaySignature.rsaCheckV1(params, merchantEntity.getAliPubKey(),
                    AlipayConfig.input_charset_utf8, AlipayConfig.sign_type_RSA2);
            if (signVerified) {
                logger.info("支付宝回调签名认证成功");
                this.check(params);//验证业务参数
                //验证有效
                //异步处理业务数据(修改同步执行)
                String tradeStatus = params.get("trade_status");
                if (tradeStatus.equals(AlipayTradeStatus.TRADE_SUCCESS.getStatus()) || tradeStatus.equals(AlipayTradeStatus.TRADE_FINISHED.getStatus())) {//支付完成 支付成功 处理订单状态 通知商户
                    Map<String, Object> map = new HashMap<>();
                    map.put("orderId", aliOrderEntity.getSysTradeNo());
                    map.put("tradeNo", params.get("trade_no"));
                    logger.info("支付成功，支付时间 : {}", params.get("gmt_payment"));
                    map.put("payTime", DateUtil.string2Date(params.get("gmt_payment")));
                    aliOrderService.updateTradeOrder(map);
                    //分账
                    if (merchantEntity.getSettleFlag() == 0) { //开启分账
                        //是否有可用分账通道
                        List<MerchantSettleChannel> merchantSettleChannelList = merchantSettleService.queryUseableSettleList(merchantEntity.getId());
                        if (EmptyUtil.isNotEmpty(merchantSettleChannelList)) {
                            int index = PollingUtil.RandomIndex(merchantSettleChannelList.size());//随机轮询可用通道
                            Double amountPercent = merchantSettleChannelList.get(index).getAmountPercent();
                            String transInAliUserId = merchantSettleChannelList.get(index).getAliUserId();
                            String result = AliPayRequest.doSettleAliRequest(merchantEntity.getAppid().toString(), merchantEntity.getMerchantPriKey(), merchantEntity.getAliPubKey(), params.get("trade_no"), aliOrderEntity.getOrderAmount(), merchantEntity.getSettleIdOut(), transInAliUserId, merchantEntity.getAuthCode() ,amountPercent);
                            JSONObject resultJson = JSON.parseObject(result).getJSONObject("alipay_trade_order_settle_response");
                            if (resultJson.getInteger("code") != 10000) {
                                logger.info("分账请求失败 ， 请求返回 : 错误码 , {} , 信息 , {}", resultJson.getString("sub_code"), resultJson.getString("sub_msg"));
                                if (resultJson.getInteger("code") == 40004) {
                                    logger.info("分账账户异常 , 暂时关闭此分账通道 ，通道ID : {} ，通道aliUserId : {}" ,merchantSettleChannelList.get(index).getId(),merchantSettleChannelList.get(index).getAliUserId());
                                }
                                merchantSettleService.closeSettleChannel(merchantSettleChannelList.get(index).getId());
                            } else {
                                logger.info("分账请求成功");
                            }
                        }else {
                            logger.info("商户 {} , 无可用分账通道" , merchantEntity.getId());
                        }
                    }
                    //通知下游商户
                    String returnMsg = this.doNotify(aliOrderEntity.getNotifyUrl(), aliOrderEntity.getOrderId().toString(), AlipayTradeStatus.TRADE_SUCCESS.getStatus(), aliOrderEntity.getOrderAmount().toString(), aliOrderEntity.getPartner());
                    if (returnMsg.equals("success") || returnMsg.contains("success") || returnMsg.contains("SUCCESS")) {
                        logger.info("通知商户成功，修改通知状态");
                        aliOrderService.updateNotifyStatus(aliOrderEntity.getSysTradeNo());
                    } else {
                        logger.error("通知商户失败 , 商户返回 : {}" , returnMsg);
                    }
                } else if (tradeStatus.equals(AlipayTradeStatus.TRADE_CLOSED.getStatus())) {
                    aliOrderService.updateTradeStatusClosed(aliOrderEntity.getSysTradeNo());
                } else {
                    logger.error("没有处理支付宝回调业务，支付宝交易状态: {} ", tradeStatus);
                }
                return "success";
            } else {
                logger.error("支付宝回调签名认证失败，signVerified=false, paramsJson:{}", paramsJson);
                return "failure";
            }
        } catch (AlipayApiException e) {
            logger.error("支付宝回调签名认证失败,paramsJson:{},errorMsg:{}", paramsJson, e.getMessage());
            return "failure";
        }
    }

    /**
     * 提供商户的查询
     *
     * @param tradeQueryParam
     * @return
     * @throws AlipayApiException
     */
    @RequestMapping(value = "/queryOrder")
    public R queryOrderStatus(@RequestBody TradeQueryParam tradeQueryParam) throws AlipayApiException {

        logger.info("进入queryOrder ------------------------------------------------------------------");
        //1.校验签名
        MerchantEntity merchantEntity = merchantService.queryById(tradeQueryParam.getMerchantId());
        if (merchantEntity == null) {
            return R.error(403015, "商户不存在");
        }
        if (merchantEntity.getTradeStatus() == 1) {
            return R.error(403016, "该商户已关闭交易权限，请联系客服人员");
        }
        String sign = tradeQueryParam.getSign();
        AliOrderEntity aliOrderEntity = aliOrderService.queryTradeId(tradeQueryParam.getOrderId());
        if (aliOrderEntity == null) {
            return R.error(403017, "订单不存在");
        }
        String checkSignStr = String.format("merchantId=%s&orderId=%s&priKey=%s", tradeQueryParam.getMerchantId(), tradeQueryParam.getOrderId(), merchantEntity.getMerchantKey());
        if (!SignUtil.checkSign(sign, checkSignStr)) {
            logger.error("验签失败, sign = {} , paramstr = {}", sign, checkSignStr);
            return R.error(403013, "验签失败");
        }
        logger.info("验签通过--------------------------");
        //2.校验商户权限
        //3.调用支付宝查询接口
        if (aliOrderEntity.getPayMethod().equals("22") || aliOrderEntity.getPayMethod() == "22") {
            String returnStr = AliPayRequest.queryOrder(merchantEntity.getAppid().toString(), merchantEntity.getMerchantPriKey(), merchantEntity.getAliPubKey(), aliOrderEntity.getSysTradeNo(), merchantEntity.getAuthCode());
            logger.info("支付宝返回msg : {}", returnStr);
            JSONObject resultJson = JSON.parseObject(returnStr).getJSONObject("alipay_trade_query_response");
            Integer code = resultJson.getInteger("code");
            if (code != 10000) {
                if (code == 40004) {
                    return R.error(40004, "用户支付中..");
                }
                return R.error(40000, resultJson.getString("msg"));
            }
            //交易状态成功 修改
            String trade_status = resultJson.getString("trade_status");
            String out_trade_no = resultJson.getString("out_trade_no");
            if (trade_status.equals(AlipayTradeStatus.TRADE_SUCCESS.getStatus()) || trade_status.equals(AlipayTradeStatus.TRADE_FINISHED.getStatus())) {
                Map<String, Object> map = new HashMap<>();
                map.put("orderId", tradeQueryParam.getOrderId());
                map.put("tradeNo", resultJson.get("trade_no"));
                aliOrderService.updateTradeOrder(map);
            }
            Map<String, Object> map = new HashMap<>();
            map.put("trade_status", trade_status);
            map.put("orderId", out_trade_no);
            return R.ok().put("data", map);
        } else if (aliOrderEntity.getPayMethod().equals("32")) {//微信
            String retMsg = WechatRequest.doWechatOrderQuery(merchantEntity.getWechatNum(), aliOrderEntity.getSysTradeNo(), "MD5", merchantEntity.getWechatKey());
            JSONObject resultJson = JSON.parseObject(retMsg);
            Map retMap = new HashMap();
            if (resultJson.getString("result_code").equals("SUCCESS")) {//第三方已下单
                if (resultJson.getString("trade_state").equals("SUCCESS")) {
                    //已支付
                    Map<String, Object> paramMap = new HashMap<>();
                    paramMap.put("orderId", aliOrderEntity.getSysTradeNo());
                    paramMap.put("tradeNo", resultJson.get("transaction_id"));
                    aliOrderService.updateTradeOrder(paramMap);
                    retMap.put("trade_status", "TRADE_SUCCESS");
                    retMap.put("orderId", aliOrderEntity.getSysTradeNo());
                    return R.ok().put("data", retMap);
                } else {
                    //未支付
                    retMap.put("trade_status", "NOT_PAY");
                    retMap.put("orderId", aliOrderEntity.getSysTradeNo());
                    return R.ok().put("data", retMap);
                }
            } else {//第三方下单失败 订单不存在
                retMap.put("trade_status", "NOT_EXIST");
                retMap.put("orderId", aliOrderEntity.getSysTradeNo());
                return R.ok().put("data", retMap);
            }
        } else {//个人码
            String retMsg = MobileRequest.queryOrder(aliOrderEntity.getSysTradeNo(), merchantEntity.getMobileUrl());
            logger.info("Mobile返回msg : {}", retMsg);
            Map retMap = new HashMap();
            if (retMsg.contains("未支付")) {
                retMap.put("trade_status", "NOT_PAY");
                retMap.put("orderId", aliOrderEntity.getSysTradeNo());
                return R.ok().put("data", retMap);
            }
            if (retMsg.contains("支付成功")) {
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("orderId", aliOrderEntity.getSysTradeNo());
                aliOrderService.updateTradeOrder(paramMap);
                retMap.put("trade_status", "TRADE_SUCCESS");
                retMap.put("orderId", aliOrderEntity.getSysTradeNo());
                return R.ok().put("data", retMap);
            } else {
                return R.error("查询失败");
            }
        }
    }

    @RequestMapping(value = "/queryOrderByAdmin")
    public R queryOrderStatus(@RequestBody Map map) throws AlipayApiException {

        logger.info("进入queryOrder By Admin ------------------------------------------------------------------");
        Long id = Long.parseLong(map.get("id").toString());
        AliOrderEntity aliOrderEntity = aliOrderService.queryById(id);
        if (aliOrderEntity == null) {
            return R.error(50000, "订单不存在");
        }
        MerchantEntity merchantEntity = merchantService.findByPriKey(aliOrderEntity.getPartner());
        if (merchantEntity == null) {
            return R.error(50001, "商户不存在");
        }
        if (aliOrderEntity.getPayMethod().equals("22") || aliOrderEntity.getPayMethod() == "22") {
            //正常支付宝下单
            String returnStr = AliPayRequest.queryOrder(merchantEntity.getAppid().toString(), merchantEntity.getMerchantPriKey(), merchantEntity.getAliPubKey(), aliOrderEntity.getSysTradeNo(), merchantEntity.getAuthCode());
            logger.info("支付宝返回msg : {}", returnStr);
            JSONObject resultJson = JSON.parseObject(returnStr).getJSONObject("alipay_trade_query_response");
            Integer code = resultJson.getInteger("code");
            if (code != 10000) {
                if (code == 40004) {
                    return R.error(40004, "用户支付中..");
                }
                return R.error(40000, resultJson.getString("msg"));
            }
            String trade_status = resultJson.getString("trade_status");
            if (trade_status.equals(AlipayTradeStatus.TRADE_SUCCESS.getStatus()) || trade_status.equals(AlipayTradeStatus.TRADE_FINISHED.getStatus())) {
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("orderId", aliOrderEntity.getSysTradeNo());
                paramMap.put("tradeNo", resultJson.get("trade_no"));
                aliOrderService.updateTradeOrder(paramMap);
                //客服人员主动查询 若已支付成功则主动发送通知
                String returnMsg = this.doNotify(aliOrderEntity.getNotifyUrl(), aliOrderEntity.getOrderId().toString(), AlipayTradeStatus.TRADE_SUCCESS.getStatus(), aliOrderEntity.getOrderAmount().toString(), aliOrderEntity.getPartner());
                return this.checkNotify(returnMsg, aliOrderEntity);
            } else if (trade_status.equals(AlipayTradeStatus.TRADE_CLOSED.getStatus())) {
                aliOrderService.updateTradeStatusClosed(aliOrderEntity.getSysTradeNo());
                return R.error(60000, "订单失败");
            } else {
                return R.ok("用户支付中..");
            }
        } else if (aliOrderEntity.getPayMethod().equals("221") || aliOrderEntity.getPayMethod().equals("321") || aliOrderEntity.getPayMethod().equals("421")) {
            //个人码下单
            String retMsg = MobileRequest.queryOrder(aliOrderEntity.getSysTradeNo(), merchantEntity.getMobileUrl());
            logger.info("Mobile返回msg : {}", retMsg);
            if (retMsg.contains("未支付")) {
                return R.ok("用户支付中..");
            }
            if (retMsg.contains("支付成功")) {
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("orderId", aliOrderEntity.getSysTradeNo());
                aliOrderService.updateTradeOrder(paramMap);
                //客服人员主动查询 若已支付成功则主动发送通知
                String returnMsg = this.doNotify(aliOrderEntity.getNotifyUrl(), aliOrderEntity.getOrderId().toString(), AlipayTradeStatus.TRADE_SUCCESS.getStatus(), aliOrderEntity.getOrderAmount().toString(), aliOrderEntity.getPartner());
                return this.checkNotify(returnMsg, aliOrderEntity);
            } else {
                return R.error("查询失败");
            }
        } else {//微信查单
            String retMsg = WechatRequest.doWechatOrderQuery(merchantEntity.getWechatNum(), aliOrderEntity.getSysTradeNo(), "MD5", merchantEntity.getWechatKey());
            JSONObject resultJson = JSON.parseObject(retMsg);
            if (resultJson.getString("result_code").equals("SUCCESS")) {//第三方已下单
                if (resultJson.getString("trade_state").equals("SUCCESS")) {
                    //已支付
                    Map<String, Object> paramMap = new HashMap<>();
                    paramMap.put("orderId", aliOrderEntity.getSysTradeNo());
                    paramMap.put("tradeNo", resultJson.get("transaction_id"));
                    aliOrderService.updateTradeOrder(paramMap);
                    //客服人员主动查询 若已支付成功则主动发送通知
                    String returnMsg = this.doNotify(aliOrderEntity.getNotifyUrl(), aliOrderEntity.getOrderId().toString(), AlipayTradeStatus.TRADE_SUCCESS.getStatus(), aliOrderEntity.getOrderAmount().toString(), aliOrderEntity.getPartner());
                    return this.checkNotify(returnMsg, aliOrderEntity);
                } else {
                    //未支付
                    return R.error(40004, "用户支付中..");
                }
            } else {//第三方下单失败 订单不存在
                aliOrderService.updateTradeStatusClosed(aliOrderEntity.getSysTradeNo());
                return R.error(40005, "微信下单失败");
            }
        }
    }

    /**
     * 1、商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
     * 2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
     * 3、校验通知中的seller_id（或者seller_email)是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email），
     * 4、验证app_id是否为该商户本身。上述1、2、3、4有任何一个验证不通过，则表明本次通知是异常通知，务必忽略。
     * 在上述验证通过后商户必须根据支付宝不同类型的业务通知，正确的进行不同的业务处理，并且过滤重复的通知结果数据。
     * 在支付宝的业务通知中，只有交易通知状态为TRADE_SUCCESS或TRADE_FINISHED时，支付宝才会认定为买家付款成功。
     *
     * @param params
     * @throws AlipayApiException
     */
    private void check(Map<String, String> params) throws AlipayApiException {
        String outTradeNo = params.get("out_trade_no");

        // 1、商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
        // TODO: 2018/9/19 根据out_trade_no查询订单是否存在
        AliOrderEntity order = aliOrderService.queryBySysTradeNo(outTradeNo);
        if (order == null) {
            throw new AlipayApiException("out_trade_no错误");
        }

        // 2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
        logger.info("支付宝返回total_amount : {}", params.get("total_amount"));
        logger.info("订单total_amount : {}", order.getOrderAmount().toString());
        Double total_amount = Double.parseDouble(params.get("total_amount"));
//        if (total_amount != order.getOrderAmount()) {
//            throw new AlipayApiException("error total_amount");
//        }

        // 3、校验通知中的seller_id（或者seller_email)是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email），
        // 第三步可根据实际情况省略

        // 4、验证app_id是否为该商户本身。
        MerchantEntity merchantEntity = merchantService.findByPriKey(order.getPartner());
        if (merchantEntity == null) {
            throw new AlipayApiException("app_id不存在");
        }
        if (!params.get("app_id").equals(merchantEntity.getAppid().toString())) {
            throw new AlipayApiException("app_id不一致");
        }
    }

    @RequestMapping(value = "pageTest", method = RequestMethod.POST)
    public R payTest() {
        runAsync(() -> {
            String url = "http://localhost/api/v1/notify";
            Map<String, String> params = new HashMap<>();
            params.put("out_trade_no", "12115448574");
            params.put("trade_status", AlipayTradeStatus.TRADE_SUCCESS.getStatus());
            params.put("totalAmount", "1");
            String rreturnStr = HttpClientUtil.doPost(url, params);
            if (rreturnStr.equals("success")) {
                logger.info("通知商户成功，修改通知状态");
            } else {
                logger.error("通知商户失败");
            }
        });
        return R.ok().put("data", "test Ok !!!!!!!!!");
    }

    @RequestMapping(value = "notify", method = RequestMethod.POST)
    public String testNotify(HttpServletRequest request) {
        logger.info("进入模拟商户回调:{}", request);
        return "success";
    }

    @RequestMapping(value = "testAuth", method = RequestMethod.POST)
    public R testAuthToken() throws AlipayApiException {
        String appId = "2018091361348399";
        String priKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDEEDJ/VWM5YjjX Su0VPB3hSydKF8/jD8kplSXZPCnkBZJplH01ZY0SM6uzJ9f/LelVvgYBSusnw9NC erPM4W2/rjnrRnw4ZrLKJw29m5ueK2bZMTrufaQhBZHcFqNbtbBLfaoTd0gTDBgw klup88t7M8nvJVj1ZF+tVawVV3U2kT+TNW2A422wld4pTWNNDNofojdLT49yDvJU mYw9cCm/U9oWt5STfyYqQnn3LFJdtZboDvCppSzovhrCUxrgAdSKE+PjvirCePim xi5yr/u/9YHcl5+zl/YvEgh5Ab0QWWDGgi3BdjYdQWy/gk6mBYKrp4KbPOt4qZ9R zIKJfpn3AgMBAAECggEBALntdkXEbr0rRSX9WskpYliVEWQ1IqJ8BNMXKnZQlJU1 J3xSIU6yx838DBZwcWf/XOg/tKgjKM9j6AKCI+Hl4VLF4Q0ZoZFG6sPDt0cYusGK /RR4mB80LKJYCtNA8Jd2vAFK4S9mjYXqkUH2eVC47j0ehp/vteW30veoZ/ExJ+me tlw70cxONEE134MzelFpqSPKR+SAGajLTQmfJleK2VP0zXnXol03+dknOGMmehkG +24bTqoLkLgq53eS45aRr+KAhgvw8YzdzJR09MvSazk3EVRJNVflfsVPr8l9SeI0 ua63n+Y+5ivL5RRWG+FwUxIMuGyDXj71EaEhqEgIN5kCgYEA/Cqk3uFFF4di8Qfa RIO4kw0uEEibCAIQY3Dlm3++/8Q/qyVXM3bXZNXoMbzkjnWy0h3ObyU4deJiJWvt yJhYueRRozPkvnPIRxoZmW5UmUMMpKygp/95qwluLrygOd8qXGxj7HTX/NsekbFp Mg6X3mVSr1OJyzVLRhEkwTX7/J0CgYEAxws3VRuHvrPsLV0218TX86EC1QyIkaps p7Dq1hMyO8J/ddWpmabZzncCwa7RxH5FuB6FtEWT/vdzEoLmzCWOn9r44eqFtCup 7ZfSiwEbKLTL/JT520wwbFgLQs5U72qLUJuS9b4YzaBsPy2J5ZL5g1QPsBXSbaXh ZG9oAXqXKqMCgYBHLLowdqELzRjuM2s2H3+/cd7olbW8guihSMJmK557jqbx5a5B Rm/xdDb/ovNYCnyYtfUIhhbznxxYt6f8EJQqk/k++Wy9HO0QtJs5JV0XjUdKS7dk 88Uhs52372HCM+0+/REPMsOsCSUNtmecy2WuTicSeZ/RDY/bjQc0ycoAOQKBgAv8 zfFPPiarGV9GDOaD0Wm32b7RCWyUcgTLb+lCsLTOqvSC0LsTRGzd2AmoKLQH0tUa 6XyI8Dfb2U9VGOdUn7la68BXaoQGWS6ZBTIf9+3ErcBhjIa7mPL9fnggixb8OLW7 GOe58i54KezIJlTwPBy/tE4rkkfORLvSxdXI90+3AoGAMl8A+arhdkLfpf+vW7st ARL2oKd6JDhXm5SK8pcdzGSs+EheTXicdGq8y1ROCp3D7D4bpCKsHgpuYhP1GvOk xLXlJ2PMXI9QhCeeGtDehbJAD47+2NBg0KjXaaW+AJQFSYfIZXTb8FMVDOHg7blk KM9+c6kiVG1YSssE8Rcfum0=";
        String aliPubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAounwJ0cIGZLbI5m8CkqJydwl8sFlM1L1gTu10Cv5GZ76kxrRmi3KmSqNbkmY/lsQHhtmVXmSVPH/4ona0p8sNvpnnxxVLC+a2nJHgmDR59/7H+B6D4qMXW3CCHO1DCrtRvokwWb7+OLSIrrteNOGX2lg3hiRRD90UE1JBNOqX4601SYoUO7c4BPKCqQ371w2BfsTAv5jVn68soMMX9MLxICfOe58AvlpVBbe8JwBIVnW82gxyfYTBSbLQOPPSCreBlZ5PiBNvRZwEehd5RQLhCX2YB0ieq/vDLvi5Okzmjjqx8h9CR687Q0WQZ4Y+7tc68XBVd5xpv4TSz6SbwImmQIDAQAB";
        String authCode = "201809BB5d3abaf5424b441cbb09f4c15cb0aX07";
        String authTokenStr = AliPayRequest.getAuthToken(appId, priKey, aliPubKey, authCode);
        logger.info("authToken String : {}", authTokenStr);
        return R.ok();
    }

    private String doNotify(String url, String out_trade_no, String trade_status, String total_amount, String partner) {
        Map<String, String> params = new HashMap<>();
        params.put("out_trade_no", out_trade_no);
        params.put("trade_status", trade_status);
        params.put("totalAmount", total_amount);
        String signStr = "orderAmount=" + total_amount + "&orderId=" + out_trade_no + "&priKey=" + partner;
        String sign = MD5Utils.encode(signStr).toUpperCase();
        params.put("sign", sign);
        return HttpClientUtil.doPost(url, params);
    }

    private R checkNotify(String msg, AliOrderEntity aliOrderEntity) {
        if (msg.contains("success")) {
            logger.info("通知商户成功，修改通知状态");
            aliOrderService.updateNotifyStatus(aliOrderEntity.getSysTradeNo());
            return R.ok("用户已支付,已通知商户");
        } else {
            logger.error("通知商户失败");
            return R.ok("用户已支付,通知商户失败");
        }
    }

    @RequestMapping(value = "testQueryBySysTradeNo", method = RequestMethod.POST)
    public R testQueryBySysTradeNo(@RequestBody Map<String, String> map) {

        AliOrderEntity aliOrderEntity = aliOrderService.queryBySysTradeNo(map.get("sysTradeNo"));
        return R.ok().put("data", aliOrderEntity);

    }

    @RequestMapping(value = "testNginx", method = RequestMethod.POST)
    public R testNginx() {
        return R.ok().put("msg", "result from admin1");
        //return R.ok().put("msg","result from admin2");
    }

    @RequestMapping(value = "notifyMobile")
    public String getNotify(HttpServletRequest request) {
        System.out.println("============>>>: enter nofityByMobile");
        Map<String, String> params = AliUtils.convertRequestParamsToMap(request); // 将异步通知中收到的待验证所有参数都存放到map中
//        for (String key : params.keySet()) {
//            System.out.println("Key = " + key);
//            System.out.println("Value = " + params.get(key));
//        }
        //获取参数
        String money = params.get("money");
        String amount = money.substring(1);
        String trade_no = params.get("no");
        String sys_trade_no = params.get("mark");
        String sign = params.get("sign");
        String type = params.get("type");
        String dt = params.get("dt");
        //验签
        //1.获取sys_trade_no查询订单
        AliOrderEntity aliOrderEntity = aliOrderService.queryBySysTradeNo(sys_trade_no);
        if (aliOrderEntity == null) {
            return "success订单不存在";
        }
        logger.info("回调参数 ， money = {} ，no = {} , mark = {} , type = {} , dt = {} ", money , trade_no , sys_trade_no , type , dt);
        logger.info("回调金额 ， amount = {}", amount);
        logger.info("订单金额 ， amount = {}", aliOrderEntity.getOrderAmount().toString());
        //11.21修改 支付宝返回参数修改 金额带上符号
        String checkSignStr = "";
        if (type.equals("支付宝") || type.equals("alipay")){
            if ("￥".equals(money.substring(0,1))){
                checkSignStr = "dt=" + dt + "&mark=" + sys_trade_no + "&money=" +  money.substring(0,1) + aliOrderEntity.getOrderAmount() + "&no=" + trade_no + "&type=" + type + aliOrderEntity.getPartner();
            }else {
                checkSignStr = "dt=" + dt + "&mark=" + sys_trade_no + "&money=" + aliOrderEntity.getOrderAmount() + "&no=" + trade_no + "&type=" + type + aliOrderEntity.getPartner();
            }
        }else {
            checkSignStr = "dt=" + dt + "&mark=" + sys_trade_no + "&money=" + money.substring(0,1) +aliOrderEntity.getOrderAmount() + "&no=" + trade_no + "&type=" + type + aliOrderEntity.getPartner();
        }
        String checkSign = MD5Utils.encode(checkSignStr);
        logger.info("sign , sign = {}", sign);
        logger.info("sign , checkSignStr = {}", checkSignStr);
        logger.info("sign , checkSign = {}", checkSign);
        if (!SignUtil.checkSign(sign.toUpperCase(), checkSignStr)) {
            return "success验签失败";
        }
        //验签通过修改订单状态,通知商户
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", aliOrderEntity.getSysTradeNo());
        map.put("tradeNo", trade_no);
        map.put("payTime", DateUtil.dtToStr(dt));
        aliOrderService.updateTradeOrder(map);
        //通知
        String returnMsg = this.doNotify(aliOrderEntity.getNotifyUrl(), aliOrderEntity.getOrderId().toString(), AlipayTradeStatus.TRADE_SUCCESS.getStatus(), aliOrderEntity.getOrderAmount().toString(), aliOrderEntity.getPartner());
        if (returnMsg.contains("success") || returnMsg.contains("SUCCESS")) {
            logger.info("通知商户成功，修改通知状态");
            aliOrderService.updateNotifyStatus(aliOrderEntity.getSysTradeNo());
        } else {
            logger.error("通知商户失败");
        }
        return "success";
    }

    @RequestMapping(value = "mobile")
    public R doMobileReq() {
        String url = "http://369pay.s1.natapp.cc";
        String result = MobileRequest.queryOrder("36946610241532243250", url);
        return null;
    }

    @RequestMapping(value = "wechatNotify")
    public String wechatNotify(HttpServletRequest request) {
        logger.info("==================进入wechant Notify===================");
        Map<String, String> params = AliUtils.convertRequestParamsToMap(request);// 将异步通知中收到的待验证所有参数都存放到map中
        //1.获取sys_trade_no查询订单
        AliOrderEntity aliOrderEntity = aliOrderService.queryBySysTradeNo(params.get("out_trade_no"));
        if (aliOrderEntity == null) {
            return "failure订单不存在";
        }
        MerchantEntity merchantEntity = merchantService.findByPriKey(aliOrderEntity.getPartner());
        if (merchantEntity == null) {
            logger.info("商户不存在");
            return "failure";
        }
        // TODO: 2018/11/1 查询商户 获取商户密钥
        String signStr = String.format("bank_type=%s&inst_id=%s&out_trade_no=%s&result_code=%s&return_code=%s&return_msg=%s&sign_type=%s&time_end=%s&total_fee=%s&transaction_id=%s&key=%s",
                params.get("bank_type"), params.get("inst_id"), params.get("out_trade_no"),
                params.get("result_code"), params.get("return_code"), params.get("return_msg"),
                params.get("sign_type"), params.get("time_end"), BigDecimalUtil.changeY2F(aliOrderEntity.getOrderAmount().toString()),
                params.get("transaction_id"), merchantEntity.getWechatKey());//暂时写死 后面从商户配置中获取
        if (!SignUtil.checkSign(params.get("sign"), signStr)) {
            return "failure验签失败";
        }
        //验签通过修改订单状态,通知商户
        runAsync(() -> {
            Map<String, Object> map = new HashMap<>();
            map.put("orderId", aliOrderEntity.getSysTradeNo());
            map.put("tradeNo", params.get("transaction_id"));
            aliOrderService.updateTradeOrder(map);
            //通知
            String returnMsg = this.doNotify(aliOrderEntity.getNotifyUrl(), aliOrderEntity.getOrderId().toString(), AlipayTradeStatus.TRADE_SUCCESS.getStatus(), aliOrderEntity.getOrderAmount().toString(), aliOrderEntity.getPartner());
            if (returnMsg.contains("success")) {
                logger.info("通知商户成功，修改通知状态");
                aliOrderService.updateNotifyStatus(aliOrderEntity.getSysTradeNo());
            } else {
                logger.error("通知商户失败");
            }
        });
        return "SUCCESS";
    }

    public static void main(String[] args) {
//        String str = "http://mobile.qq.com/qrcode?url=";
//        String add = "https://i.qianbao.qq.com/wallet/sqrcode.htm?m=tenpay&f=wallet&u=1491522516&a=1&n=L。&ac=2674F0DA92BE37A0D55FE71D840858951AC1FA7D439779F34D8A45280D16170C058209CECBF0B0067AE8FDB11292117295A1F43D903F0EFF89CC95EB5B238A9A";
//        String qrcodeUrl = add ;
//        String imgStr = ImageToBase64Util.createQRCode(qrcodeUrl);
//        System.out.println(0 == false ? 1 : 2);
    }

    @RequestMapping(value = "wechat")
    public R testWechat(HttpServletRequest request) throws Exception {
        logger.info("==================进入wechant===================");
//        String msg = WechatRequest.doWechatOrderCreate("10000","MD5","http://9jiqzs.natappfree.cc/api/v1/wechatNotify",
//                "2018103112460020","100","MWEB","c6e285907444ace4568ae3dfaaac78da","123");
        logger.info(request.getHeader("X-FORWARDED-FOR"));
        logger.info(request.getHeader("Proxy-Client-IP"));
        logger.info(request.getHeader("WL-Proxy-Client-IP"));
        logger.info(request.getHeader("HTTP_CLIENT_IP"));
        logger.info(request.getHeader("X-Real-IP"));
        logger.info(request.getRemoteAddr());
        logger.info(NetworkUtil.getIpAddress(request));
        return R.ok();
    }

//    @RequestMapping(value = "testRedis")
//    public R testRedis(@RequestBody Map params){
//        if (redisTemplate.hasKey(params.get("mark").toString())){
//            return R.ok("已通知");
//        }
//        redisTemplate.opsForValue().set(params.get("mark").toString(),params.get("mark").toString(),24*60*60*1000);
//        return R.ok();
//    }

    @RequestMapping(value = "trade" , method = RequestMethod.GET)
    public void testAndriod(HttpServletResponse response , @RequestParam String sysTradeNo) throws Exception{
        AliOrderEntity aliOrderEntity = aliOrderService.queryBySysTradeNo(sysTradeNo);
        if (aliOrderEntity == null) {
            logger.info("订单不存在 ， 订单系统订单号 : {}" , sysTradeNo);
            response.sendRedirect("http://admin.vcapay.com.cn:8080/pay-admin/404.html");
        }
        MerchantEntity merchant = merchantService.queryById(aliOrderEntity.getMerchantId());
        if (merchant == null) {
            logger.info("商户不存在 ， 订单系统订单号 : {}" , sysTradeNo);
            response.sendRedirect("http://admin.vcapay.com.cn:8080/pay-admin/404.html");
        }
        String mobileUrl = "";
        Long channelId = 0L;
        String aliUserId = "";
        if (merchant.getPriFlag() == 0){
            mobileUrl = merchant.getMobileUrl();
        }else {
            List<ChannelEntity> channelEntityList = channelService.queryUseableChannelByMerchantId(merchant.getId());
            if (merchant.getPollingFlag() == 1){//开启轮询
                int index = PollingUtil.RandomIndex(channelEntityList.size());
                mobileUrl = channelEntityList.get(index).getUrl();
                channelId = channelEntityList.get(index).getId();
                aliUserId = channelEntityList.get(index).getAliUserId();
            }else {//轮询关闭
                mobileUrl = channelEntityList.get(0).getUrl();
                channelId = channelEntityList.get(0).getId();
                aliUserId = channelEntityList.get(0).getAliUserId();
            }
        }
        logger.info("支付宝个码下单 ， 商户id : {} ， 通道Id : {} , aliUserId : {} " , merchant.getId() , channelId , aliUserId);
        response.sendRedirect("http://admin.vcapay.com.cn:8080/pay-admin/modules/aliPayTest/aliPay2.html?userId="+aliUserId+"&amount="+aliOrderEntity.getOrderAmount()+"&mark="+aliOrderEntity.getSysTradeNo());
    }
}

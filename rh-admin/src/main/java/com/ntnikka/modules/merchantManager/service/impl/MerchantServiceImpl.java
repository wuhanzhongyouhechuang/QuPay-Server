package com.ntnikka.modules.merchantManager.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ntnikka.common.utils.EmptyUtil;
import com.ntnikka.common.utils.Query;
import com.ntnikka.modules.merchantManager.dao.MerchantDao;
import com.ntnikka.modules.merchantManager.entity.MerchantDept;
import com.ntnikka.modules.merchantManager.entity.MerchantEntity;
import com.ntnikka.modules.merchantManager.service.MerchantDeptService;
import com.ntnikka.modules.merchantManager.service.MerchantService;
import com.ntnikka.modules.orderManager.entity.TradeOrder;
import com.ntnikka.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service("merchantService")
public class MerchantServiceImpl extends ServiceImpl<MerchantDao, MerchantEntity> implements MerchantService {

    @Autowired
    MerchantDao merchantDao;

    @Autowired
    MerchantDeptService merchantDeptService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        //String merchantName = (String) params.get("merchantName");
        String merchantId = (String) params.get("id");
        Page<MerchantEntity> page = this.selectPage(
                new Query<MerchantEntity>(params).getPage(),
                new EntityWrapper<MerchantEntity>().eq(EmptyUtil.isNotEmpty(merchantId), "id", EmptyUtil.isNotEmpty(merchantId) ? Long.parseLong(merchantId) : "")
        );

        return new PageUtils(page);
    }

    @Override
    public void updateTradeStatus(Map<String, Object> params) {
        merchantDao.updateTradeStatus(params);
    }

    @Override
    @Transactional
    public void save(MerchantEntity merchantEntity) {
        this.insert(merchantEntity);
        MerchantDept merchantDept = new MerchantDept();
        merchantDept.setName(merchantEntity.getMerchantName());
        merchantDept.setParentId(merchantEntity.getMerchantDeptId());
        merchantDept.setOrderNum(0);
        merchantDept.setDelFlag(0);
        merchantDept.setMerchantId(merchantEntity.getId());
        merchantDeptService.insert(merchantDept);
    }

    @Override
    public MerchantEntity findByPriKey(String priKey) {
        return merchantDao.findByPriKey(priKey);
    }

    @Override
    public MerchantEntity queryById(Long merchantId) {
        return merchantDao.findById(merchantId);
    }

    @Override
    public void updateSettleStatus(Map<String, Object> params) {
       merchantDao.updateSettleStatus(params);
    }

    @Override
    public List<MerchantEntity> hasOrder() {
        return merchantDao.hasOrder();
    }

    @Override
    public List<TradeOrder> queryYesterdayOrderList(Map<String, Object> params) {
        return merchantDao.queryYesterdayOrderList(params);
    }
}

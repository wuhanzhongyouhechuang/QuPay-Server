package com.ntnikka.modules.merchantManager.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ntnikka.modules.common.utils.EmptyUtil;
import com.ntnikka.modules.common.utils.Query;
import com.ntnikka.modules.merchantManager.dao.MerchantDao;
import com.ntnikka.modules.merchantManager.entity.MerchantDept;
import com.ntnikka.modules.merchantManager.entity.MerchantEntity;
import com.ntnikka.modules.merchantManager.service.MerchantDeptService;
import com.ntnikka.modules.merchantManager.service.MerchantService;
import com.ntnikka.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


@Service("merchantService")
public class MerchantServiceImpl extends ServiceImpl<MerchantDao, MerchantEntity> implements MerchantService {

    @Autowired
    MerchantDao merchantDao;

    @Autowired
    MerchantDeptService merchantDeptService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String merchantName = (String) params.get("merchantName");
        Page<MerchantEntity> page = this.selectPage(
                new Query<MerchantEntity>(params).getPage(),
                new EntityWrapper<MerchantEntity>().like(EmptyUtil.isNotEmpty(merchantName), "merchant_name", merchantName)
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
}

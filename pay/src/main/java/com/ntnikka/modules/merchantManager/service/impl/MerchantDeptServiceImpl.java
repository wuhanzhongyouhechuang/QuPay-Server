package com.ntnikka.modules.merchantManager.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ntnikka.modules.common.utils.Constant;
import com.ntnikka.modules.merchantManager.dao.MerchantDeptDao;
import com.ntnikka.modules.merchantManager.entity.MerchantDept;
import com.ntnikka.modules.merchantManager.service.MerchantDeptService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName MerchantDeptServiceImpl
 * @Author Liuq
 * @Description todo
 * @Date 2018/9/18 14:17
 **/
@Service("MerchantDeptService")
public class MerchantDeptServiceImpl extends ServiceImpl<MerchantDeptDao, MerchantDept> implements MerchantDeptService {
    @Override
    public List<MerchantDept> queryList(Map<String, Object> params) {
        List<MerchantDept> deptList =
                this.selectList(new EntityWrapper<MerchantDept>()
                        .addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER)));

        for (MerchantDept merchantDept : deptList) {
            MerchantDept parentDeptEntity = this.selectById(merchantDept.getParentId());
            if (parentDeptEntity != null) {
                merchantDept.setParentName(parentDeptEntity.getName());
            }
        }
        return deptList;
    }

    @Override
    public List<Long> queryDetpIdList(Long parentId) {
        return baseMapper.queryDetpIdList(parentId);
    }

    @Override
    public List<Long> getSubDeptIdList(Long deptId) {
        //部门及子部门ID列表
        List<Long> deptIdList = new ArrayList<>();

        //获取子部门ID
        List<Long> subIdList = queryDetpIdList(deptId);
        getDeptTreeList(subIdList, deptIdList);

        return deptIdList;
    }

    /**
     * 递归
     */
    private void getDeptTreeList(List<Long> subIdList, List<Long> deptIdList) {
        for (Long deptId : subIdList) {
            List<Long> list = queryDetpIdList(deptId);
            if (list.size() > 0) {
                getDeptTreeList(list, deptIdList);
            }

            deptIdList.add(deptId);
        }
    }

    @Override
    public List<Long> queryMerchantDeptIdList(Long parentId) {
        return baseMapper.queryMerchantDeptIdList(parentId);
    }
}

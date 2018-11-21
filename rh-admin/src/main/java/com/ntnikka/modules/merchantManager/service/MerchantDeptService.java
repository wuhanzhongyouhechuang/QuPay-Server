package com.ntnikka.modules.merchantManager.service;

import com.baomidou.mybatisplus.service.IService;
import com.ntnikka.modules.merchantManager.entity.MerchantDept;
import com.ntnikka.modules.sys.entity.SysDeptEntity;

import java.util.List;
import java.util.Map;

/**
 * @ClassName MerchantDeptService
 * @Author Liuq
 * @Description todo
 * @Date 2018/9/18 14:16
 **/
public interface MerchantDeptService extends IService<MerchantDept> {

    List<MerchantDept> queryList(Map<String, Object> map);

    /**
     * 查询子部门ID列表
     *
     * @param parentId 上级部门ID
     */
    List<Long> queryDetpIdList(Long parentId);

    /**
     * 获取子部门ID，用于数据过滤
     */
    List<Long> getSubDeptIdList(Long deptId);

    /**
     * 查询子部门部门ID列表
     *
     * @param parentId
     * @return
     */
    List<Long> queryMerchantDeptIdList(Long parentId);

    List<MerchantDept> queryMerchantParentDeptList();

    List<MerchantDept> queryListForMerchant(List<String> idList);
}

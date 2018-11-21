package com.ntnikka.modules.apply.service;

import com.baomidou.mybatisplus.service.IService;
import com.ntnikka.modules.apply.entity.GbEntity;
import com.ntnikka.modules.sys.entity.SysUserEntity;
import com.ntnikka.utils.PageUtils;

import java.util.List;
import java.util.Map;


/**
 * 系统用户
 */
public interface GbService extends IService<GbEntity> {

//	PageUtils queryPage(Map<String, Object> params);
//
//	/**
//	 * 查询用户的所有Gb ID
//	 */
//	List<Long> queryAllGbId(Long gbId);

    /**
     * 保存国民经济行业分类库
     */
    void save(GbEntity gb);

    /**
     * 修改用户
     */
    void update(GbEntity gb);

    /**
     * 列表
     */
    List<GbEntity> queryAll();

    List<Map<String, Object>> getAllCids();

    List<Map<String, Object>> getAllBids();

    List<Map<String, Object>> getAllMids();

    List<Map<String, Object>> getAllLids();

}


package com.ntnikka.modules.sys.service;


import com.baomidou.mybatisplus.service.IService;
import com.ntnikka.modules.sys.entity.SysRoleEntity;
import com.ntnikka.utils.PageUtils;

import java.util.Map;


/**
 * 角色
 */
public interface SysRoleService extends IService<SysRoleEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void save(SysRoleEntity role);

    void update(SysRoleEntity role);

    void deleteBatch(Long[] roleIds);

}


package com.ntnikka.common.aspect;


import com.ntnikka.common.annotation.DataFilter;
import com.ntnikka.common.utils.Constant;
import com.ntnikka.exception.RRException;
import com.ntnikka.modules.sys.entity.SysUserEntity;
import com.ntnikka.modules.sys.service.SysDeptService;
import com.ntnikka.modules.sys.service.SysRoleDeptService;
import com.ntnikka.modules.sys.service.SysUserRoleService;
import com.ntnikka.modules.sys.shiro.ShiroUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 数据过滤，切面处理类
 */
@Aspect
@Component
public class DataFilterAspect {
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysRoleDeptService sysRoleDeptService;

    @Pointcut("@annotation(com.ntnikka.common.annotation.DataFilter)")
    public void dataFilterCut() {

    }

    @Before("dataFilterCut()")
    public void dataFilter(JoinPoint point) throws Throwable {
        Object params = point.getArgs()[0];
        if (params != null && params instanceof Map) {
            SysUserEntity user = ShiroUtils.getUserEntity();

            //如果不是超级管理员，则进行数据过滤
            if (user.getUserId() != Constant.SUPER_ADMIN) {
                Map map = (Map) params;
                map.put(Constant.SQL_FILTER, getSQLFilter(user, point));
            }

            return;
        }

        throw new RRException("数据权限接口，只能是Map类型参数，且不能为NULL");
    }

    /**
     * 获取数据过滤的SQL
     */
    private String getSQLFilter(SysUserEntity user, JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        DataFilter dataFilter = signature.getMethod().getAnnotation(DataFilter.class);
        //获取表的别名
        String tableAlias = dataFilter.tableAlias();
        if (StringUtils.isNotBlank(tableAlias)) {
            tableAlias += ".";
        }

        //用户部门ID列表
        Set<Long> deptIdList = new HashSet<>();
        deptIdList.add(user.getDeptId());

        //用户角色对应的部门ID列表
        List<Long> roleIdList = sysUserRoleService.queryRoleIdList(user.getUserId());
        if (roleIdList.size() > 0) {
            List<Long> userDeptIdList = sysRoleDeptService.queryDeptIdList(roleIdList.toArray(new Long[roleIdList.size()]));
            deptIdList.addAll(userDeptIdList);
        }

        //用户子部门ID列表
        if (dataFilter.subDept()) {
            List<Long> subDeptIdList = sysDeptService.getSubDeptIdList(user.getDeptId());
            deptIdList.addAll(subDeptIdList);
        }

        StringBuilder sqlFilter = new StringBuilder();
        sqlFilter.append(" (");
        sqlFilter.append(tableAlias).append("dept_id in(").append(StringUtils.join(deptIdList, ",")).append(")");

        //没有本部门数据权限，也能查询本人数据
        if (dataFilter.user()) {
            sqlFilter.append(" or ").append(tableAlias).append("user_id=").append(user.getUserId());
        }
        sqlFilter.append(")");

        return sqlFilter.toString();
    }
}

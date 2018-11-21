package com.ntnikka.modules.merchantManager.controller;

import com.ntnikka.modules.merchantManager.entity.MerchantDept;
import com.ntnikka.modules.merchantManager.service.MerchantDeptService;
import com.ntnikka.modules.sys.controller.AbstractController;
import com.ntnikka.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * @ClassName MerchantDeptController
 * @Author Liuq
 * @Description todo
 * @Date 2018/9/18 14:27
 **/
@RestController
@RequestMapping("/merchant/dept")
public class MerchantDeptController extends AbstractController {

    @Autowired
    MerchantDeptService merchantDeptService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public List<MerchantDept> list() {
        List<MerchantDept> deptList = merchantDeptService.queryList(new HashMap<String, Object>());

        return deptList;
    }

    /**
     * 选择部门(添加、修改菜单)
     */
    @RequestMapping("/select")
    public R select() {
        List<MerchantDept> deptList = merchantDeptService.queryList(new HashMap<String, Object>());

        //添加一级部门
//        if(getUserId() == Constant.SUPER_ADMIN){
//            MerchantDept root = new MerchantDept();
//            root.setDeptId(0L);
//            root.setName("一级部门");
//            root.setParentId(-1L);
//            root.setOpen(true);
//            deptList.add(root);
//        }

        return R.ok().put("deptList", deptList);
    }

    /**
     * 上级部门Id(管理员则为0)
     */
    @RequestMapping("/info")
    public R info() {
        long deptId = 0;
//        if(getUserId() != Constant.SUPER_ADMIN){
//            List<MerchantDept> deptList = merchantDeptService.queryList(new HashMap<String, Object>());
//            Long parentId = null;
//            for(MerchantDept merchantDept : deptList){
//                if(parentId == null){
//                    parentId = merchantDept.getParentId();
//                    continue;
//                }
//
//                if(parentId > merchantDept.getParentId().longValue()){
//                    parentId = merchantDept.getParentId();
//                }
//            }
//            deptId = parentId;
//        }

        return R.ok().put("deptId", deptId);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{deptId}")
    public R info(@PathVariable("deptId") Long deptId) {
        MerchantDept dept = merchantDeptService.selectById(deptId);

        return R.ok().put("dept", dept);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MerchantDept dept) {
        merchantDeptService.insert(dept);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MerchantDept dept) {
        merchantDeptService.updateById(dept);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(long deptId) {
        //判断是否有子部门
        List<Long> deptList = merchantDeptService.queryDetpIdList(deptId);
        if (deptList.size() > 0) {
            return R.error("请先删除子部门");
        }

        merchantDeptService.deleteById(deptId);
        return R.ok();
    }
}

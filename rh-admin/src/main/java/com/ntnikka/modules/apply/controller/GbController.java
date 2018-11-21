package com.ntnikka.modules.apply.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ntnikka.modules.apply.entity.GbEntity;
import com.ntnikka.modules.apply.entity.MyGbEntity;
import com.ntnikka.modules.apply.service.GbService;
import com.ntnikka.modules.apply.service.MyGbService;
import com.ntnikka.modules.sys.controller.AbstractController;
import com.ntnikka.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * @author luk
 * @create 2018-04-08 14:50
 * @desc 国民经济行业代码controller
 **/
@RestController
@RequestMapping("/apply/mygb")
public class GbController extends AbstractController {

    @Autowired
    private MyGbService myGbService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public List<MyGbEntity> list() {
        List<MyGbEntity> gbList = myGbService.queryAll();
        return gbList;
    }


}

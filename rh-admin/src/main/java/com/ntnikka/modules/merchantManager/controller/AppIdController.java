package com.ntnikka.modules.merchantManager.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.ntnikka.common.utils.PageUtils;
import com.ntnikka.modules.merchantManager.entity.AppIdEntity;
import com.ntnikka.modules.merchantManager.service.AppIdService;
import com.ntnikka.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Liuq
 * @email 530775870@qq.com
 * @date 2018-10-10 11:56:51
 */
@RestController
@RequestMapping("/merchant/appid")
public class AppIdController {
    @Autowired
    private AppIdService appIdService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = appIdService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {
        AppIdEntity appId = appIdService.selectById(id);

        return R.ok().put("appId", appId);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AppIdEntity appId) {
        appIdService.insert(appId);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AppIdEntity appId) {
        appIdService.updateById(appId);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Map ids) {
        String idListStr = ids.get("ids").toString();
        appIdService.deleteBatchIds(Arrays.asList(idListStr));
        return R.ok();
    }

    @RequestMapping("/queryAppid")
    public R queryAppidList() {
        List<Long> appidList = appIdService.queryAppid();
        return R.ok().put("appId", appidList);
    }

    @RequestMapping("/queryInfoByAppid")
    public R queryAppidInfo(@RequestBody Map map) {
        AppIdEntity appIdEntity = appIdService.queryInfoByAppid(map);
        return R.ok().put("appId", appIdEntity);
    }

}

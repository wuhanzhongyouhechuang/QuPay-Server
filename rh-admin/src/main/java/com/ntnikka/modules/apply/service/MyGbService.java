package com.ntnikka.modules.apply.service;

import com.baomidou.mybatisplus.service.IService;
import com.ntnikka.modules.apply.entity.MyGbEntity;

import java.util.List;

/**
 * @author luk
 * @create 2018-04-10 14:24
 * @desc MyGbService
 **/
public interface MyGbService extends IService<MyGbEntity> {

    void save(MyGbEntity mygb);

    List<MyGbEntity> queryAll();

    Long getIdByCode(String code);

}

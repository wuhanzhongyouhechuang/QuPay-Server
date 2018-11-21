package com.ntnikka.modules.apply.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ntnikka.modules.apply.dao.GbDao;
import com.ntnikka.modules.apply.dao.MyGbDao;
import com.ntnikka.modules.apply.entity.GbEntity;
import com.ntnikka.modules.apply.entity.MyGbEntity;
import com.ntnikka.modules.apply.service.GbService;
import com.ntnikka.modules.apply.service.MyGbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author luk
 * @create 2018-04-10 14:26
 * @desc MyGbService实现类
 **/
@Service("MyGbService")
public class MyGbServiceImpl extends ServiceImpl<MyGbDao, MyGbEntity> implements MyGbService {

    @Autowired
    private MyGbDao myGbDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(MyGbEntity mygb) {
        this.insert(mygb);
    }

    @Override
    public List<MyGbEntity> queryAll() {
        return this.selectList(null);
    }

    @Override
    public Long getIdByCode(String code) {
        return myGbDao.getIdByCode(code);
    }

}

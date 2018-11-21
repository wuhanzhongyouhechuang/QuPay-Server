package com.ntnikka.modules.apply.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ntnikka.modules.apply.dao.GbDao;
import com.ntnikka.modules.apply.entity.GbEntity;
import com.ntnikka.modules.apply.service.GbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author luk
 * @create 2018-04-04 14:44
 * @desc GbService实现类
 **/
@Service("GbService")
public class GbServiceImpl extends ServiceImpl<GbDao, GbEntity> implements GbService {

    @Autowired
    private GbDao gbDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(GbEntity gb) {
        this.insert(gb);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(GbEntity gb) {
        this.updateById(gb);
    }

    //select * 取所有值(参数传null)
    @Override
    public List<GbEntity> queryAll() {
        return this.selectList(null);
    }

    @Override
    public List<Map<String, Object>> getAllCids() {
        return gbDao.getCids();
    }

    @Override
    public List<Map<String, Object>> getAllBids() {
        return gbDao.getBids();
    }

    @Override
    public List<Map<String, Object>> getAllMids() {
        return gbDao.getMids();
    }

    @Override
    public List<Map<String, Object>> getAllLids() {
        return gbDao.getLids();
    }


}

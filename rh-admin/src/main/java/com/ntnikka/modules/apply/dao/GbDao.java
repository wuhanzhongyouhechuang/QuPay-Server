package com.ntnikka.modules.apply.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ntnikka.modules.apply.entity.GbEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author luk
 * @create 2018-04-04 14:46
 * @desc Gb处理DAO
 **/
@Repository
public interface GbDao extends BaseMapper<GbEntity> {

    List<Map<String, Object>> getCids();

    List<Map<String, Object>> getBids();

    List<Map<String, Object>> getMids();

    List<Map<String, Object>> getLids();

}

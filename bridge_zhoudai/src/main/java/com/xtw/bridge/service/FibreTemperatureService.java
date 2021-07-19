package com.xtw.bridge.service;

import com.xtw.bridge.mapper.FibreTemperatureDao;
import com.xtw.bridge.model.FibreTemperature;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * User: Mr.Chen
 * Date: 2021/7/19
 * Description: No Description
 */
@Service
public class FibreTemperatureService implements FibreTemperatureDao {

    @Resource
    FibreTemperatureDao fibreTemperatureDao;

    @Override
    public int insertData(FibreTemperature fibreTemperature) {
        return fibreTemperatureDao.insertData(fibreTemperature);
    }
}

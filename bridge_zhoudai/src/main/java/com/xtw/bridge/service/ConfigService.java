package com.xtw.bridge.service;

import com.xtw.bridge.mapper.ConfigDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/8/5
 * Description: No Description
 */
@Service
public class ConfigService implements ConfigDao{

    @Resource
    ConfigDao configDao;

    // 查询外置局放所有配置
    @Override
    public List<HashMap> queryOutPartialConfig() {
        return configDao.queryOutPartialConfig();
    }

    @Override
    public List<HashMap> queryFibreTemperatureConfig() {
        return configDao.queryFibreTemperatureConfig();
    }


}

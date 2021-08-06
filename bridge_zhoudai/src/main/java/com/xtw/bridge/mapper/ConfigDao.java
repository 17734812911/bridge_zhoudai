package com.xtw.bridge.mapper;

import java.util.HashMap;
import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/8/5
 * Description: 设备配置接口
 */
public interface ConfigDao {

    // 查询外置局放所有配置
    public List<HashMap> queryOutPartialConfig();

    // 查询光纤测温所有配置
    public List<HashMap> queryFibreTemperatureConfig();
}

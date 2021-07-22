package com.xtw.bridge.mapper;

import com.xtw.bridge.model.FibreTemperature;
import com.xtw.bridge.model.FibreTemperatureConfig;

import java.util.List;


/**
 * User: Mr.Chen
 * Date: 2021/7/19
 * Description: No Description
 */
public interface FibreTemperatureDao {

    // 插入光纤测温数据
    public int insertData(FibreTemperature fibreTemperature);

    // 根据配备IP和通道ID 查询光纤测温配置
    public List<FibreTemperatureConfig> queryFibreTemperatureConfig(String deviceIp, String channel);


}

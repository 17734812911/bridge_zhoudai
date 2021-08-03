package com.xtw.bridge.mapper;

import com.xtw.bridge.model.FibreTemperature;
import com.xtw.bridge.model.FibreTemperatureAlert;
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

    // 插入告警数据
    public int insertAlertData(FibreTemperatureAlert fibreTemperatureAlert);

    // 根据分区ID查询三相光纤测温数据
    public List<FibreTemperature> queryDatasById(int partitionId);

    // 查询光纤测温所有数据(所有分区三相最大值)
    public List<FibreTemperature> queryAllPartitionMaxValue();

    // 查询光纤测温通道的读取顺序
    public boolean queryReadOrder();
}

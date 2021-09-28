package com.xtw.bridge.service;

import com.xtw.bridge.model.FibreTemperature;
import com.xtw.bridge.model.FibreTemperatureAlert;
import com.xtw.bridge.model.FibreTemperatureConfig;
import com.xtw.bridge.model.page.PageRequest;
import com.xtw.bridge.model.page.PageResult;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/8/16
 * Description: No Description
 */
public interface FibreTemperatureService {
    // 插入光纤测温数据
    public int insertData(FibreTemperature fibreTemperature);

    // 根据配备IP和通道ID 查询光纤测温配置
    public List<FibreTemperatureConfig> queryFibreTemperatureConfig(String deviceIp, String channel);

    // 插入告警数据
    public int insertAlertData(FibreTemperatureAlert fibreTemperatureAlert);

    // 根据分区ID查询三相光纤测温数据
    public List<FibreTemperature> queryDatasById(Integer partitionId);

    // 按时间查询光纤测温并分页
    public List queryDatasPage(Integer partitionId, String beginTime, String endTime);

    // 查询光纤测温所有数据(所有分区三相最大值)
    public List<FibreTemperature> queryAllPartitionMaxValue(String begintime, String endtime);

    // 查询光纤测温通道的读取顺序
    public boolean queryReadOrder();

    // 根据分区id和数据点位查询该点历史数据
    public List<FibreTemperature> queryHistoricalDatas(String beginTime, String endTime, Integer partitionId);

    // 解析数据
    public LinkedHashMap<String, Object> parseData(String begintime, String endtime);

    // 根据分区id和数据点位查询该点历史数据
    public List<LinkedHashMap<String, Object>> queryHistoricalDatasByCondition(String beginTime, String endTime, Integer partitionId, int point);
}

package com.xtw.bridge.mapper;

import com.xtw.bridge.model.FibreTemperature;
import com.xtw.bridge.model.FibreTemperatureAlert;
import com.xtw.bridge.model.FibreTemperatureConfig;
import com.xtw.bridge.model.page.PageRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * User: Mr.Chen
 * Date: 2021/7/19
 * Description: No Description
 */
public interface FibreTemperatureDao {

    // 插入光纤测温数据
    public int insertData(FibreTemperature fibreTemperature);

    // 向device表更新光纤测温最新数据时间
    public int updateDataTime(String dateTime);

    // 根据配备IP和通道ID 查询光纤测温配置
    public List<FibreTemperatureConfig> queryFibreTemperatureConfig(String deviceIp, String channel);

    // 插入告警数据
    public int insertAlertData(FibreTemperatureAlert fibreTemperatureAlert);

    // 根据分区ID查询三相光纤测温数据
    public List<FibreTemperature> queryDatasById(Integer partitionId);

    // 按时间查询光纤测温并分页
    public List<FibreTemperature> queryDatasPage(Integer partitionId, String beginTime, String endTime, Integer limitStart, Integer LimitLength);

    // 查询符合条件的数据的总数
    public Integer queryDataCount(Integer partitionId, String beginTime, String endTime);

    // 按时间查询光纤测温最新数据
    public List<FibreTemperature> queryDatasPageDesc(Integer partitionId, String beginTime, String endTime, Integer limitStart, Integer LimitLength);

    // 查询光纤测温所有数据(所有分区三相最大值)
    public List<FibreTemperature> queryAllPartitionMaxValue(String begintime, String endtime);

    // 查询所有分区光纤测温三相中的最大值及所在点位
    public List<FibreTemperature> queryAllMaxAndPoint();

    // 查询光纤测温通道的读取顺序
    public boolean queryReadOrder();

    // 根据分区id和数据点位查询该点历史数据
    public List<FibreTemperature> queryHistoricalDatas(String beginTime, String endTime, Integer partitionId);

    // 查询起止点位
    public HashMap<String,Integer> queryStartEndPoint(Integer partitionId);

}

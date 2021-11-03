package com.xtw.bridge.mapper;

import com.xtw.bridge.model.OutPartial;

import java.util.HashMap;
import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/7/28
 * Description: No Description
 */
public interface OutPartialDao {

    // 根据分区ID查询该分区所有外置局放设备的数据
    public List<HashMap<String,String>> queryOutPartialMaxValue(int partitionId);

    // 根据分区ID和设备ID查询该分区的外置局放设备的历史趋势(最大放电量、对应时间)
    public List<HashMap<String,String>> queryOutPartitionTrend(int partitionId, String terminalId, String begintime, String endtime);

    // 外置局放历史分析(最大放电量、最大放电频次、时间)
    public List<HashMap<String,String>> historicalAnalysis(int partitionId, String terminalId, String begintime, String endtime);

    // 根据外置局放设备id查询该设备最新数据
    public List<OutPartial> queryOutPartialData(String terminalId);

    // 获取所有外置局放最新数据
    public List<OutPartial> queryAllOutPartialData();
}

package com.xtw.bridge.service;

import com.xtw.bridge.mapper.OutPartialDao;
import com.xtw.bridge.model.OutPartial;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/7/28
 * Description: No Description
 */
@Service
public class OutPartialService implements OutPartialDao{
    @Resource
    OutPartialDao outPartialDao;

    // 根据分区ID查询该分区所有外置局放设备的数据
    @Override
    public List<HashMap<String,String>> queryOutPartialMaxValue(int partitionId){
        return outPartialDao.queryOutPartialMaxValue(partitionId);
    }

    @Override
    public List<HashMap<String,String>> queryOutPartitionTrend(int partitionId, String terminalId, String begintime, String endtime) {
        // 根据分区ID和设备ID查询该分区的外置局放设备的历史趋势(最大放电量、对应时间)
        return outPartialDao.queryOutPartitionTrend(partitionId, terminalId, begintime, endtime);
    }

    @Override
    public List<HashMap<String, String>> historicalAnalysis(int partitionId, String terminalId, String begintime, String endtime) {
        // 根据分区ID和设备ID查询该分区的外置局放历史分析(最大放电量、最大放电频次、时间)
        return outPartialDao.historicalAnalysis(partitionId, terminalId, begintime, endtime);
    }

    // 根据外置局放设备id查询该设备最新数据
    @Override
    public List<OutPartial> queryOutPartialData(String terminalId) {
        return outPartialDao.queryOutPartialData(terminalId);
    }
}

package com.xtw.bridge.service;

import com.xtw.bridge.mapper.OutPartialDao;
import com.xtw.bridge.model.OutPartial;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/7/28
 * Description: No Description
 */
@Service
public class OutPartialService{

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    OutPartialDao outPartialDao;

    // 根据分区ID查询该分区所有外置局放设备的数据
    public List<HashMap<String,String>> queryOutPartialMaxValue(int partitionId){
        return outPartialDao.queryOutPartialMaxValue(partitionId);
    }

    public List<HashMap<String,String>> queryOutPartitionTrend(int partitionId, String terminalId, String begintime, String endtime) {
        // 根据分区ID和设备ID查询该分区的外置局放设备的历史趋势(最大放电量、对应时间)
        return outPartialDao.queryOutPartitionTrend(partitionId, terminalId, begintime, endtime);
    }

    public List<HashMap<String, String>> historicalAnalysis(int partitionId, String terminalId, String begintime, String endtime) {
        // 根据分区ID和设备ID查询该分区的外置局放历史分析(最大放电量、最大放电频次、时间)
        return outPartialDao.historicalAnalysis(partitionId, terminalId, begintime, endtime);
    }

    // 根据外置局放设备id查询该设备最新数据
    public List<OutPartial> queryOutPartialData(String terminalId) {
        return outPartialDao.queryOutPartialData(terminalId);
    }

    // 获取所有外置局放最新数据
    public ArrayList<HashMap<String,String>> queryAllOutPartialData() {
        List<OutPartial> outPartialList = outPartialDao.queryAllOutPartialData();
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        for(int i=0;i<outPartialList.size();i++){
            HashMap<String,String> map = new HashMap<>();
            map.put("partitionID", outPartialList.get(i).getPartitionId());
            map.put("terminalID", outPartialList.get(i).getTerminalId());
            map.put("a_max_electric", String.valueOf(outPartialList.get(i).getAMaxElectric()));
            map.put("a_max_frequency", String.valueOf(outPartialList.get(i).getAMaxFrequency()));
            map.put("b_max_electric", String.valueOf(outPartialList.get(i).getBMaxElectric()));
            map.put("b_max_frequency", String.valueOf(outPartialList.get(i).getBMaxFrequency()));
            map.put("c_max_electric", String.valueOf(outPartialList.get(i).getCMaxElectric()));
            map.put("c_max_frequency", String.valueOf(outPartialList.get(i).getCMaxFrequency()));
            map.put("time", formatter.format(outPartialList.get(i).getCreateTime()));

            list.add(map);
        }
        return list;
    }
}

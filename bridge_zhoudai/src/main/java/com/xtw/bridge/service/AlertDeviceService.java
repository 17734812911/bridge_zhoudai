package com.xtw.bridge.service;

import com.xtw.bridge.mapper.AlertDeviceDao;
import com.xtw.bridge.model.AlertDO;
import com.xtw.bridge.model.AlertDevice;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;

/**
 * User: Mr.Chen
 * Date: 2021/6/28
 * Description: No Description
 */
@Service
public class AlertDeviceService {

    @Resource
    AlertDeviceDao alertDeviceDao;

    // 查询报警设备信息及报警信息
    public List<AlertDevice> queryAllAlertDevice(){
        return alertDeviceDao.queryAllAlertDevice();
    }

    // 查询七天中每天的告警次数
    public List<AlertDO> queryEveryDayAlarmNumber(){
        return alertDeviceDao.queryEveryDayAlarmNumber();
    }

    // 告警查询（有条件）
    public List<AlertDevice> queryAlertDeviceByCriteria(Integer id, String lineName, String deviceName, String joint, Date beginTime, Date endTime) {
        return alertDeviceDao.queryAlertDeviceByCriteria(id, lineName, deviceName, joint, beginTime, endTime);
    }

    // 报警确认
    public int alarmEnter(Integer id){
        return alertDeviceDao.alarmEnter(id);
    }

    // 统计指定时长（天）的报警次数
    public List<HashMap<String,String>> alertCount(Integer time){
        return alertDeviceDao.alertCount(time);
    }

    // 获取所有报警设备所属分区
    public ArrayList<Integer> alertPartition(){
        ArrayList<Integer> arrayList = new ArrayList<>();
        List<Integer> alertPartitionList = alertDeviceDao.alertPartition();
        for (Integer str : alertPartitionList) {
            arrayList.add(str - 1);     // 查询回来的数值比分区号大1（分区号从0开始的）
        }

        return arrayList;
    }

}

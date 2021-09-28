package com.xtw.bridge.service;

import com.xtw.bridge.mapper.AlertDeviceDao;
import com.xtw.bridge.model.AlertDO;
import com.xtw.bridge.model.AlertDevice;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: Mr.Chen
 * Date: 2021/6/28
 * Description: No Description
 */
@Service
public class AlertDeviceService {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
    public List<AlertDevice> queryAlertDeviceByCriteria(Integer id, String lineName, String deviceName, String beginTime, String endTime, String alertType, String isEnter) {
        return alertDeviceDao.queryAlertDeviceByCriteria(id, lineName, deviceName, beginTime, endTime, alertType, isEnter);
    }

    // 报警确认
    public int alarmEnter(String id){
        return alertDeviceDao.alarmEnter(id, sdf.format(new Date()));
    }

    // 统计指定时长（天）的报警次数
    public List<HashMap<String,String>> alertCount(Integer time){
        return alertDeviceDao.alertCount(time);
    }

    // 获取所有报警设备所属分区(用于页面闪烁)
    public ArrayList<Integer> alertPartition(){
        ArrayList<Integer> arrayList = new ArrayList<>();
        List<Integer> alertPartitionList = alertDeviceDao.alertPartition();
        for (Integer str : alertPartitionList) {
            arrayList.add(str);
        }

        return arrayList;
    }

    // 按条件查询告警设备
    public List<AlertDevice> getTodayAlarm(String beginTime, String endTime, String alertType, String deviceName, String isConfirm){
        return alertDeviceDao.getTodayAlarm(beginTime, endTime, alertType, deviceName, isConfirm);
    }

}

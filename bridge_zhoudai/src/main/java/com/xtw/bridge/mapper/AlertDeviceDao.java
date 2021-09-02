package com.xtw.bridge.mapper;

import com.xtw.bridge.model.AlertDO;
import com.xtw.bridge.model.AlertDevice;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/6/28
 * Description: No Description
 */
public interface AlertDeviceDao {

    // 查询报警设备信息及报警信息
    public List<AlertDevice> queryAllAlertDevice();

    // 查询七天中每天的告警次数
    public List<AlertDO> queryEveryDayAlarmNumber();

    // 告警查询（有条件）
    public List<AlertDevice> queryAlertDeviceByCriteria(Integer id, String lineName, String deviceName, String joint, Date beginTime, Date endTime);

    // 告警确认
    public int alarmEnter(Integer id);

    // 统计指定时长（天）的报警次数
    public List<HashMap<String,String>> alertCount(Integer time);

    // 获取所有报警设备所属分区
    public List<Integer> alertPartition();
}

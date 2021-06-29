package com.xtw.bridge.mapper;

import com.xtw.bridge.model.AlertDevice;

import java.util.Date;
import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/6/28
 * Description: No Description
 */
public interface AlertDeviceDao {

    // 查询所有报警设备
    public List<AlertDevice> queryAllAlertDevice();

    // 告警查询（有条件）
    public List<AlertDevice> queryAlertDeviceByCriteria(String lineName, String deviceName, String joint, Date beginTime, Date endTime);
}

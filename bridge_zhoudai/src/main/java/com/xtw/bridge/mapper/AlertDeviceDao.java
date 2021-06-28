package com.xtw.bridge.mapper;

import com.xtw.bridge.model.AlertDevice;

import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/6/28
 * Description: No Description
 */
public interface AlertDeviceDao {

    // 查询所有报警设备
    public List<AlertDevice> queryAllAlertDevice();
}

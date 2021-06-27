package com.xtw.bridge.mapper;

import com.xtw.bridge.model.Device;

import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/6/25
 * Description: No Description
 */
public interface DeviceDao {

    // 查询所有设备
    public List<Device> queryAllDevice();
}

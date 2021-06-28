package com.xtw.bridge.mapper;

import com.xtw.bridge.model.Device;
import com.xtw.bridge.model.Line;

import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/6/25
 * Description: No Description
 */
public interface DeviceDao {

    // 查询所有设备
    public List<Device> queryAllDevice();

    // 查询设备在线情况
    public List<Line> queryOnlineDevice();

    // 查询所有类型设备近7天最大值
    public List<Device> queryDeviceMaxValue();
}

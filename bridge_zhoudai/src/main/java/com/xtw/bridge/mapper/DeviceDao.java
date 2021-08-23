package com.xtw.bridge.mapper;

import com.xtw.bridge.model.Device;
import com.xtw.bridge.model.DeviceDO;
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

    // 查询所有种类设备对应的数量
    public List<DeviceDO> queryAllTypeNumber();

    // 查询不在线设备种类对应的数量
    public List<DeviceDO> queryOutlineDevice();

    // 查询所有线路
    public List<Line> queryAllLine();
}

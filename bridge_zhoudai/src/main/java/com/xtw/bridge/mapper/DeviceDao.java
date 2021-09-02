package com.xtw.bridge.mapper;

import com.xtw.bridge.model.Device;
import com.xtw.bridge.model.DeviceDO;
import com.xtw.bridge.model.Line;
import com.xtw.bridge.model.MaxValue;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/6/25
 * Description: No Description
 */
public interface DeviceDao {

    // 查询所有设备
    public List<Device> queryAllDevice();

    // 按分类查询设备
    public List<LinkedHashMap<String,String>> queryDeviceByCategory();

    // 根据LineId查询lineName
    public String queryLineName(String lineId);

    // 查询所有种类设备对应的数量
    public List<DeviceDO> queryAllTypeNumber();

    // 查询不在线设备种类对应的数量
    public List<DeviceDO> queryOutlineDevice();

    // 查询所有线路
    public List<Line> queryAllLine();

    // 查询所有分类七天最大值
    public MaxValue queryMaxValue();
}

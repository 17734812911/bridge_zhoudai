package com.xtw.bridge.mapper;

import com.xtw.bridge.model.EnvironmentDO;
import com.xtw.bridge.model.EnvironmentDevice;
import com.xtw.bridge.model.page.PageRequest;
import com.xtw.bridge.model.page.PageResult;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/7/29
 * Description: 环境量
 */
public interface EnvironmentDao {

    // 根据分区ID查询环境量数据
    public List<EnvironmentDevice> queryAllDataByPartitionId(int partitionId);

    // 根据分区ID查询所有设备在使用的通道
    public List<EnvironmentDO> queryUseChannels(Integer partitionId);

    // 根据环境量和表皮测温设备id查询该设备最新数据
    public List<EnvironmentDevice> queryDatasByTerminalId(String terminalId);

    // 根据环境量和表皮测温设备id查询该设备在使用的通道

    public List<EnvironmentDO> queryUseChannelsByTerminalId(String terminalId);

    // 根据环境量和表皮测温设备id查询该设备所有数据
    public List<EnvironmentDevice> queryEnvironmentDatasPage(String terminalId, String beginTime, String endTime);

    //
}

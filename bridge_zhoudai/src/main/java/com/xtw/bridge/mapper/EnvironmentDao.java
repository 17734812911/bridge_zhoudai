package com.xtw.bridge.mapper;

import com.xtw.bridge.model.EnvironmentDevice;

import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/7/29
 * Description: 环境量
 */
public interface EnvironmentDao {

    // 根据分区ID查询环境量数据
    public List<EnvironmentDevice> queryAllDataByPartitionId(int partitionId);
}

package com.xtw.bridge.mapper;

import com.xtw.bridge.model.OutPartial;

import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/7/28
 * Description: No Description
 */
public interface OutPartialDao {

    // 根据分区ID查询该分区所有外置局放设备的数据
    public List<OutPartial> queryOutPartialMaxValue(int partitionId);
}

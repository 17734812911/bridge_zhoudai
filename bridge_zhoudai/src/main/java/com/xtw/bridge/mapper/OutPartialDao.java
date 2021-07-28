package com.xtw.bridge.mapper;

import com.xtw.bridge.model.OutPartial;

import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/7/28
 * Description: No Description
 */
public interface OutPartialDao {

    // 查询所有设备的最大放电量和最大放电频次
    public List<OutPartial> queryOutPartialMaxValue();
}

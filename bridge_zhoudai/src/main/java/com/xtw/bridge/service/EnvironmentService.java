package com.xtw.bridge.service;

import com.xtw.bridge.model.page.PageRequest;
import com.xtw.bridge.model.page.PageResult;

import java.util.Date;

/**
 * User: Mr.Chen
 * Date: 2021/8/18
 * Description: No Description
 */
public interface EnvironmentService {
    public PageResult queryEnvironmentDatasPage(PageRequest pageRequest, String terminalId, String beginTime, String endTime);
}

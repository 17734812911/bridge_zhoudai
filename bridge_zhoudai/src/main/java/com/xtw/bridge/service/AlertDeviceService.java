package com.xtw.bridge.service;

import com.xtw.bridge.mapper.AlertDeviceDao;
import com.xtw.bridge.model.AlertDevice;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/6/28
 * Description: No Description
 */
@Service
public class AlertDeviceService {

    @Resource
    AlertDeviceDao alertDeviceDao;

    // 查询报警设备
    public List<AlertDevice> queryAllAlertDevice(){
        return alertDeviceDao.queryAllAlertDevice();
    }
}

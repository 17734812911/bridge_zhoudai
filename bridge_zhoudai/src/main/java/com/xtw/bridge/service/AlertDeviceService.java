package com.xtw.bridge.service;

import com.xtw.bridge.mapper.AlertDeviceDao;
import com.xtw.bridge.model.AlertDO;
import com.xtw.bridge.model.AlertDevice;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedHashMap;
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

    // 查询报警设备信息及报警信息
    public List<AlertDevice> queryAllAlertDevice(){
        return alertDeviceDao.queryAllAlertDevice();
    }

    // 查询七天中每天的告警次数
    public List<AlertDO> queryEveryDayAlarmNumber(){
        return alertDeviceDao.queryEveryDayAlarmNumber();
    }

    // 告警查询（有条件）
    public List<AlertDevice> queryAlertDeviceByCriteria(Integer id, String lineName, String deviceName, String joint, Date beginTime, Date endTime) {
        return alertDeviceDao.queryAlertDeviceByCriteria(id, lineName, deviceName, joint, beginTime, endTime);
    }

    // 报警确认
    public int alarmEnter(Integer id){
        return alertDeviceDao.alarmEnter(id);
    }
}

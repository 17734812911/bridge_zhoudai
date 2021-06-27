package com.xtw.bridge.service;

import com.xtw.bridge.mapper.DeviceDao;
import com.xtw.bridge.model.Device;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/6/25
 * Description: No Description
 */
@Service
public class DeviceService {
    @Resource
    DeviceDao deviceDao;

    public List<Device> queryAllDevice(){
        return deviceDao.queryAllDevice();
    }
}

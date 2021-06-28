package com.xtw.bridge.service;

import com.xtw.bridge.mapper.DeviceDao;
import com.xtw.bridge.model.Device;
import com.xtw.bridge.model.Line;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
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

    public List<Line> queryOnlineDevice(){
        List<Line> deviceList = deviceDao.queryOnlineDevice();
        Date d = new Date();

        if(!deviceList.isEmpty()){
            for(int i=0;i<deviceList.size();i++){
                for(int j=0;j<deviceList.get(i).getDevice().size();j++){
                    int days = (int)(d.getTime() - deviceList.get(i).getDevice().get(j).getDataTime().getTime()) / (1000*3600*24);
                    if(days > 1){
                        deviceList.get(i).getDevice().get(j).setIsOnline(false);
                    }else{
                        deviceList.get(i).getDevice().get(j).setIsOnline(true);
                    }
                }
            }
        }
        return deviceList;
    }

    // 查询所有类型设备近7天最大值
    public List<Device> queryDeviceMaxValue(){
        return deviceDao.queryDeviceMaxValue();
    }
}

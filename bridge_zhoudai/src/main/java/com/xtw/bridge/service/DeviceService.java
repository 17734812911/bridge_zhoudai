package com.xtw.bridge.service;

import com.xtw.bridge.mapper.DeviceDao;
import com.xtw.bridge.model.Device;
import com.xtw.bridge.model.DeviceDO;
import com.xtw.bridge.model.Line;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedHashMap;
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

    // 查询设备在线情况
    public LinkedHashMap<String, Integer> queryOnlineDevice(){
        LinkedHashMap<String, Integer> linkHashMap = new LinkedHashMap<>();
        // 查询所有种类设备对应的数量
        List<DeviceDO> allTypeNumberList = deviceDao.queryAllTypeNumber();
        // 查询不在线设备种类对应的数量
        List<DeviceDO> deviceList = deviceDao.queryOutlineDevice();
        // 解析设备在线/不在线情况
        for(int i=0;i<allTypeNumberList.size();i++){
            for(int j=0;j<deviceList.size();j++){
                // 获取对应设备
                if(allTypeNumberList.get(i).getName().equals(deviceList.get(j).getName())){
                    // 在线数量
                    int onLineNumber = allTypeNumberList.get(i).getNumber() - deviceList.get(j).getNumber();
                    switch(allTypeNumberList.get(i).getName()){
                        case "外置局放":
                            linkHashMap.put("WZJFOnLine", onLineNumber);
                            linkHashMap.put("WZJFOffLine", deviceList.get(j).getNumber());
                            break;
                        case "环境量":
                            linkHashMap.put("HJLOnLine", onLineNumber);
                            linkHashMap.put("HJLOffLine", deviceList.get(j).getNumber());
                            break;
                        case "光纤测温":
                            linkHashMap.put("GXCWOnLine", onLineNumber);
                            linkHashMap.put("GXCWOffLine", deviceList.get(j).getNumber());
                            break;
                    }
                }
            }
        }
        return linkHashMap;
    }

    // 查询所有线路
    public List<Line> queryAllLine(){
        return deviceDao.queryAllLine();
    }
}

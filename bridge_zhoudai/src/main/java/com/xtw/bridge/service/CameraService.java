package com.xtw.bridge.service;

import com.xtw.bridge.mapper.CameraDao;
import com.xtw.bridge.model.Camera;
import com.xtw.bridge.model.CameraAlert;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/8/24
 * Description: No Description
 */
@Service
public class CameraService {

    @Resource
    CameraDao cameraDao;

    // 根据设备ID查询该摄像头的配置
    public Camera getCameraConfig(String terminalId){
       return cameraDao.getCameraConfig(terminalId);
    }

    // 根据分区ID查询该分区下的所有摄像头
    public List<Camera> getAllCamera(String partitionId){
        return cameraDao.getAllCamera(partitionId);
    }

    // 根据摄像头编码查询deviceId
    public Camera queryTerminalIdByCode(String code){
        return cameraDao.queryTerminalIdByCode(code);
    }

    // 向告警表插入数据
    public int saveAlarmData(CameraAlert cameraAlert){
        return cameraDao.saveAlarmData(cameraAlert);
    }

    // 更新设备表的最后数据的时间
    public int updateDeviceDate(String terminalId, String lastDataTime){
        return cameraDao.updateDeviceDate(terminalId, lastDataTime);
    }
}

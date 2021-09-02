package com.xtw.bridge.mapper;

import com.xtw.bridge.model.Camera;
import com.xtw.bridge.model.CameraAlert;

import java.util.Date;
import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/8/24
 * Description: No Description
 */
public interface CameraDao {
    // 根据设备ID查询该摄像头的配置
    public Camera getCameraConfig(String terminalId);

    // 根据分区ID查询该分区下的所有摄像头
    public List<Camera> getAllCamera(String partitionId);

    // 根据摄像头编码查询deviceId
    public Camera queryTerminalIdByCode(String code);

    // 向告警表插入数据
    public int saveAlarmData(CameraAlert cameraAlert);

    // 更新设备表的最后数据的时间
    public int updateDeviceDate(String terminalId, String lastDataTime);
}

package com.xtw.bridge.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * User: Mr.Chen
 * Date: 2021/8/23
 * Description: 摄像头配置实体类
 */
@Data
@Schema(name = "Camera", description = "摄像头配置实体类")
public class Camera {
    private String cameraId;    // 摄像头ID
    private String terminalId;  // 设备ID
    private String partitionId;     // 分区ID
    private String deviceIp;        // 摄像头IP
    private String cameraOne;       // 一号通道(正常画面)
    private String cameraTwo;       // 二号通道(热成像画面)
    private String name;            // 摄像头类型名
    private String typeName;        // 摄像头画面类型名称
    private double alarmValue;      // 告警值
    private double offsetValue;     // 偏移量

    private String deviceName;      // 设备名称
    private String produceName;     // 产品名称
    private String lineName;        // 线路名称
    private String joint;           // 接头名称

}

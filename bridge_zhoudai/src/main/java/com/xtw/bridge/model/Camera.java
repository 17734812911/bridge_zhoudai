package com.xtw.bridge.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * User: Mr.Chen
 * Date: 2021/8/23
 * Description: No Description
 */
@Data
@Schema(name = "Camera", description = "摄像头配置实体类")
public class Camera {
    private String cameraId;    // 摄像头ID
    private String terminalId;  // 设备ID
    private String partitionId;     // 分区ID
}

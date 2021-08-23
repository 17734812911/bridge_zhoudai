package com.xtw.bridge.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * User: Mr.Chen
 * Date: 2021/6/25
 * Description: 设备实体类
 */
@Schema(name = "Device", description = "设备实体类")
@Data
public class Device {
    @Schema(name = "id", description = "id")
    private Integer id;
    @Schema(name = "name", description = "产品名称")
    private String name;
    @Schema(name = "deviceName", description = "设备名称")
    private String deviceName;
    @Schema(name = "deviceTypeId", description = "设备类型id")
    private Integer deviceTypeId;
    @Schema(name = "terminalId", description = "设备ID")
    private String terminalId;
    @Schema(name = "lineId", description = "线路ID")
    private Integer lineId;
    @Schema(name = "joint", description = "接头")
    private String joint;
    @Schema(name = "environmentDevice", description = "设备数据实体类")
    private EnvironmentDevice environmentDevice;
    @Schema(name = "isOnline", description = "是否在线")
    private Boolean isOnline;

}

package com.xtw.bridge.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * User: Mr.Chen
 * Date: 2021/8/10
 * Description: 外置局放配置实体类
 */
@Data
@Schema(name = "OutPartialConfig", description = "外置局放设备的配置实体类")
public class OutPartialConfig {
    @Schema(name = "name", description = "产品名称")
    private String name;
    @Schema(name = "deviceName", description = "设备名称")
    private String deviceName;
    @Schema(name = "communicationMode", description = "通信方式")
    private String communicationMode;
    @Schema(name = "terminalId", description = "设备ID")
    private String terminalId;
    @Schema(name = "joint", description = "接头名称")
    private String joint;
    @Schema(name = "lineName", description = "线路名称")
    private String lineName;
    @Schema(name = "partitionId", description = "分区ID")
    private String partitionId;
    @Schema(name = "alarmValue", description = "报警值")
    private Double alarmValue;
    @Schema(name = "alarmFrequencyValue", description = "放电频次报警值")
    private Integer alarmFrequencyValue;
    @Schema(name = "offsetValue", description = "偏移量")
    private Double offsetValue;
    @Schema(name = "deviceIp", description = "设备IP")
    private String deviceIp;
    @Schema(name = "port", description = "监听端口")
    private Integer port;
    @Schema(name = "intervalSlave", description = "设备间访问间隔时间（毫秒）")
    private Integer intervalSlave;
    @Schema(name = "retryCount", description = "失败重试次数")
    private Integer retryCount;
    @Schema(name = "retryInterval", description = "失败后重发次数中的时间间隔（毫秒）")
    private Integer retryInterval;
    @Schema(name = "pollingInterval", description = "多久采集一遍数据（毫秒）")
    private Integer pollingInterval;

}

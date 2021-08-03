package com.xtw.bridge.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * User: Mr.Chen
 * Date: 2021/7/21
 * Description: No Description
 */
@Data
@Schema(name = "FibreTemperatureConfig", description = "光纤测温配置类")
public class FibreTemperatureConfig {
    @Schema(name = "id", description = "主键ID")
    private Integer id;
    @Schema(name = "channel", description = "通道ID")
    private String channel;
    @Schema(name = "partitionId", description = "分区ID")
    private Integer partitionId;
    @Schema(name = "startPosition", description = "开始点位")
    private Integer startPosition;
    @Schema(name = "endPosition", description = "结束点位")
    private Integer endPosition;
    @Schema(name = "deviceIp", description = "设备IP")
    private String deviceIp;
    @Schema(name = "offsetValue", description = "偏移量")
    private Double offsetValue;
    @Schema(name = "alarmValue", description = "报警阈值")
    private Double alarmValue;
    @Schema(name = "readOrder", description = "读取顺序")
    private Boolean readOrder;
}

package com.xtw.bridge.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * User: Mr.Chen
 * Date: 2021/7/19
 * Description: 光纤测温实体类
 */
@Data
@Schema(name = "FibreTemperature", description = "光纤测温实体类")
public class FibreTemperature {
    @Schema(name = "id", description = "主键ID")
    private Integer id;
    @Schema(name = "deviceIp", description = "设备IP")
    private String deviceIp;
    @Schema(name = "channel", description = "通道ID")
    private String channel;
    @Schema(name = "partitionId", description = "分区ID")
    private Integer partitionId;
    @Schema(name = "createTime", description = "数据采集时间")
    private String createTime;
    @Schema(name = "step", description = "量程")
    private String step;
    @Schema(name = "datas", description = "光纤测温数据")
    private String datas;
    @Schema(name = "maxValue", description = "最大值")
    private BigDecimal maxValue;
    @Schema(name = "maxValuePoints", description = "最大值所在点位")
    private Integer maxValuePoints;
    @Schema(name = "offsetValue", description = "偏移量")
    private double offsetValue;

}

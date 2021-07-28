package com.xtw.bridge.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * User: Mr.Chen
 * Date: 2021/7/27
 * Description: No Description
 */
@Schema(name = "FibreTemperatureAlert", description = "光纤测温报警实体类")
@Data
public class FibreTemperatureAlert {
    @Schema(name = "id", description = "id")
    private Integer id;
    @Schema(name = "content", description = "报警内容")
    private String content;         // 报警内容
    @Schema(name = "alertData", description = "报警值")
    private String alertData;   // 报警值
    @Schema(name = "alertDate", description = "报警时间")
    private Date alertDate;     // 报警时间
    @Schema(name = "channel", description = "通道ID")
    private String channel;         // 通道ID
    @Schema(name = "partitionId", description = "分区号")
    private String partitionId;      // 设备
    @Schema(name = "isConfirm", description = "报警是否已确认")
    private Boolean isConfirm;      // 是否确认
}

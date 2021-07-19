package com.xtw.bridge.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.Date;

/**
 * User: Mr.Chen
 * Date: 2021/6/28
 * Description: 设备报警实体类
 */
@Schema(name = "AlertDevice", description = "设备报警实体类")
@Data
public class AlertDevice {
    @Schema(name = "id", description = "id")
    private Integer id;
    @Schema(name = "content", description = "报警内容")
    private String content;         // 报警内容
    @Schema(name = "alertData", description = "报警值")
    private String alertData;   // 报警值
    @Schema(name = "alert", description = "报警时间")
    private Date alertDate;     // 报警时间
    @Schema(name = "line", description = "线路实体类")
    private Line line;         // 线路
    @Schema(name = "device", description = "设备实体类")
    private Device device;      // 设备
    @Schema(name = "isConfirm", description = "报警是否已确认")
    private Boolean isConfirm;      // 是否确认
}
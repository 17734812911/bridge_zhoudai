package com.xtw.bridge.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * User: Mr.Chen
 * Date: 2021/8/31
 * Description: 摄像头告警实体类
 */
@Data
public class CameraAlert {
    @Schema(name = "lineId", description = "分区ID")
    private String lineId;
    @Schema(name = "terminalId", description = "设备ID")
    private String terminalId;
    @Schema(name = "content", description = "报警内容")
    private String content;
    @Schema(name = "alertData", description = "报警值")
    private String alertData;
    @Schema(name = "alertDate", description = "报警时间")
    private Date alertDate;
    @Schema(name = "isConfirm", description = "报警是否已确认")
    private Boolean isConfirm;
}

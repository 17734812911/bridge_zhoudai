package com.xtw.bridge.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/6/28
 * Description: 设备报警实体类
 */
@Data
public class AlertDevice {
    private Integer id;
    private Integer lineId;
    private String terminalId;
    private String content;         // 报警内容
    private String alertData;   // 报警值
    private Date alertDate;     // 报警时间
    // private Line line;         // 线路
    // private Device device;      // 设备
    private Boolean isConfirm;      // 是否确认
}
package com.xtw.bridge.model;

import lombok.Data;

import java.util.Date;

/**
 * User: Mr.Chen
 * Date: 2021/6/25
 * Description: 设备实体类
 */
@Data
public class Device {
    private Integer id;
    private String name;
    private String terminalId;
    private Integer lineId;
    private String joint;
    private Date creatTime;
    private Date alarmTime;
    private String deviceData;
    private Date dataTime;
    private Boolean isOnline;

}

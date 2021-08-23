package com.xtw.bridge.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * User: Mr.Chen
 * Date: 2021/8/11
 * Description: No Description
 */
@Data
public class EnvironmentConfig {
    private String name;
    private String deviceName;
    private String terminalId;
    private String lineName;
    private String joint;
    private String deviceIp;
    private String port;
    private Integer partitionId;
    private Integer intervalSlave;  // 设备间访问间隔时间（毫秒）
    private Integer retryCount;     // 失败重试次数
    private Integer retryInterval;  // 失败后重发次数中的时间间隔（毫秒）
    private Integer pollingInterval;    // 多久采集一遍数据（毫秒）
    private String channnelName;        // 通道名称
    private Integer channelId;
    private String channelType;
    private Integer rangeUp;
    private Integer rangeLow;
    private String alarmUp;
    private Double offsetValue;
    private boolean use;


}

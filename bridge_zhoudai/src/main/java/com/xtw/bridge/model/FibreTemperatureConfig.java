package com.xtw.bridge.model;

import lombok.Data;

/**
 * User: Mr.Chen
 * Date: 2021/7/21
 * Description: No Description
 */
@Data
public class FibreTemperatureConfig {
    private Integer id;
    private String channel;
    private Integer partitionId;
    private Integer startPosition;
    private Integer endPosition;
    private String deviceIp;
    private Double offsetValue;
    private Double alarmValue;
}

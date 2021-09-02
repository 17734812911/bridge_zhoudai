package com.xtw.bridge.model;

import lombok.Data;

import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/8/31
 * Description: No Description
 */
@Data
public class Thermometry {
    private TargetAttrs targetAttrs;
    private int presetNo;
    private int alarmLevel;
    private int alarmType;
    private int alarmRule;
    private int ruleCalibType;
    private String ruleTemperature;
    private String curTemperature;
    private PtzInfo ptzInfo;
    private String imageUrl;
    private String visiblePicUrl;
    private int thermometryUnit;
    private String toleranceTemperature;
    private int alertFilterTime;
    private int alarmFilterTime;
    private int ruleId;
    private List<Point> point;
    private HighesPoint highesPoint;
}

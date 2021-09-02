package com.xtw.bridge.model;

import lombok.Data;

/**
 * User: Mr.Chen
 * Date: 2021/8/31
 * Description: No Description
 */
@Data
public class TargetAttrs {
    private String imageServerCode;
    private String deviceIndexCode;
    private String cameraIndexCode;
    private String cameraAddress;
    private double longitude;
    private double latitude;
}

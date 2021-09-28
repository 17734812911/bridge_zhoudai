package com.xtw.bridge.model;

import lombok.Data;

@Data
public class TargetAttrs {
    private String imageServerCode;
    private String deviceIndexCode;
    private String cameraIndexCode;
    private String cameraAddress;
    private double longitude;
    private double latitude;
}

package com.xtw.bridge.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 *  摄像头温度实体类
 */
@Data
public class CameraTemperatureModel {
    private String dataType;
    private Date recvTime;
    private Date sendTime;
    private Date dateTime;
    private String ipAddress;
    private int portNo;
    private int channelID;
    private String eventType;
    private String eventDescription;
    private List<Thermometry> thermometry;
}

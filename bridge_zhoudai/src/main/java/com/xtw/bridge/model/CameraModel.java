package com.xtw.bridge.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/8/31
 * Description: No Description
 */
@Data
public class CameraModel {
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

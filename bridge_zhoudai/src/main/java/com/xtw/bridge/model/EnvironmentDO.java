package com.xtw.bridge.model;

import lombok.Data;

/**
 * User: Mr.Chen
 * Date: 2021/8/9
 * Description: No Description
 */
@Data
public class EnvironmentDO {
    private String terminalId;
    private String channelId;
    private String name;    // 环境量通道名称
    private String channelType;     // 通道类型
}

package com.xtw.bridge.model;

import lombok.Data;

import java.util.Date;

/**
 * User: Mr.Chen
 * Date: 2021/7/19
 * Description: 光纤测温实体类
 */
@Data
public class FibreTemperature {
    private Integer id;
    private String deviceIp;
    private String Channel;
    private Date createTime;
    private String step;
    private String datas;

}

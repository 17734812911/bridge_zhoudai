package com.xtw.bridge.model;

import lombok.Data;

import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/6/25
 * Description: 线路实体类
 */
@Data
public class Line {
    private Integer id;
    private String name;
    private List<Device> device;  // 设备实体类
}

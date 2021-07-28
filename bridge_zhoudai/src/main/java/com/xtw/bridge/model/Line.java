package com.xtw.bridge.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/6/25
 * Description: 线路实体类
 */
@Schema(name = "Line", description = "线路实体类")
@Data
public class Line {
    @Schema(name = "id", description = "id")
    private String id;
    @Schema(name = "name", description = "线路名称")
    private String name;
    @Schema(name = "device", description = "设备实体类")
    private List<Device> device;  // 设备实体类
}

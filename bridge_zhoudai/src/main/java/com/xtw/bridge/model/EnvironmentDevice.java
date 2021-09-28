package com.xtw.bridge.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * User: Mr.Chen
 * Date: 2021/7/13
 * Description: No Description
 */
@Schema(name = "Device", description = "设备数据实体类")
@Data
public class EnvironmentDevice {
    @Schema(name = "ID", description = "ID")
    private Integer id;
    @Schema(name = "terminalId", description = "设备ID")
    private String terminalId;
    @Schema(name = "terminalIp", description = "设备IP")
    private String terminalIp;
    @Schema(name = "passageOne", description = "1号通道值")
    private String passageOne;
    @Schema(name = "passageTwo", description = "2号通道值")
    private String passageTwo;
    @Schema(name = "passageThree", description = "3号通道值")
    private String passageThree;
    @Schema(name = "passageFour", description = "4号通道值")
    private String passageFour;
    @Schema(name = "passageFive", description = "5号通道值")
    private String passageFive;
    @Schema(name = "passageSix", description = "6号通道值")
    private String passageSix;
    @Schema(name = "passageSeven", description = "7号通道值")
    private String passageSeven;
    @Schema(name = "passageEigth", description = "8号通道值")
    private String passageEigth;
    @Schema(name = "passageNine", description = "9号通道值")
    private String passageNine;
    @Schema(name = "passageTen", description = "10号通道值")
    private String passageTen;
    @Schema(name = "insertTime", description = "最新数据时间")
    private String insertTime;

}

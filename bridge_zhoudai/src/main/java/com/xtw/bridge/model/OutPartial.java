package com.xtw.bridge.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * User: Mr.Chen
 * Date: 2021/7/28
 * Description: 外置局放实体类
 */
@Schema(name = "OutPartial", description = "外置局放实体类")
@Data
public class OutPartial {
    @Schema(name = "id", description = "外置局放ID")
    private Integer id;
    @Schema(name = "terminalId", description = "设备ID")
    private String terminalId;
    @Schema(name = "aElectric", description = "A相放电波形")
    private String aElectric;
    @Schema(name = "aFrequency", description = "A相放电频次")
    private String aFrequency;
    @Schema(name = "aMaxElectric", description = "A相最大放电量")
    private Integer aMaxElectric;
    @Schema(name = "aMaxFrequency", description = "A相最大放电频次")
    private Integer aMaxFrequency;
    @Schema(name = "bElectric", description = "B相放电波形")
    private String bElectric;
    @Schema(name = "bFrequency", description = "B相放电频次")
    private String bFrequency;
    @Schema(name = "bMaxElectric", description = "B相最大放电量")
    private Integer bMaxElectric;
    @Schema(name = "bMaxFrequency", description = "A相最大放电频次")
    private Integer bMaxFrequency;
    @Schema(name = "cElectric", description = "C相放电波形")
    private String cElectric;
    @Schema(name = "cFrequency", description = "C相放电频次")
    private String cFrequency;
    @Schema(name = "cMaxElectric", description = "C相最大放电量")
    private Integer cMaxElectric;
    @Schema(name = "cMaxFrequency", description = "A相最大放电频次")
    private Integer cMaxFrequency;
    @Schema(name = "createTime", description = "时间")
    private Date createTime;
    @Schema(name = "aStatus", description = "A相状态")
    private Integer aStatus;
    @Schema(name = "bStatus", description = "B相状态")
    private Integer bStatus;
    @Schema(name = "cStatus", description = "C相状态")
    private Integer cStatus;
}

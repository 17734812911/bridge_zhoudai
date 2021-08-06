package com.xtw.bridge.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * User: Mr.Chen
 * Date: 2021/8/6
 * Description: No Description
 */
@Data
@Schema(name = "Role", description = "角色实体类")
public class Role {
    @Schema(name = "id", description = "主键ID")
    private Integer id;
    @Schema(name = "roleName", description = "角色名称")
    private String roleName;
    @Schema(name = "roleCode", description = "角色编码")
    private String roleCode;
    @Schema(name = "roleDesc", description = "角色信息描述")
    private String roleDesc;
}

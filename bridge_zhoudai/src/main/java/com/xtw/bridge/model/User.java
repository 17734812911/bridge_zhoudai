package com.xtw.bridge.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * User: Mr.Chen
 * Date: 2021/8/5
 * Description: No Description
 */
@Data
@Schema(name = "User", description = "用户实体类")
public class User {
    @Schema(name = "id", description = "主键ID")
    private Integer id;
    @Schema(name = "username", description = "用户名")
    private String username;
    @Schema(name = "password", description = "密码")
    private String password;
    @Schema(name = "cleartextPassword", description = "明文")
    private String cleartextPassword;
    @Schema(name = "createTime", description = "用户创建时间")
    private Date createTime;
    @Schema(name = "role", description = "角色实体类")
    private Role role;

    public User() {
    }

    public User(Integer id) {
        this.id = id;
    }

    // public User(Integer id, String username, String password, String cleartextPassword, Date createTime, Role role) {
    //     this.id = id;
    //     this.username = username;
    //     this.password = password;
    //     this.cleartextPassword = cleartextPassword;
    //     this.createTime = createTime;
    //     this.role = role;
    // }
    //
    public User(String username, String password, String cleartextPassword, Date createTime) {
        this.username = username;
        this.password = password;
        this.cleartextPassword = cleartextPassword;
        this.createTime = createTime;
    }

}

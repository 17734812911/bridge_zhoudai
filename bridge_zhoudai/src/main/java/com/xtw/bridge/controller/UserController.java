package com.xtw.bridge.controller;

import com.xtw.bridge.configs.SecurityConfig;
import com.xtw.bridge.model.User;
import com.xtw.bridge.myexception.CustomException;
import com.xtw.bridge.myexception.CustomExceptionType;
import com.xtw.bridge.myexception.ResponseFormat;
import com.xtw.bridge.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/8/5
 * Description: No Description
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Resource
    SecurityConfig securityConfig;
    @Resource
    UserService userService;

    // 添加用户
    @PostMapping("user")
    @Operation(
            summary = "添加用户",
            parameters = {
                    @Parameter(name = "username", description = "用户名"),
                    @Parameter(name = "password", description = "密码")
            }
    )
    public ResponseFormat addUser(@RequestBody HashMap<String,String> map) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(map.containsKey("username") && map.get("username") != "" & map.containsKey("password") && map.get("password") != ""){
            String userName = map.get("username");
            String passWord = map.get("password");
            // 构建用户实体类
            User user = new User(userName, securityConfig.passwordEncoder().encode(passWord), passWord, sdf.parse(sdf.format(new Date())));
            int result = userService.addUser(user);
            if(result > 0){
                return ResponseFormat.success("添加成功");
            } else if(result == -1){
                return ResponseFormat.error(new CustomException(CustomExceptionType.ALREADY_REGISTERED, "用户名已经被注册"));
            } else{
                return ResponseFormat.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "用户添加失败"));
            }
        }else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "请输入用户名密码"));
        }
    }

    @PutMapping("/user")
    @Operation(
            summary = "修改用户角色",
            parameters = {
                    @Parameter(name = "username", description = "用户名"),
                    @Parameter(name = "roleid", description = "角色ID")
            }
    )
    public ResponseFormat editUserRole(@RequestBody HashMap<String,String> map){
        if(map.containsKey("username") && map.get("username") != "" & map.containsKey("roleid") && map.get("roleid") != ""){
            String username = map.get("username");
            int roleId = Integer.parseInt(map.get("roleid"));
            int result = userService.editRole(username, roleId);
            if(result > 0){
                return ResponseFormat.success("修改角色成功");
            } else{
                return ResponseFormat.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "用户角色修改失败"));
            }
        }else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "请输入正确的参数"));
        }

    }


    // 根据用户名删除用户
    @DeleteMapping("/user")
    @Operation(
            summary = "根据用户名删除用户",
            parameters = {
                    @Parameter(name = "username", description = "用户名")
            }
    )
    public ResponseFormat delUser(String username){
        int result = userService.delUser(username);
        if(result > 0){
            return ResponseFormat.success("删除用户成功");
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "删除用户失败"));
        }
    }


    // 根据用户名查询用户信息
    @GetMapping("/user")
    @Operation(
            summary = "根据用户名查询用户信息",
            parameters = {
                    @Parameter(name = "username", description = "用户名")
            }
    )
    public ResponseFormat queryUserByName(String username){
        User user = userService.queryUserByName(username);
        if(user != null){
            return ResponseFormat.success("查询成功", user);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "查询用户信息失败"));
        }
    }

    // 查询所有用户
    @GetMapping("/all-user")
    @Operation(
            summary = "查询所有用户信息"
    )
    public ResponseFormat queryAllUser(){
        List<User> userList = userService.queryAllUser();
        if(userList != null){
            return ResponseFormat.success("查询成功", userList);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "查询用户信息失败"));
        }
    }


    // 根据用户名查询用户密码
    @GetMapping("/userpw")
    @Operation(
            summary = "根据用户名查询用户密码",
            parameters = {
                    @Parameter(name = "username", description = "用户名")
            }
    )
    public ResponseFormat queryPasswordByUsername(String username){
        User user = userService.queryPasswordByUsername(username);
       if(user != null){
           return ResponseFormat.success("查询成功", user.getCleartextPassword());
       } else{
           return ResponseFormat.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "查询失败"));
       }
    }
}

package com.xtw.bridge.controller;

import com.xtw.bridge.myexception.CustomException;
import com.xtw.bridge.myexception.CustomExceptionType;
import com.xtw.bridge.myexception.ResponseFormat;
import com.xtw.bridge.service.authentication.JwtAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang3.StringUtils;
import javax.annotation.Resource;
import java.util.Map;

/**
 * User: Mr.Chen
 * Date: 2021/6/24
 * Description: JWT登录认证
 */
@RestController
@RequestMapping("/users")
public class JwtAuthController {

    @Resource
    JwtAuthService jwtAuthService;

    @PostMapping("/login")
    @Operation(
            summary = "登录接口",
            parameters = {
                    @Parameter(name = "username", description = "用户名"),
                    @Parameter(name = "password", description = "密码")
            }
    )
    public ResponseFormat login(@RequestBody Map<String,String> map){// @RequestParam String username, @RequestParam String password
        String username = map.get("username");
        String password = map.get("password");
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            return ResponseFormat.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR,"用户名密码不能为空"));
        }

        try{
            //用户名密码的认证
            return ResponseFormat.success("登录成功", jwtAuthService.login(username, password));
        } catch (CustomException e){
            // 服务层的实现类可能会抛出异常，在这里处理下
            return ResponseFormat.error(e);
        }
    }

    // 刷新令牌
    @PostMapping("/refreshtoken")
    @Operation(
            summary = "刷新令牌",
            parameters = {
                    @Parameter(name = "oldToken", description = "旧的Token")
            }
    )
    public ResponseFormat refresh(@RequestHeader("${jwt.header}") String oldToken) { //${jwt.header}
        return ResponseFormat.success("刷新成功", jwtAuthService.refreshToken(oldToken));
    }

}

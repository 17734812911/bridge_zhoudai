package com.xtw.bridge.controller;

import com.xtw.bridge.myexception.CustomException;
import com.xtw.bridge.myexception.CustomExceptionType;
import com.xtw.bridge.myexception.ResponseFormat;
import com.xtw.bridge.service.authentication.JwtAuthService;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang3.StringUtils;
import javax.annotation.Resource;

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
    public ResponseFormat login(@RequestParam String username, @RequestParam String password){//@RequestBody Map<String,String> map
        // String username = map.get("username");
        // String password = map.get("password");
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
    public ResponseFormat refresh(@RequestHeader("oldToken") String oldToken) { //${jwt.header}
        return ResponseFormat.success("刷新成功", jwtAuthService.refreshToken(oldToken));
    }

}

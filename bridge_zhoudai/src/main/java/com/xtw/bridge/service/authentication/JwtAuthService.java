package com.xtw.bridge.service.authentication;

import com.xtw.bridge.myexception.CustomException;
import com.xtw.bridge.myexception.CustomExceptionType;
import com.xtw.bridge.utils.JwtTokenUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * User: Mr.Chen
 * Date: 2021/6/24
 * Description: JWT登录认证服务层
 */
@Service
public class JwtAuthService {
    @Resource
    AuthenticationManager authenticationManager;
    @Resource
    UserDetailsService userDetailsService;
    @Resource
    JwtTokenUtil jwtTokenUtil;

    /**
     * 登录认证换取JWT令牌
     * @param username
     * @param password
     * @return  JWT令牌
     * @throws CustomException
     */
    public String login(String username, String password) throws CustomException {

        // 构建Token
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username,password);
        // 登录认证
        try{
            // 使用构建的token进行认证，如果成功，会返回一个认证主体(不成功会抛出一个异常)
            Authentication authentication = authenticationManager.authenticate(upToken);
            // 将登录认证的主体告诉SpringSecurity
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (AuthenticationException e){
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"用户名或密码输入有误！");
        }
        // 加载用户信息及其角色信息
        UserDetails userDetails =  userDetailsService.loadUserByUsername(username);
        // 构建JWT令牌并返回
        return jwtTokenUtil.generateToken(userDetails);
    }

    /**
     * 刷新token
     * @param oldToken  旧token
     * @return
     */
    public String refreshToken(String oldToken) {
        if(!jwtTokenUtil.isTokenExpired(oldToken)){     // 验证token是否过期
            return jwtTokenUtil.refreshToken(oldToken);
        }
        return null;
    }
}

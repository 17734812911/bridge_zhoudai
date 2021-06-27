package com.xtw.bridge.filter;

import com.xtw.bridge.service.authentication.MyUserDetailsService;
import com.xtw.bridge.utils.JwtTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: Mr.Chen
 * Date: 2021/6/25
 * Description: JWT登录认证过滤器
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    JwtTokenUtil jwtTokenUtil;
    @Resource
    MyUserDetailsService myUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 获取JWT令牌
        String jwtToken = request.getHeader(jwtTokenUtil.getHeader());
        if(!StringUtils.isEmpty(jwtToken)){     // 令牌不为空
            // 从令牌中提取用户名
            String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            // 用户名不为空且没有被认证过
            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
                // 校验令牌是不是在有效期
                if(jwtTokenUtil.validateToken(jwtToken,userDetails)){
                    // 对使用该令牌的用户进行授权
                    // 参数分别为认证主体、密码、资源访问权限
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    // 将JWT令牌转换为SpringSecurity认识的令牌
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        // 让过滤器链继续走下去
        filterChain.doFilter(request, response);
    }
}

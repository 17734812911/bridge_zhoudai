package com.xtw.bridge.service.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * User: Mr.Chen
 * Date: 2021/6/24
 * Description: 用于鉴权当前用户的本次访问是否有访问权限
 */
@Component("rbacService")
public class MyRBACService {
    public boolean hasPermission(HttpServletRequest request, Authentication authentication){

        Object principal = authentication.getPrincipal();       // 获取登录认证的主体信息
        if (principal instanceof UserDetails){  // 判断principal是不是UserDetails类型
            UserDetails userDetails = ((UserDetails) principal);

            // 将权限信息封装成GrantedAuthority类型,因为UserDetails中的权限集合就是GrantedAuthority接口类型
            // request.getRequestURI()用于获取权限的唯一标识
            // SimpleGrantedAuthority是GrantedAuthority接口的一个实现类
            // simpleGrantedAuthority是本次访问的资源
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(request.getRequestURI());

            // userDetails.getAuthorities()用于获取当前用户所有能访问的资源
            // 如果包含本次能访问的资源，就返回true,不包含就返回false
            return userDetails.getAuthorities().contains(simpleGrantedAuthority);
        }
        return false;
    }
}

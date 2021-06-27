package com.xtw.bridge.service.authentication;

import com.xtw.bridge.mapper.MyUserDetailsServiceMapper;
import com.xtw.bridge.model.MyUserDetails;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: Mr.Chen
 * Date: 2021/6/24
 * Description: 加载用户信息
 */
@Component
public class MyUserDetailsService implements UserDetailsService {
    @Resource
    MyUserDetailsServiceMapper myUserDetailsServiceMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {    // 参数不一定要是用户名，只要能唯一代表这个用户的标识就行

        // 加载用户基础数据(账号密码等)
        MyUserDetails myUserDetails = myUserDetailsServiceMapper.findByUserName(username);
        // 校验
        if(myUserDetails == null){
            throw new UsernameNotFoundException("用户不存在");
        }

        // 加载用户的角色列表
        List<String> roleCodes = myUserDetailsServiceMapper.findRoleByUserName(username);

        // 根据用户的角色列表加载用户具有哪些权限
        List<String> authorities = myUserDetailsServiceMapper.findAuthorityByRoleCodes(roleCodes);

        //角色是一种特殊的权限，在角色前面加上"ROLE_"代表的是角色权限
        roleCodes = roleCodes.stream().map(rc -> "ROLE_" + rc).collect(Collectors.toList());
        // 将全色权限放到权限列表中
        authorities.addAll(roleCodes);

        // 将authorities放到用户详情中。用SpringSecurity提供的AuthorityUtils工具类将List类型的authorities转换为需要的GrantedAuthority类型
        myUserDetails.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList(String.join(",",authorities)));

        return myUserDetails;
    }
}

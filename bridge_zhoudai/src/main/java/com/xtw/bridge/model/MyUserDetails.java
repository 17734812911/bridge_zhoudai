package com.xtw.bridge.model;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

/**
 * User: Mr.Chen
 * Date: 2021/6/24
 * Description: No Description
 */
@Schema(name = "MyUserDetails", description = "用户信息实体类")
public class MyUserDetails implements UserDetails {
    @Schema(name = "username", description = "用户名")
    private String username;
    @Schema(name = "password", description = "密码")
    private String password;
    @Schema(name = "accountNonExpired", description = "是否没过期")
    private boolean accountNonExpired;
    @Schema(name = "accountNonLocked", description = "是否没被锁定")
    private boolean accountNonLocked;
    @Schema(name = "credentialsNonExpired", description = "凭证(密码)是否没过期")
    private boolean credentialsNonExpired;
    @Schema(name = "enabled", description = "账号是否可用")
    private boolean enabled;
    @Schema(name = "authorities", description = "用户的权限集合")
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {   // 数据库中没有定义该字段，所以直接返回true.有的话再改回来
        this.accountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {   // 数据库中没有定义该字段，所以直接返回true.有的话再改回来
        this.accountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {   // 数据库中没有定义该字段，所以直接返回true.有的话再改回来
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return "MyUserDetails{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", accountNonExpired=" + accountNonExpired +
                ", accountNonLocked=" + accountNonLocked +
                ", credentialsNonExpired=" + credentialsNonExpired +
                ", enabled=" + enabled +
                ", authorities=" + authorities +
                '}';
    }
}

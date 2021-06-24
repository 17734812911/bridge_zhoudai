package com.xtw.bridge.mapper;

import com.xtw.bridge.model.MyUserDetails;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/6/24
 * Description: No Description
 */
public interface MyUserDetailsServiceMapper {

    // 根据用户名或手机号查询用户
    @Select("SELECT username,password,enabled FROM user WHERE username = #{username} or phone=#{username}")
    MyUserDetails findByUserName(@Param("username") String username);


    // 根据用户名或手机号查询用户的所有角色
    @Select("SELECT role_code " +
            "FROM role r " +
            "LEFT JOIN user_role ur ON r.id = ur.role_id " +
            "LEFT JOIN user u ON u.id = ur.user_id " +
            "WHERE u.username = #{username} or u.phone=#{username}")
    List<String> findRoleByUserName(@Param("username") String username);


    // 根据用户的角色列表查询用户所拥有的所有权限
    @Select({
            "<script>",
            "SELECT url ",
            "FROM menu m ",
            "LEFT JOIN role_menu rm ON m.id = rm.menu_id ",
            "LEFT JOIN role r ON r.id = rm.role_id ",
            "WHERE r.role_code IN ",
            "<foreach collection='roleCodes' item='roleCode' open='(' separator=',' close=')'>",
            "#{roleCode}",
            "</foreach>",
            "</script>"
    })
    List<String> findAuthorityByRoleCodes(@Param("roleCodes") List<String> roleCodes);
}

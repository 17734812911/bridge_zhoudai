package com.xtw.bridge.mapper;

import com.xtw.bridge.model.User;

import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/8/5
 * Description: No Description
 */
public interface UserDao {

    // 添加用户
    public int addUser(User user);

    // 关联新用户的角色
    public int relationUserRole(int userId);

    // 根据用户名查询用户
    public User queryUserByName(String username);

    // 修改用户角色
    public int editUserRole(int userId, int roleId);

    // 根据用户名删除用户
    public int delUser(String username);

    // 删除用户角色关联记录
    public int delUserRole(int userId);

    // 查询所有用户
    public List<User> queryAllUser();

    // 根据用户名查询用户密码
    public User queryPasswordByUsername(String userName);

}

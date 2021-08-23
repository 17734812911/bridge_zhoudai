package com.xtw.bridge.service;

import com.xtw.bridge.mapper.UserDao;
import com.xtw.bridge.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/8/5
 * Description: No Description
 */
@Service
public class UserService implements UserDao {

    @Resource
    UserDao userDao;

    @Transactional
    @Override
    public int addUser(User user) {
        User resultUser = queryUserByName(user.getUsername());     // 判断当前用户名是否已经被注册
        if(resultUser == null){
            int result =  userDao.addUser(user);
            // 关联新用户的角色
            relationUserRole(user.getId());
            return result;
        }else{
            return -1;
        }
    }
    // 关联用户角色
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int relationUserRole(int userId) {
        return userDao.relationUserRole(userId);
    }

    // 根据用户名查询用户是否存在
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public User queryUserByName(String username) {
        return userDao.queryUserByName(username);
    }

    // 修改用户角色
    @Transactional
    public int editRole(String username, int roleId){
        User resultUser = queryUserByName(username);
        if(resultUser != null){
            int result = editUserRole(resultUser.getId(), roleId);
            return result;
        }
        return 0;
    }
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int editUserRole(int userId, int roleId) {
        int result =  userDao.editUserRole(userId, roleId);
        return result;
    }

    @Override
    @Transactional      // Spring事务(会等到delUserRole方法执行完后一起提交，否则都不提交)
    public int delUser(String username) {
        // 根据用户名查询用户ID,用于删除用户角色表数据
        User user = userDao.queryUserByName(username);
        // 删除用户
        int resultDelUser = userDao.delUser(username);
        // 删除用户角色表信息
        int resultDelUserRole = delUserRole(user.getId());
        if(resultDelUser > 0 && resultDelUserRole > 0){
            return 1;
        }
        return 0;
    }

    @Transactional(propagation = Propagation.REQUIRED)     // 如果当前没有事务，就新建一个事务，如果已经存在在一个事务中，就加入到这个事务中
    @Override
    public int delUserRole(int userId) {
        return userDao.delUserRole(userId);
    }

    // 查询所有用户
    @Override
    public List<User> queryAllUser() {
        List<User> userList = userDao.queryAllUser();
        return userList;
    }

    // 根据用户名查询用户密码
    public User queryPasswordByUsername(String userName){
        User user = userDao.queryPasswordByUsername(userName);
        return user;
    }
}

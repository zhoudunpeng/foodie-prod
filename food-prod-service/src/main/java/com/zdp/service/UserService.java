package com.zdp.service;

import com.zdp.pojo.Users;
import com.zdp.pojo.bo.UserBO;

/**
 * @author sesshomaru
 * @date 2021/4/24 22:58
 */
public interface UserService {

    /**
     * 判断用户名是否存在
     */
    public boolean queryUserNameIsExist(String username);

    /**
     * 判断用户名是否存在
     */
    public Users createUser(UserBO userBO);

    /**
     * 检索用户名和密码是否匹配，用于登录
     */
    public Users queryUserForLogin(String username, String password);
}

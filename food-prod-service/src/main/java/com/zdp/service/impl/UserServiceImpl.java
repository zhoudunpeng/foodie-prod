package com.zdp.service.impl;

import com.zdp.enums.Sex;
import com.zdp.mapper.UsersMapper;
import com.zdp.pojo.Users;
import com.zdp.pojo.bo.UserBO;
import com.zdp.service.UserService;
import com.zdp.utils.DateUtil;
import com.zdp.utils.MD5Utils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

/**
 * @author sesshomaru
 * @date 2021/4/24 23:00
 */
@Service
public class UserServiceImpl implements UserService {

    private static final String USER_FACE = "http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png";

    @Autowired
    private Sid sid;

    @Autowired
    private UsersMapper usersMapper;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean queryUserNameIsExist(String username) {
        Example userExample = new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();
        // 使用实体类的属性即可
        criteria.andEqualTo("username", username);
        Users users = usersMapper.selectOneByExample(userExample);
        return users == null ? false : true;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users createUser(UserBO userBO) {

//        try {
//            Thread.sleep(3500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        String userId = sid.nextShort();

        Users user = new Users();
        user.setId(userId);
        user.setUsername(userBO.getUsername());
        try {
            user.setPassword(MD5Utils.getMD5Str(userBO.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 默认用户昵称同用户名
        user.setNickname(userBO.getUsername());
        // 默认头像
        user.setFace(USER_FACE);
        // 默认生日
        user.setBirthday(DateUtil.stringToDate("1900-01-01"));
        // 默认性别为 保密
        user.setSex(Sex.secret.type);

        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());

        usersMapper.insert(user);

        return user;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserForLogin(String username, String password) {

//        try {
//            Thread.sleep(2500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        Example userExample = new Example(Users.class);
        Example.Criteria userCriteria = userExample.createCriteria();

        userCriteria.andEqualTo("username", username);
        userCriteria.andEqualTo("password", password);

        Users result = usersMapper.selectOneByExample(userExample);

        return result;
    }
}

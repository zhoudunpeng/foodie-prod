package com.zdp.controller;

import com.zdp.pojo.Users;
import com.zdp.pojo.bo.UserBO;
import com.zdp.service.UserService;
import com.zdp.utils.CookieUtils;
import com.zdp.utils.IMOOCJSONResult;
import com.zdp.utils.JsonUtils;
import com.zdp.utils.MD5Utils;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author sesshomaru
 * @date 2021/4/24 23:06
 */
@RestController
@RequestMapping("passport")
public class PassportController {

    @Autowired
    private UserService userService;

    @GetMapping("usernameIsExist")
    public int usernameIsExist(@RequestParam("username") String username) {
        if (StringUtils.isBlank(username)) {
            return 500;
        }

        boolean isExist = userService.queryUserNameIsExist(username);
        if (isExist) {
            return 500;
        }
        return 200;
    }

    @ApiOperation(value = "用户注册", notes = "用户注册", httpMethod = "POST")
    @PostMapping("/regist")
    public IMOOCJSONResult regist(@RequestBody UserBO userBO,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {

        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPwd = userBO.getConfirmPassword();

        // 0. 判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password) ||
                StringUtils.isBlank(confirmPwd)) {
            return IMOOCJSONResult.errorMsg("用户名或密码不能为空");
        }

        // 1. 查询用户名是否存在
        boolean isExist = userService.queryUserNameIsExist(username);
        if (isExist) {
            return IMOOCJSONResult.errorMsg("用户名已经存在");
        }

        // 2. 密码长度不能少于6位
        if (password.length() < 6) {
            return IMOOCJSONResult.errorMsg("密码长度不能少于6");
        }

        // 3. 判断两次密码是否一致
        if (!password.equals(confirmPwd)) {
            return IMOOCJSONResult.errorMsg("两次密码输入不一致");
        }

        // 4. 实现注册
        Users userResult = userService.createUser(userBO);

        userResult = setNullProperty(userResult);

        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(userResult), true);

        // TODO 生成用户token，存入redis会话
        // TODO 同步购物车数据

        return IMOOCJSONResult.ok();
    }


    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @PostMapping("/login")
    public IMOOCJSONResult login(@RequestBody UserBO userBO,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        String username = userBO.getUsername();
        String password = userBO.getPassword();

        // 0. 判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password)) {
            return IMOOCJSONResult.errorMsg("用户名或密码不能为空");
        }

        // 1. 实现登录
        Users userResult = userService.queryUserForLogin(username,
                MD5Utils.getMD5Str(password));

        if (userResult == null) {
            return IMOOCJSONResult.errorMsg("用户名或密码不正确");
        }

        userResult = setNullProperty(userResult);

        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(userResult), true);


        // TODO 生成用户token，存入redis会话
        // TODO 同步购物车数据

        return IMOOCJSONResult.ok(userResult);
    }

    private Users setNullProperty(Users userResult) {
        userResult.setPassword(null);
        userResult.setMobile(null);
        userResult.setEmail(null);
        userResult.setCreatedTime(null);
        userResult.setUpdatedTime(null);
        userResult.setBirthday(null);
        return userResult;
    }

    @ApiOperation(value = "用户退出登录", notes = "用户退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    public IMOOCJSONResult logout(@RequestParam String userId,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {

        // 清除用户的相关信息的cookie
        CookieUtils.deleteCookie(request, response, "user");

        // TODO 用户退出登录，需要清空购物车
        // TODO 分布式会话中需要清除用户数据

        return IMOOCJSONResult.ok();
    }

}

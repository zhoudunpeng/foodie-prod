package com.zdp.controller;

import com.zdp.pojo.Stu;
import com.zdp.service.StuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sesshomaru
 * @date 2021/4/22 21:00
 */
@RestController
public class StuController {

    @Autowired
    private StuService stuService;

    @GetMapping("/getStu")
    public Stu getStu(Integer id){
        return stuService.getStuInfo(id);
    }
}

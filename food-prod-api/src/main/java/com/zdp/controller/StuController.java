package com.zdp.controller;

import com.zdp.pojo.Stu;
import com.zdp.service.StuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author sesshomaru
 * @date 2021/4/22 21:00
 */
@ApiIgnore //不再显示再swagger2的文档里
@RestController
public class StuController {

    @Autowired
    private StuService stuService;

    @GetMapping("/getStu")
    public Stu getStu(Integer id){
        return stuService.getStuInfo(id);
    }

    // 添加存在幂等  插入多次 需要幂等控制
    @PostMapping("/saveStu")
    public Object saveStu() {
        stuService.saveStu();
        return "OK";
    }

    // 更新有幂等 同一份内容更新100次 也只会更新1次有效所以存在幂等  无需幂等控制
    @PostMapping("/updateStu")
    public Object updateStu(int id) {
        stuService.updateStu(id);
        return "OK";
    }

    // 删除有幂等 删除多次起作用的只有1次所以存在幂等 无需幂等控制
    @PostMapping("/deleteStu")
    public Object deleteStu(int id) {
        stuService.deleteStu(id);
        return "OK";
    }
}

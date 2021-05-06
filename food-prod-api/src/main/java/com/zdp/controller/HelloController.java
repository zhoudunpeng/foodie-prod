package com.zdp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author sesshomaru
 * @date 2021/4/22 19:47
 */
@ApiIgnore //不再显示再swagger2的文档里
@RestController
public class HelloController {

    @GetMapping("/hello")
    public Object hello() {
        return "hello world";
    }
}

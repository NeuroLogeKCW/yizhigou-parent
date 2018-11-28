package com.yizhigou.manager.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("login")
public class IndexController {

    @RequestMapping("name")
    public Map name(){

        //获取登录名
        String name=SecurityContextHolder.getContext().getAuthentication().getName();
        Map map=new HashMap();
        //放到Map中
        map.put("loginName", name);
        //返回这个Map
        return map ;
    }
}

package com.cmx.springbootshiro.controller;


import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * j权限测试的controller
 */
@RestController
@RequestMapping("/remember")
public class TestController {

    @RequestMapping("/from")
    public String testForm(){
        return "form test success";
    }


    @RequestMapping("/getAuth")
    @RequiresRoles("admin")
    public String testRole(){
        return "user has admin role!";
    }

    @RequestMapping("/remember")
    public String isRemember(){
        return "remember success";
    }



}

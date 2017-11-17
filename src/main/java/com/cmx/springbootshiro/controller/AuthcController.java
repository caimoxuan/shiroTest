package com.cmx.springbootshiro.controller;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/authSuccess")
public class AuthcController {

    private static final Logger log = LoggerFactory.getLogger(AuthcController.class);

    @RequestMapping("/getAuthStatus")
    public String getAuthStatus(){
        log.debug("getAuthStatus  start");
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        System.out.println("获取用户session : " + session.getId());

        return subject.getPrincipal() + "'s userStatus :" + (subject.isAuthenticated()?"authed":"unAuthed");
    }


    @RequestMapping(value = "/hasRole")
    @RequiresPermissions("systemUser")
    public String getRole(String username){

        log.debug("getRole {}", username);

        return "if you has systemUser permission, you can see this";
    }


    @RequestMapping("/getString")
    @RequiresPermissions("admin")
    public String getString(){
        return "if you has admin permission, you can see this!";
    }


    @RequestMapping("/getUserName")
    public String getUserName(){
        Subject subject = SecurityUtils.getSubject();
        return subject.getPrincipal().toString();
    }

/**
    @ExceptionHandler({AuthorizationException.class})
    public String doNotHasPermission(HttpServletResponse response, HttpServletRequest request){
        return "没有权限";
    }
*/
}

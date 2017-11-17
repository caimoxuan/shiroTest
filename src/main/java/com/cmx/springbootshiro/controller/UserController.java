package com.cmx.springbootshiro.controller;


import com.cmx.springbootshiro.entity.SystemUser;
import com.cmx.springbootshiro.service.SystemUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;

@RestController
@RequestMapping("/userLogin")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);


    @Autowired
    SystemUserService userService;

    @Autowired
    SessionDAO sessionDAO;

    @Value("${shiro.algorith.name}")
    private String algorithName;

    @Value("${shiro.hash.iterators}")
    private Integer hashIterator;


    @RequestMapping("/getValue")
    public String getValue(){
        return algorithName + " : " + hashIterator;
    }


    @RequestMapping("/getUser")
    @ResponseBody
    public SystemUser getSystemUserById(@RequestParam String userId){

        SystemUser user = new SystemUser();
        try{
            user = userService.findUserById(userId);
        }catch(Exception e){
            System.out.println("getUsererror : " + e.toString());
        }

        return user;
    }

    @RequestMapping("/addUser")
    @ResponseBody
    public String addUser(SystemUser user){

        try{
            userService.addUser(user);
        }catch(Exception e){
            System.out.println("add Exception : " + e);
            return "user fail! try again!";
        }

        return "success";
    }

    @RequestMapping("/login")
    public String login(HttpServletResponse response, String username, String password, String rememberMe){

        log.debug("username : {} ,password : {} , rememberMe : {}", username, password, rememberMe);

        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        token.setRememberMe(true);
        Subject subject = SecurityUtils.getSubject();
        try{
            subject.login(token);

            Session session = subject.getSession();
            System.out.println("获取当前用户的session : " + session.getId() + ",用户session 的过期时间 ：" + session.getTimeout());

            log.debug("这里验证授权: {} ", subject.isPermitted("admin"));

            response.sendRedirect("http://localhost/userLogin/success.html");
        }catch(Exception e){
            System.out.println(e);
            return "faile";
        }

        return "什么都没有发生";
    }

    /**
     * 这种方式只适用于web端前后端不分离的情况，由于没有登陆的实体页面，会导致返回success
     * @param request
     * @return
     */
    @RequestMapping(value = "/nofilterlogin")
    public String loginNoFilter(HttpServletRequest request, HttpServletResponse response)throws Exception {

        String failMessage = (String)request.getAttribute("shiroLoginFailure");
        System.out.println("login exception : " + failMessage);
        if(failMessage != null){
            response.sendRedirect("http://localhost/userLogin/login.html");
            return "fail";
        }

        response.sendRedirect("http://localhost/userLogin/success.html");
        return "success";

    }


    @RequestMapping(value = "/getActiveUser")
    public String getActivrUser(){

        Collection<Session> sessionList = sessionDAO.getActiveSessions();
        for(Session s : sessionList){

            System.out.println("存活的session ：" + s.getId());
        }

        return "success";
    }

    /**
     * 根据sessionId查看session中的信息
     * @param sessionId
     * @return
     */
    @RequestMapping(value = "/getSessionTimeOut")
    public String getSessionTimeOut(String sessionId){
        Collection<Session> sessionList = sessionDAO.getActiveSessions();
        for(Session s : sessionList){
            if(sessionId.equals(s.getId())){

                Date lastAccessTime = s.getLastAccessTime();
                System.out.println(new Date(lastAccessTime.getTime()+s.getTimeout()));

                String username = s.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY).toString();
                System.out.println(username);
                System.out.println(lastAccessTime + "  session 会过期时间 ：" + s.getTimeout());
            }
        }

        return "success";
    }


    @RequestMapping(value = "/getSessionSubjecgt")
    public String getSubject(String sessionId){
        Subject subject = SecurityUtils.getSubject();
        Session s = subject.getSession();
        return "success";
    }


}

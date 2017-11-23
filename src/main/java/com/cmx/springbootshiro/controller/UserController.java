package com.cmx.springbootshiro.controller;


import com.cmx.springbootshiro.entity.SystemUser;
import com.cmx.springbootshiro.service.SystemUserService;
import com.cmx.springbootshiro.shiro.entity.SessionEntity;
import com.cmx.springbootshiro.shiro.service.IUserSessionService;
import com.cmx.springbootshiro.util.SerializableUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/userLogin")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);


    @Autowired
    SystemUserService userService;

    @Autowired
    SessionDAO sessionDAO;

    @Autowired
    IUserSessionService userSessionService;


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

        }catch(Exception e){
            System.out.println(e);
            return "faile";
        }
        return "success";
    }

    /**
     * 注意如果没有filter控制 这种方式只适用于web端前后端不分离的情况，由于没有登陆的实体页面，会导致返回success
     * @param request
     * @return
     */
    @RequestMapping(value = "/nofilterlogin")
    public String loginNoFilter(HttpServletRequest request, HttpServletResponse response)throws Exception {

        String failMessage = (String)request.getAttribute("shiroLoginFailure");
        System.out.println("login exception : " + failMessage);
        if(failMessage != null){
            //response.sendRedirect("http://localhost/userLogin/login.html");
            return failMessage;
        }

        //response.sendRedirect("http://localhost/userLogin/success.html");
        return "success";

    }


    @RequestMapping(value = "/getAllSession")
    @ResponseBody
    @RequiresRoles({"admin","systemUser"})
    public Map<Object, Object> getAllSession(){

        Map<Object, Object> result = new HashMap<>();
        List<Map<Object, String>> items = new ArrayList<>();

        List<SessionEntity> sessionEntities = userSessionService.query(new SessionEntity());
        for(SessionEntity sessionEntity : sessionEntities){
                Map<Object, String> map = new HashMap<>(4);
                Session session = SerializableUtils.deserialize(sessionEntity.getSession_id());
                String sessionId = (String)session.getId();
                String host = session.getHost();
                String lastAccessTime = SimpleDateFormat.getInstance().format(session.getLastAccessTime());

                map.put("sessionId", sessionId);
                map.put("host", host);
                map.put("lastAccessTime", lastAccessTime);
                SimplePrincipalCollection spc = (SimplePrincipalCollection)session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                if(null != spc){
                    log.info("getUser : {} ", spc);
                    map.put("spc", spc.toString());
                }
                items.add(map);
        }

        result.put("row", items);
        result.put("pageCount",1);
        result.put("pageSize", 10);
        result.put("pageNow", 1);

        return result;
    }


    @RequestMapping("/deleteSession")
    public String deleteSession(String sessionId){
        if(null == sessionId){
            return "session is not avaliable";
        }
        try{
            userSessionService.delete(sessionId);
        }catch(Exception e){
            log.error("delete session error : {}", e.toString());
        }
        return "success";
    }


}

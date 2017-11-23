package com.cmx.springbootshiro.controller;


import com.cmx.springbootshiro.iservice.IUserPermissionService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/permission")
public class UserPermissionController {


    private static final Logger logger = LoggerFactory.getLogger(UserPermissionController.class);

    @Autowired
    IUserPermissionService userPermissionService;


    @RequestMapping("addSystemRole")
    public String addSystemRole(String roleName,
                                String description,
                                String avaliable){
        Map<String, Object> params = new HashMap<>(3);
        params.put("role", roleName);
        params.put("description", description);
        params.put("avaliable",avaliable);

        try{
            userPermissionService.addSystemRole(params);
        }catch(Exception e){
            logger.error("add role exception : {}", e);
        }

        return "success";
    }


    @RequestMapping("/getUserRole")
    @ResponseBody
    @RequiresRoles("admin")
    public List<Map<String, Object>> getUserRole(String userId){
        return userPermissionService.queryRole(userId);
    }

    @RequestMapping("/getAllRole")
    @ResponseBody
    public  List<Map<String, Object>> getAllRole(){
        return userPermissionService.findAllRole();
    }


    @RequestMapping("/addUserRole")
    public String addUserRole(String userId, String roleId){

        Map<String, Object> params = new HashMap<>(2);
        params.put("userId", userId);
        params.put("roleId", roleId);
        userPermissionService.addUserRole(params);
        return "success";
    }

    @RequestMapping("/updateUserRole")
    public String updateUserRole(String userId, List<String> roleIds){

        if(null != userId && null != roleIds){
            for(String roleId : roleIds){
                Map<String, Object> params = new HashMap<>(2);
                params.put("userId", userId);
                params.put("roleId", roleId);
                userPermissionService.addUserRole(params);
            }
        }

        return "success";
    }

}

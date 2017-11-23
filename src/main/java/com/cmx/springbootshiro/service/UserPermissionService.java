package com.cmx.springbootshiro.service;

import com.cmx.springbootshiro.iservice.IUserPermissionService;
import com.cmx.springbootshiro.mapper.UserPermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class UserPermissionService implements IUserPermissionService{

    @Autowired
    UserPermissionMapper userPermissionMapper;

    @Override
    public List<Map<String, Object>> queryRole(String userId) {

        List<Map<String, Object>> roleMap = userPermissionMapper.queryRole(userId);
        if(roleMap.size() > 0){
            return roleMap;
        }
        return null;
    }

    @Override
    public void addUserRole(Map<String, Object> params) {
        userPermissionMapper.addUserRole(params);
    }

    @Override
    public List<Map<String, Object>> findAllRole() {
        return userPermissionMapper.findAllRole();
    }

    @Override
    public void addSystemRole(Map<String, Object> params) {
        userPermissionMapper.addSystemRole(params);
    }
}

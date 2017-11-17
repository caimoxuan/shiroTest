package com.cmx.springbootshiro.service;

import com.cmx.springbootshiro.entity.SystemUser;
import com.cmx.springbootshiro.iservice.ISystemUserService;
import com.cmx.springbootshiro.mapper.SystemUserMapper;
import com.cmx.springbootshiro.util.UserPasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SystemUserService implements ISystemUserService {


    @Autowired
    private UserPasswordUtil passwordUtil;

    @Autowired
    private SystemUserMapper systemUserMapper;


    @Override
    public void addUser(SystemUser user) {
        passwordUtil.encryptPassword(user);
        systemUserMapper.add(user);
    }

    @Override
    public void updateUser(SystemUser user) {
        systemUserMapper.modify(user);
    }

    @Override
    public SystemUser findUserById(String id) {
        return systemUserMapper.getById(id);
    }

    @Override
    public SystemUser findUserByName(String username) {
        return systemUserMapper.getByName(username);
    }

    @Override
    public List<SystemUser> findAllUser() {
        return systemUserMapper.query();
    }


}

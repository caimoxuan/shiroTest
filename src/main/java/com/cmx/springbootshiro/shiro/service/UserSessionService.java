package com.cmx.springbootshiro.shiro.service;

import com.cmx.springbootshiro.shiro.entity.SessionEntity;
import com.cmx.springbootshiro.shiro.mapper.UserSessionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class UserSessionService implements IUserSessionService {


    @Autowired
    private UserSessionMapper userSessionMapper;

    @Override
    public void add(SessionEntity se) {
        userSessionMapper.add(se);
    }

    @Override
    public void delete(Serializable session) {
        userSessionMapper.delete(session);
    }

    @Override
    public void update(SessionEntity se) {
        userSessionMapper.modify(se);
    }

    @Override
    public SessionEntity findById(Serializable session) {
        return userSessionMapper.getById(session);
    }

    @Override
    public SessionEntity query(SessionEntity se) {
        return null;
    }
}

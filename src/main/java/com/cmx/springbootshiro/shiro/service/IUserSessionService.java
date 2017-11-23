package com.cmx.springbootshiro.shiro.service;

import com.cmx.springbootshiro.shiro.entity.SessionEntity;

import java.io.Serializable;
import java.util.List;

public interface IUserSessionService {

    void add(SessionEntity se);

    void delete(Serializable session);

    void update(SessionEntity se);

    SessionEntity findById(Serializable session);

    List<SessionEntity> query(SessionEntity se);

}

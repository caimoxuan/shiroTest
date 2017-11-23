package com.cmx.springbootshiro.shiro.mapper;


import com.cmx.springbootshiro.shiro.entity.SessionEntity;
import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;
import java.util.List;

@Mapper
public interface UserSessionMapper {

    void add(SessionEntity se);

    void modify(SessionEntity se);

    void delete(Serializable session);

    List<SessionEntity> query(Serializable session);

    SessionEntity getById(Serializable session);


}

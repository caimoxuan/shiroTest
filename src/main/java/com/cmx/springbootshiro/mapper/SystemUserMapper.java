package com.cmx.springbootshiro.mapper;


import com.cmx.springbootshiro.entity.SystemUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SystemUserMapper {

    void add(SystemUser user);

    void modify(SystemUser user);

    SystemUser getById(@Param("id") String id);
    SystemUser getByName(@Param("username")String username);

    List<SystemUser> query();



}

package com.cmx.springbootshiro.mapper;


import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserPermissionMapper {

    List<Map<String, Object>> queryRole(String userId);

    void addUserRole(Map<String, Object> param);

    List<Map<String, Object>> findAllRole();

    void addSystemRole(Map<String, Object> params);

}

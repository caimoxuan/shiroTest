package com.cmx.springbootshiro.iservice;

import java.util.List;
import java.util.Map;

public interface IUserPermissionService {

    List<Map<String, Object>> queryRole(String userId);

    void addUserRole(Map<String, Object> params);

    List<Map<String, Object>> findAllRole();

    void addSystemRole(Map<String, Object> params);

}

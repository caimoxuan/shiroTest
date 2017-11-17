package com.cmx.springbootshiro.iservice;

import com.cmx.springbootshiro.entity.SystemUser;
import java.util.List;

public interface ISystemUserService {

    void addUser(SystemUser user);

    void updateUser(SystemUser user);

    SystemUser findUserById(String id);

    SystemUser findUserByName(String username);

    List<SystemUser> findAllUser();

}

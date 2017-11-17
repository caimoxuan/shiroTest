package com.cmx.springbootshiro.shiro.realm;

import com.cmx.springbootshiro.entity.SystemUser;
import com.cmx.springbootshiro.service.SystemUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class SimpleUserAuthRealm extends AuthorizingRealm {

    private static final Logger log = LoggerFactory.getLogger(SimpleUserAuthRealm.class);


    @Autowired
    SystemUserService systemUserService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {


        System.out.println("进入权限授予");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        Object object = principalCollection.getPrimaryPrincipal();

        log.debug("权限授予中的userName : {}", object.toString());
        List<String> permissionList = new ArrayList<>();
        permissionList.add("admin");
        authorizationInfo.addStringPermissions(permissionList);

        return authorizationInfo;
    }

    /**
     * 用来验证身份的
     * @param authenticationToken
     * @return
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        System.out.println("进入验证身份");
        //获取用户账号
        String userName = (String)authenticationToken.getPrincipal();
        Object password = authenticationToken.getCredentials();
        if(null == userName || password == null){
            log.debug("用户没有输入用户名和密码");
            throw new UnknownAccountException("没有填写账号或密码");
        }
        log.debug("authenticationInfo username {}", userName);
        SystemUser user = systemUserService.findUserByName(userName);

        if(user == null){
            log.debug("账号不存在");
            throw new UnknownAccountException("未知的账号");
        }
        if(user.getLocked()) {
            log.debug("账号被锁定");
            throw new LockedAccountException("被锁定的账号");
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user.getUsername(),user.getPassword(), ByteSource.Util.bytes(user.getCredentialsSalt()),getName());

        return authenticationInfo;
    }

}

package com.cmx.springbootshiro.shiro.session.dao;

import com.cmx.springbootshiro.shiro.entity.SessionEntity;
import com.cmx.springbootshiro.shiro.service.UserSessionService;
import com.cmx.springbootshiro.util.SerializableUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;


public class SimpleCacheSessionDao extends CachingSessionDAO {

    private static final Logger log = LoggerFactory.getLogger(SimpleCacheSessionDao.class);

    private UserSessionService userSessionService;

    public UserSessionService getUserSessionService() {
        return userSessionService;
    }

    public void setUserSessionService(UserSessionService userSessionService) {
        this.userSessionService = userSessionService;
    }


    @Override
    protected void doUpdate(Session session) {

        if(null != session.getId()){
            session.touch();
            SessionEntity sessionEntity = new SessionEntity();
            sessionEntity.setSession_id(SerializableUtils.serialize(session));
            sessionEntity.setCookie(session.getId().toString());
            System.out.println("upadte session : " + SerializableUtils.serialize(session).length());
            userSessionService.update(sessionEntity);
        }else{
            log.error("session id is null!");
        }
    }

    @Override
    protected void doDelete(Session session) {
        System.out.println("删除过期的session " + session.getId());
        userSessionService.delete(session.getId());
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable cookie =  generateSessionId(session);
        assignSessionId(session, cookie);
        log.debug("doCreate ： " + cookie);
        SessionEntity sessionEntity = new SessionEntity();
        sessionEntity.setCookie(cookie.toString());
        sessionEntity.setSession_id(SerializableUtils.serialize(session));
        userSessionService.add(sessionEntity);
        System.out.println("create session and save :" + sessionEntity.toString());
        return session.getId();//SerializableUtils.serialize(session);
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        Session session = null;



        System.out.println("readSesion start");
        System.out.println("readSession UnknowSessionException : 开始从数据库读 .");
        SessionEntity sessionEntity = userSessionService.findById(sessionId);

        System.out.println("取的时候的sessionId: " + sessionId);
        if(sessionEntity != null){
            session = SerializableUtils.deserialize(sessionEntity.getSession_id());
            System.out.println("取出用户的session : " + session);
        }else{
            System.out.println("没有取到session");
        }
        return session;

    }
}

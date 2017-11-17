package com.cmx.springbootshiro.shiro.session.dao;


import com.cmx.springbootshiro.shiro.entity.SessionEntity;
import com.cmx.springbootshiro.shiro.service.UserSessionService;
import com.cmx.springbootshiro.util.SerializableUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;


@Component
public class SimpleSessionDao extends EnterpriseCacheSessionDAO {


    private static final Logger log = LoggerFactory.getLogger(SimpleSessionDao.class);


    private UserSessionService userSessionService;

    public void setUserSessionService(UserSessionService userSessionService){
        this.userSessionService = userSessionService;
    }

    @Override
    public Serializable create(Session session) {
        Serializable cookie =  super.create(session);
        SessionEntity sessionEntity = new SessionEntity();
        sessionEntity.setCookie(cookie.toString());
        sessionEntity.setSession_id(SerializableUtils.serialize(session));
        userSessionService.add(sessionEntity);
        System.out.println("create session and save :" + sessionEntity.toString());
        return cookie;
    }

    @Override
    public Session readSession(Serializable sessionId) throws UnknownSessionException {
        Session session = null;
        System.out.println("readSesion start");
        try{
            session = super.readSession(sessionId);
            log.debug("在父类方法中取出的session: {}", session.getId());
        }catch(Exception e){
            if(e instanceof UnknownSessionException) {
                System.out.println("readSession UnknowSessionException : 开始从数据库读 .");
                SessionEntity sessionEntity = userSessionService.findById(sessionId);
                System.out.println("取的时候的sessionId: " + sessionId);
                if(sessionEntity != null){
                    session = SerializableUtils.deserialize(sessionEntity.getSession_id());
                    System.out.println("取出用户的session : " + session + "session 状态 : " + ((SimpleSession)session).isValid());

                    if(null != session){
                        ((SimpleSession)session).setLastAccessTime(new Date());
                        ((SimpleSession)session).setExpired(false);
                        update(session);
                    }
                }else{
                    System.out.println("没有取到session");
                }


            }else{
                System.out.println("readSession ： " + e);
            }
        }

        if(null == session){
            System.out.println("error ： readsession 还是 null");
        }else{
            if(((SimpleSession)session).isValid()){

            }else{
                log.debug("用户的session过期 {}", ((SimpleSession) session).isValid());
            }
        }


        return session;
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        super.update(session);
        SessionEntity sessionEntity = userSessionService.findById(session.getId());
        if(sessionEntity != null){
            sessionEntity.setSession_id(SerializableUtils.serialize(session));
            System.out.println("upadte session : " + SerializableUtils.serialize(session).length());
            userSessionService.update(sessionEntity);
        }
    }

    @Override
    public void delete(Session session) {
        System.out.println("删除过期的session " + session.getId());
        userSessionService.delete(session.getId());
        super.delete(session);
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {

        System.out.println("doReadSession");
        Session session =  super.doReadSession(sessionId);

        if(session == null){
            System.out.println("doReadSession is null");
        }else{
            System.out.println("doReadSession has value !");
        }

        return session;

    }



}

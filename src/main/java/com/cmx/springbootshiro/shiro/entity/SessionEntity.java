package com.cmx.springbootshiro.shiro.entity;


import java.io.Serializable;

public class SessionEntity {

    private Long id;

    private String cookie;

    private String session_id;

    public SessionEntity() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    @Override
    public String toString() {
        return "SessionEntity{" +
                "id=" + id +
                ", cookie='" + cookie + '\'' +
                ", session_id=" + session_id +
                '}';
    }
}

package com.cmx.springbootshiro.shiro.filter;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.servlet.AdviceFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class RedirectFilter extends AdviceFilter {

    private static final Logger logger = LoggerFactory.getLogger(RedirectFilter.class);


    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        logger.debug("======前置增强");
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        Object obj = session.getAttribute(DefaultSubjectContext.AUTHENTICATED_SESSION_KEY);
        boolean state = false;
        if(null != obj){
           state = (boolean)obj;
        }
        String failMessage = (String)request.getAttribute("shiroLoginFailure");
        if(null != failMessage){//如果是登陆验证，不拦截
            return true;
        }
        if(subject.isAuthenticated() || subject.isRemembered() || state){ //如果用户是登陆过或者是通过rememberMe登陆的 或者 session 存在 不拦截
            return true;
        }else{
            ((HttpServletResponse)response).sendRedirect("http://localhost/userLogin/login.html");
            return false;
        }
    }

    @Override
    protected void postHandle(ServletRequest request, ServletResponse response) throws Exception {
        logger.debug("======后置增强");
        super.postHandle(request, response);
    }
}

package com.cmx.springbootshiro.shiro.exception;

import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.session.UnknownSessionException;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShiroAuthException implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        System.out.println(httpServletRequest.getRequestURI());

        ModelAndView modelAndView = new ModelAndView();

        System.out.println("exception" + e);
        try{
             if(e instanceof UnauthorizedException){
                 httpServletResponse.setStatus(403);//没有权限
             }else if(e instanceof UnknownSessionException){
                 httpServletResponse.setStatus(302); //跳转
             }else{
                 httpServletResponse.setStatus(400);
             }
        }catch(Exception ex){

        }
        return modelAndView;
    }
}

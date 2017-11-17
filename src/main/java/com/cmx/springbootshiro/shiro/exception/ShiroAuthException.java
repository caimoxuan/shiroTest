package com.cmx.springbootshiro.shiro.exception;

import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
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
                 httpServletResponse.sendRedirect("http://localhost/userLogin/error/permission.html");
             }else{
                 httpServletResponse.sendRedirect("http://localhost/userLogin/error/error.html");
             }
        }catch(Exception ex){

        }
        return modelAndView;
    }
}

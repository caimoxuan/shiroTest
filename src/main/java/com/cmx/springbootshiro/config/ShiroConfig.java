package com.cmx.springbootshiro.config;


import com.cmx.springbootshiro.shiro.exception.ShiroAuthException;
import com.cmx.springbootshiro.shiro.filter.RedirectFilter;
import com.cmx.springbootshiro.shiro.filter.SimpleFormAuthFilter;
import com.cmx.springbootshiro.shiro.listener.SimpleSessionListener;
import com.cmx.springbootshiro.shiro.matcher.SimpleCredentialsMatcher;
import com.cmx.springbootshiro.shiro.realm.SimpleUserAuthRealm;
import com.cmx.springbootshiro.shiro.service.UserSessionService;
import com.cmx.springbootshiro.shiro.session.dao.SimpleCacheSessionDao;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ShiroConfig {


    @Value("${shiro.algorith.name}")
    private String algorithName;

    @Value("${shiro.hash.iterators}")
    private Integer hashIterator;

    @Value("${shiro.login.url}")
    private String loginUrl;

    @Value("${shiro.success.url}")
    private String successUrl;


    /**
     *  开启shiro aop注解支持
     *  使用代理方式;所以需要开启代码支持
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        //设置securityManager (必须)
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //前端的页面的设置
        shiroFilterFactoryBean.setLoginUrl(loginUrl);
        shiroFilterFactoryBean.setSuccessUrl(successUrl);

        //拦截器的设置
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        //匹配所有请求 authc 需要验证， anon 不需要验证
        filterChainDefinitionMap.put("/userLogin/nofilterlogin", "form,redirect");

        filterChainDefinitionMap.put("/userLogin/*", "anon");

        filterChainDefinitionMap.put("/remember", "user");
        //登出shiro已经实现
        filterChainDefinitionMap.put("/logout", "logout");
        filterChainDefinitionMap.put("/**", "user,redirect");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        //配置自定义的拦截器
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("form", simpleFormAuthFilter());
        filterMap.put("redirect", new RedirectFilter());
        shiroFilterFactoryBean.setFilters(filterMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 密码凭证匹配器
     * @return
     */
    @Bean
    public CredentialsMatcher credentialsMatcher(){
        SimpleCredentialsMatcher credentialsMatcher = new SimpleCredentialsMatcher(ehCacheManager());
        credentialsMatcher.setHashAlgorithmName(algorithName);
        credentialsMatcher.setHashIterations(hashIterator);
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        return credentialsMatcher;
    }

    /**
     * 自定realm
     * @return
     */
    @Bean
    public SimpleUserAuthRealm simpleUserAuthRealm(){
        SimpleUserAuthRealm simpleUserAuthRealm  = new SimpleUserAuthRealm();
        simpleUserAuthRealm.setCredentialsMatcher(credentialsMatcher());
        return simpleUserAuthRealm;
    }

    public FormAuthenticationFilter simpleFormAuthFilter(){
        SimpleFormAuthFilter simpleFormAuthFilter = new SimpleFormAuthFilter();
        simpleFormAuthFilter.setRememberMeParam("rememberMe");
        return simpleFormAuthFilter;
    }


    @Bean
    public SecurityManager securityManager(SessionManager sessionManager){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //realm配置
        securityManager.setRealm(simpleUserAuthRealm());
        /**
         * DefaultSessuinManager javaSE环境, 不向前端传送cookie
         * DefaultWebSessionManager  传送cookie，但是不是sevlet 的session管理方式
         * DefaultContainerSessionManager 使用servlet容器来管理session
         */
        securityManager.setSessionManager(sessionManager);
        securityManager.setCacheManager(ehCacheManager());
        securityManager.setRememberMeManager(cookieRememberMeManager());
        return securityManager;
    }

    /**
     * cacheManager  使用ecache缓存
     * @return
     */
    @Bean
    public EhCacheManager ehCacheManager(){
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile("classpath:shiro-ecache.xml");
        return cacheManager;
    }


    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator creator=new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

    //====================================Session start =============================
    @Bean
    public UserSessionService userSessionService(){
        return new UserSessionService();
    }


    /**
     * 使用sessionDao 管理session的CURD 这里自定义一个sessionDao 将session 序列化后存入数据库 实现持久化
     * @return
     */
    @Bean
    public SessionDAO simpleSessionDao(UserSessionService userSessionService){
//        SimpleSessionDao simpleSessionDao = new SimpleSessionDao();
//        simpleSessionDao.setUserSessionService(userSessionService());
        SimpleCacheSessionDao simpleCacheSessionDao = new SimpleCacheSessionDao();
        simpleCacheSessionDao.setUserSessionService(userSessionService);
        //new EnterpriseCacheSessionDAO(); //这个在缓存中管理session的使用cache的sessionDao 需要定义好cacheManage
        //new MemorySessionDao(); //这个是在内存中管理session
        return simpleCacheSessionDao;
    }


    /**
     * 会话管理器
     * @return
     */
    @Bean
    public SessionManager sessionManager(SessionDAO simpleSessionDao){
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        defaultWebSessionManager.setSessionDAO(simpleSessionDao);
        List<SessionListener> list = new ArrayList<>();
        list.add(sessionListener());
        defaultWebSessionManager.setSessionListeners(list);   //会话监听列表
        defaultWebSessionManager.setDeleteInvalidSessions(true); //是否删除过期的session
        defaultWebSessionManager.setGlobalSessionTimeout(1800000);//设置过期的时间 毫秒为单位
        defaultWebSessionManager.setSessionIdCookie(sessionIdCookie());
        return defaultWebSessionManager;
    }

    @Bean
    public SessionListener sessionListener(){
        return new SimpleSessionListener();
    }

    /**
     * cookie 生成模板
     */
    @Bean("sessionIdCookie")
    public SimpleCookie sessionIdCookie(){
        SimpleCookie simpleCookie = new SimpleCookie("sid");
        simpleCookie.setHttpOnly(true);
        simpleCookie.setMaxAge(-1);// 30 天  -1 ：关闭浏览器失效
        return simpleCookie;
    }
    @Bean("rememberMeCookie")
    public SimpleCookie rememberCookie(){
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        simpleCookie.setHttpOnly(true);
        simpleCookie.setMaxAge(2592000);
        return simpleCookie;
    }

    //rememberMe cookie manager
    @Bean
    public CookieRememberMeManager cookieRememberMeManager(){
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberCookie());
        cookieRememberMeManager.setCipherKey(org.apache.shiro.codec.Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));//cipherKey是加密rememberMe Cookie的密钥；默认AES算法；
        return cookieRememberMeManager;
    }

    @Bean
    public HandlerExceptionResolver shiroAuthException(){
        return new ShiroAuthException();
    }




}

package com.cmx.springbootshiro.shiro.matcher;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class SimpleCredentialsMatcher extends HashedCredentialsMatcher {


    private static final Logger logger = LoggerFactory.getLogger(SimpleCredentialsMatcher.class);

    private Cache<String, AtomicInteger> passwordRetryCache;

    public SimpleCredentialsMatcher(CacheManager cacheManager) {
        passwordRetryCache = cacheManager.getCache("passwordRetryCache");
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token,
                                      AuthenticationInfo info) {
        System.out.println("doCredentialsMatch...");

        String username = (String)token.getPrincipal();

        AtomicInteger retryCount = passwordRetryCache.get(username);
        System.out.println(username +" 验证次数 :" + retryCount);
        if(retryCount == null){
            retryCount = new AtomicInteger(0);
            passwordRetryCache.put(username, retryCount);
        }

        if(retryCount.incrementAndGet() >= 5){
            throw new LockedAccountException();
        }

        boolean result = super.doCredentialsMatch(token, info);

        if(result){
            passwordRetryCache.remove(username);
        }

        return  result;
    }


    @Override
    public String getHashAlgorithmName() {
        System.out.println("AlgorithmName: " + super.getHashAlgorithmName());
        return super.getHashAlgorithmName();
    }


    @Override
    public boolean isStoredCredentialsHexEncoded() {
        return super.isStoredCredentialsHexEncoded();
    }

    @Override
    public void setStoredCredentialsHexEncoded(boolean storedCredentialsHexEncoded) {
        super.setStoredCredentialsHexEncoded(storedCredentialsHexEncoded);
    }


    @Override
    public int getHashIterations() {

        System.out.println("mather：HashIterations" + super.getHashIterations());

        return super.getHashIterations();
    }

    @Override
    public void setHashIterations(int hashIterations) {
        System.out.println("set mather：HashIterations" + super.getHashIterations());
        super.setHashIterations(hashIterations);
    }
}

/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.security.shiro;

import com.areg.project.EndpointConstants;
import com.areg.project.security.jwt.JwtFilter;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroConfig {

    @Bean
    public DefaultWebSecurityManager securityManager() {
        final var securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(createRealm());
        return securityManager;
    }

    @Bean
    public ShiroRealm createRealm() {
        return new ShiroRealm();
    }

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        final var chainDefinition = new DefaultShiroFilterChainDefinition();
        chainDefinition.addPathDefinition(EndpointConstants.LOGIN, "anon");
        chainDefinition.addPathDefinition(EndpointConstants.LOGOUT, "logout");
        chainDefinition.addPathDefinition(EndpointConstants.API, "anon");
        return chainDefinition;
    }

    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilterRegistrationBean(JwtFilter jwtFilter) {
        final var registrationBean = new FilterRegistrationBean<JwtFilter>();
        registrationBean.setFilter(jwtFilter);
        // Set the URL patterns that the filter should apply to
        registrationBean.addUrlPatterns(EndpointConstants.API, EndpointConstants.LOGOUT);
        return registrationBean;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager,
                                                         ShiroFilterChainDefinition chainDefinition) {
        final var shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl(EndpointConstants.LOGIN);
        shiroFilterFactoryBean.setSuccessUrl("/");
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(chainDefinition.getFilterChainMap());
        return shiroFilterFactoryBean;
    }

    public static String getSessionId(Subject currentUser) {
        final Session session = currentUser.getSession();
        return session.getId() != null ? session.getId().toString() : null;
    }
}
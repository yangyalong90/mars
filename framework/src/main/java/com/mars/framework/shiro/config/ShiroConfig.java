package com.mars.framework.shiro.config;

import com.mars.framework.shiro.exception.handler.DefaultExceptionResultParse;
import com.mars.framework.shiro.exception.handler.ExceptionResultParse;
import com.mars.framework.shiro.exception.handler.ShiroExceptionHandler;
import com.mars.framework.shiro.filter.DefaultShiroFilter;
import com.mars.framework.shiro.filter.JwtFilter;
import com.mars.framework.shiro.filter.LoginFilter;
import com.mars.framework.shiro.realm.CustomRealm;
import com.mars.framework.shiro.service.LoginService;
import com.mars.framework.shiro.user.ShiroUserDetail;
import org.apache.shiro.mgt.*;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.*;

@Configuration
@EnableConfigurationProperties(ShiroProperties.class)
public class ShiroConfig {

    private ShiroProperties properties;

    public ShiroConfig(ShiroProperties properties) {
        this.properties = properties;
    }

    @Bean
    public CustomRealm customRealm() {
        return new CustomRealm();
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager,
                                              LoginService loginService,
                                              ShiroExceptionHandler exceptionHandler) {

        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setLoginUrl(properties.getLogin());
        factoryBean.setSecurityManager(securityManager);

        // 注册jwt拦截器
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("jwt", new DefaultShiroFilter(new JwtFilter(), exceptionHandler));
        filterMap.put("login", new DefaultShiroFilter(new LoginFilter(loginService), exceptionHandler));
        factoryBean.setFilters(filterMap);

        // 设置 url 对应 filter
        Map<String, String> filterChainDefinitionMap = new HashMap<>();
        filterChainDefinitionMap.put("/**", "jwt");
        filterChainDefinitionMap.put("/login", "login");
        factoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return factoryBean;

    }

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(customRealm());

        // 关闭 shiro 的session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator sessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(sessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        securityManager.setSubjectFactory(new JwtSubjectFactory());

        return securityManager;
    }

    /**
     * 配置shiro对于权限注解的支持
     * <p>
     * DefaultAdvisorAutoProxyCreator 实现了 BeanProcessor
     * 当 ApplicationContext读如所有的Bean配置信息后，这个类将扫描上下文，
     * 寻找所有的Advistor(一个Advisor是一个切入点和一个通知的组成)，将这些 Advisor 应用到所有符合切入点的Bean中
     *
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 使用 CGLIB 方式创建代理对象
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    /**
     * 配置shiro框架提供的切面类，用于创建代理对象
     * 这里为创建 权限校验的 advisor
     * 在 DefaultAdvisorAutoProxyCreator 的作用下生效
     *
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        return new AuthorizationAttributeSourceAdvisor();
    }

//    @Bean
//    @ConditionalOnMissingBean
//    public ShiroExceptionHandler shiroExceptionHandler(ExceptionResultParse resultParse) {
//        return new ShiroExceptionHandler(resultParse);
//    }

    @Bean
    @ConditionalOnMissingBean(ExceptionResultParse.class)
    public ExceptionResultParse exceptionResultParse(){
        return new DefaultExceptionResultParse();
    }

    @Bean
    @ConditionalOnMissingBean
    public LoginService loginService() {
        return loginUser -> {
            ShiroUserDetail shiroUserDetail = new ShiroUserDetail();
            shiroUserDetail.setUsername(loginUser.getUsername());
            List<String> permissions = new ArrayList<>();
            permissions.add("api:u2");
            permissions.add("api:u3");
            shiroUserDetail.setPermissions(permissions);
            return shiroUserDetail;
        };
    }

    class JwtSubjectFactory extends DefaultWebSubjectFactory {
        @Override
        public Subject createSubject(SubjectContext context) {
            // 这里禁止创建session
            context.setSessionCreationEnabled(false);
            return super.createSubject(context);
        }
    }

}

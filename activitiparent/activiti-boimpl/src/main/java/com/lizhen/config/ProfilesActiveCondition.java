package com.lizhen.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author         :wangyz
 * @date      2018/11/23 09:35
 * 环境判断类
 */
public class ProfilesActiveCondition implements Condition {

    private final static String ACTIVE = "spring.profiles.active";

    private final static String LOCAL = "local";
    private final static String DEV = "dev";
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        Environment env = conditionContext.getEnvironment();
        String profiles = env.getProperty(ACTIVE);
//        return !(DEV.equals(profiles));
        if(LOCAL.equals(profiles)||DEV.equals(profiles)){
            return false;
        }
        return  true;
    }
}

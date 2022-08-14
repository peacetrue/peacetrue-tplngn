package com.github.peacetrue.template;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 目录模板引擎自动配置。
 *
 * @author peace
 **/
@Configuration
@ConditionalOnClass(VelocityTemplateEngine.class)
public class TemplateAutoConfiguration {

    /**
     * 装配默认的目录模板引擎。
     *
     * @return 目录模板引擎
     */
    @Bean
    @ConditionalOnMissingBean(DirectoryTemplateEngine.class)
    public DirectoryTemplateEngine directoryTemplateEngine() {
        return VelocityTemplateEngine.buildVelocityDirectoryTemplateEngine();
    }
}

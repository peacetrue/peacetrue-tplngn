package com.github.peacetrue.template.shell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 目录模板引擎命令行应用。
 *
 * @author peace
 **/
@SpringBootApplication
public class TemplateShellApplication {

    /**
     * 启动应用。
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(TemplateShellApplication.class, args);
    }

}

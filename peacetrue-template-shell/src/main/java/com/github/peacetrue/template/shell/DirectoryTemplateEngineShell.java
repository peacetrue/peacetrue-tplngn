package com.github.peacetrue.template.shell;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.peacetrue.beanmap.BeanMapUtils;
import com.github.peacetrue.log.LoggerUtils;
import com.github.peacetrue.template.DirectoryTemplateEngine;
import com.github.peacetrue.template.Options;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 目录模板引擎命令行组件。
 *
 * @author peace
 **/
@Slf4j
@ShellComponent
public class DirectoryTemplateEngineShell {

    @Autowired
    private DirectoryTemplateEngine directoryTemplateEngine;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 使用变量渲染目录模板。
     *
     * @param templateLocation  目录模板位置
     * @param variablesLocation 变量属性文件位置
     * @param optionsLocation   选项属性文件位置
     * @param resultPath        结果路径
     * @throws IOException 命令执行过程中发生读写异常
     */
    @ShellMethod(value = "渲染目录模板")
    public void evaluate(@ShellOption(help = "目录模板位置。文件路径使用 file:/path/to/template，绝对路径以 / 起始，否则为相对路径，相对路径基于执行 java 命令时所在的目录；类路径使用 classpath:path/to/template，注意不要以 / 起始", value = {"-t", "--templateLocation"}, defaultValue = "file:template") String templateLocation,
                         @ShellOption(help = "选项属性文件位置。从 properties 文件中读取选项，路径规则同上", value = {"-o", "--optionsLocation"}, defaultValue = "file:options.properties") String optionsLocation,
                         @ShellOption(help = "变量属性文件位置。从 properties 文件中读取变量，路径规则同上。支持多个位置，使用逗号分割，例如：file:variables.properties,file:variables1.properties", value = {"-v", "--variablesLocation"}, defaultValue = "file:variables.properties") String variablesLocation,
                         @ShellOption(help = "结果路径。直接是文件路径，例如：/path/to/result，相对路径规则同上", value = {"-r", "--resultPath"}, defaultValue = "result") String resultPath) throws IOException {
        log.info("evaluate args: {}", LoggerUtils.formatArgs(ImmutableMap.of(
                "templateLocation", templateLocation, "optionsLocation", optionsLocation,
                "variablesLocation", variablesLocation, "resultPath", resultPath
        )));

        Map<String, Object> optionsMap = resolveProperties(optionsLocation);
        optionsMap = BeanMapUtils.tier(optionsMap);
        Options options = objectMapper.convertValue(optionsMap, Options.class);
        log.debug("got options: {}", options);

        Map<String, Object> variables = resolveVariables(variablesLocation);
        variables = BeanMapUtils.tier(variables);
        log.debug("got variables: {}", variables);

        directoryTemplateEngine.evaluate(templateLocation, options, variables, resultPath);
    }

    private Map<String, Object> resolveVariables(String variablesLocation) {
        Map<String, Object> variables = new HashMap<>();
        Arrays.stream(variablesLocation.split(","))
                .map(resourceLoader::getResource)
                .filter(Resource::exists)
                .forEach(item -> {
                    try {
                        Properties properties = loadProperties(item);
                        fill(variables, properties);
                    } catch (IOException e) {
                        log.error("load properties from {} error", item);
                    }
                });
        return variables;
    }

    private static Properties loadProperties(Resource item) throws IOException {
        return PropertiesLoaderUtils.loadProperties(new EncodedResource(item, StandardCharsets.UTF_8));
    }

    private Map<String, Object> resolveProperties(String propertiesPath) throws IOException {
        Resource resource = resourceLoader.getResource(propertiesPath);
        if (!resource.exists()) {
            log.warn("can't found properties at {}", propertiesPath);
            return Collections.emptyMap();
        }
        Properties properties = loadProperties(resource);
        return convert(properties);
    }

    private static Map<String, Object> convert(Properties properties) {
        Map<String, Object> map = new HashMap<>(properties.size());
        fill(map, properties);
        return map;
    }

    private static void fill(Map<String, Object> map, Properties properties) {
        properties.forEach((key, value) -> {
            if (key instanceof String) map.put((String) key, value);
        });
    }

}

package com.github.peacetrue.template;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.peacetrue.beanmap.BeanMap;
import com.github.peacetrue.beanmap.BeanMapUtils;
import com.github.peacetrue.io.ConditionalResourcesLoader;
import com.github.peacetrue.io.ResourcesUtilsTest;
import com.github.peacetrue.util.FileUtils;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import static com.github.peacetrue.io.ResourcesLoader.LOCATION_PREFIX_FILE;

/**
 * @author peace
 **/
@Slf4j
class DirectoryTemplateEngineImplTest {

    //tag::antora[]

    /** Antora 目录模板 */
    @Test
    void antora() throws IOException {
        String templateLocation = LOCATION_PREFIX_FILE + ResourcesUtilsTest.templatesDirPath + "/antora";
        String resultPath = ResourcesUtilsTest.templatesDirPath + "/../results/learn";
        Path resultPathObject = Paths.get(resultPath);
        if (Files.exists(resultPathObject)) FileUtils.deleteRecursively(resultPathObject);
        DirectoryTemplateEngine templateEngine = VelocityTemplateEngine.buildVelocityDirectoryTemplateEngine();
        templateEngine.evaluate(
                templateLocation,
                getOptions(),
                Collections.singletonMap("repository", getRepository()),
                resultPath
        );
    }

    private Options getOptions() {
        return Options.builder()
                .resourceActions(
                        Options.ResourcePathPatterns.builder()
                                .ignore("antora-variable\\.adoc$")
                                .build()
                )
                .build();
    }

    private ImmutableMap<String, String> getRepository() {
        return ImmutableMap.of(
                "name", "Learn",
                "title", "学习",
                "description", "记录学习过程。",
                "directoryName", "learn",
                "packageName", "com.github.peacetrue.learn"
        );
    }
    //end::antora[]

    //tag::antora-module[]

    /** Antora 模块目录模板 */
    @Test
    void antoraModule() throws IOException {
        String templateLocation = LOCATION_PREFIX_FILE + ResourcesUtilsTest.templatesDirPath + "/antora-module";
        String resultPath = ResourcesUtilsTest.templatesDirPath + "/../results/learn-redis";
        Path resultPathObject = Paths.get(resultPath);
        if (Files.exists(resultPathObject)) FileUtils.deleteRecursively(resultPathObject);
        DirectoryTemplateEngine templateEngine = VelocityTemplateEngine.buildVelocityDirectoryTemplateEngine();
        templateEngine.evaluate(
                templateLocation,
                getModuleOptions(),
                ImmutableMap.of("repository", getRepository(), "module", getModule()),
                resultPath
        );
    }

    private Options getModuleOptions() {
        return Options.builder()
                .resourceActions(
                        Options.ResourcePathPatterns.builder()
                                .ignore("antora-module-variable\\.adoc$")
                                .build()
                )
                .build();
    }

    private ImmutableMap<String, String> getModule() {
        return ImmutableMap.of(
                "name", "Redis",
                "title", "学习 Redis",
                "description", "记录学习 Redis 的过程",
                "directoryName", "learn-redis"
        );
    }


    //end::antora-module[]

    @Test
    void templateDirAbsent() throws IOException {
        DirectoryTemplateEngineImpl templateEngine = new DirectoryTemplateEngineImpl();
        templateEngine.setResourcesLoader(ConditionalResourcesLoader.DEFAULT);
        templateEngine.evaluate(
                "classpath:absentTemplateDir",
                new Options(), Collections.emptyMap(),
                ""
        );
    }


    static final ObjectMapper objectMapper = new ObjectMapper();

    /** 生成选项 */
    @Test
    void generateOptions() throws IOException {
        store(new Properties(), getOptions(), "antora-options.properties");
        store(new Properties(), new Options(), "antora-module-options.properties");
        store(new Properties(), VelocityTemplateEngine.getOptions(), "default-options.properties");
    }

    private void store(Properties properties, Options options, String path) throws IOException {
        log.info("options: {}", options);
        Map<String, Object> flatten = BeanMapUtils.flatten(objectMapper.convertValue(options, BeanMap.class));
        flatten.values().removeAll(Collections.singleton(null));
        properties.putAll(flatten);
        properties.store(Files.newOutputStream(ResourcesUtilsTest.templatesDirPath.resolve(path)), "选项属性文件");
    }

    /** 生成变量 */
    @Test
    void generateVariables() throws IOException {
        Properties properties = new Properties();
        Map<String, Object> variables = new LinkedHashMap<>();

        variables.put("repository", getRepository());
        log.info("variables: {}", variables);
        properties.putAll(BeanMapUtils.flatten(variables));
        Path path = ResourcesUtilsTest.templatesDirPath.resolve("antora-variables.properties");
        properties.store(Files.newBufferedWriter(path, StandardCharsets.UTF_8), "变量属性文件");

        variables.put("module", getModule());
        log.info("variables: {}", variables);
        properties.putAll(BeanMapUtils.flatten(variables));
        path = ResourcesUtilsTest.templatesDirPath.resolve("antora-module-variables.properties");
        properties.store(Files.newBufferedWriter(path, StandardCharsets.UTF_8), "变量属性文件");
    }
}

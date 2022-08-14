package com.github.peacetrue.template;

import com.github.peacetrue.io.ConditionalResourcesLoader;
import com.github.peacetrue.velocity.tools.VelocityToolsUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.velocity.tools.ToolContext;
import org.apache.velocity.tools.ToolManager;

import java.io.Reader;
import java.io.Writer;
import java.util.Map;
import java.util.Objects;

import static org.apache.velocity.tools.config.ConfigurationUtils.GENERIC_DEFAULTS_PATH;

/**
 * Velocity 模板引擎。
 *
 * @author peace
 **/
@Slf4j
@Getter
public class VelocityTemplateEngine implements TemplateEngine {

    /** 移除 Velocity 模板文件后缀 */
    public static final ResultPathResolver VELOCITY_RESULT_PATH_RESOLVER = templatePath -> StringUtils.removeEnd(templatePath, ".vm");

    private final VelocityEngine velocityEngine;
    private final ToolManager toolManager = new ToolManager();

    /**
     * 通过 Velocity 引擎构造实例，启用 ToolManager 支持。
     *
     * @param velocityEngine Velocity 引擎
     */
    public VelocityTemplateEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = Objects.requireNonNull(velocityEngine);
        this.velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADERS, "file,classpath");
        this.velocityEngine.setProperty("resource.loader.classpath.class", ClasspathResourceLoader.class.getName());
        this.toolManager.setVelocityEngine(velocityEngine);
        this.toolManager.configure(GENERIC_DEFAULTS_PATH);
        this.toolManager.configure(VelocityToolsUtils.TOOLS_PATH);
        this.velocityEngine.init();
    }

    @Override
    public void evaluate(Reader template, Map<String, ?> variables, Writer result) {
        log.debug("render Reader template");
        ToolContext context = toolManager.createContext();
        variables.forEach(context::put);
        velocityEngine.evaluate(context, result, "peacetrue-template", template);
    }

    /**
     * 构建 Velocity 目录模板引擎。
     *
     * @return Velocity 目录模板引擎
     */
    public static DirectoryTemplateEngine buildVelocityDirectoryTemplateEngine() {
        DirectoryTemplateEngineImpl templateEngine = new DirectoryTemplateEngineImpl();
        templateEngine.setResourcesLoader(ConditionalResourcesLoader.DEFAULT);
        templateEngine.setOptions(getOptions());
        templateEngine.setResultPathResolver(VelocityTemplateEngine.VELOCITY_RESULT_PATH_RESOLVER);
        templateEngine.setTemplateEngine(new VelocityTemplateEngine(new VelocityEngine()));
        return templateEngine;
    }

    static Options getOptions() {
        return Options.builder()
                .resourceActions(
                        Options.ResourcePathPatterns.builder()
                                .ignore("VARIABLE\\.adoc")
                                .evaluateResourcePath("\\$")
                                .evaluateResourceContent("vm$")
                                .copyResourceContent(".*")
                                .build()
                )
                .build();
    }

}

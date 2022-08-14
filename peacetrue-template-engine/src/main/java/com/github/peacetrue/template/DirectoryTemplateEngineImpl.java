package com.github.peacetrue.template;

import com.github.peacetrue.io.Resource;
import com.github.peacetrue.io.ResourcesLoader;
import com.github.peacetrue.log.LoggerUtils;
import com.github.peacetrue.spring.beans.BeanUtils;
import com.github.peacetrue.util.FileUtils;
import com.github.peacetrue.util.MapUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 目录模板引擎实现。
 *
 * @author peace
 **/
@Slf4j
@Setter
public class DirectoryTemplateEngineImpl implements DirectoryTemplateEngine {

    private ResourcesLoader resourcesLoader;
    private ResultPathResolver resultPathResolver;
    private Options options;
    private TemplateEngine templateEngine;

    @Override
    public void evaluate(String templateLocation, Options options, Map<String, ?> variables, String resultPath) throws IOException {
        if (log.isInfoEnabled()) {
            log.info("evaluate template: {}", LoggerUtils.formatArgs(MapUtils.from(
                    new String[]{"templateLocation", "resultPath"},
                    new String[]{templateLocation, resultPath}
            )));
        }

        List<Resource> resources = resourcesLoader.getResources(templateLocation);
        log.debug("got resources.size: {}", resources.size());
        if (resources.isEmpty()) {
            log.warn("can't found any resources from location: {}, Please check if the templateLocation valid", templateLocation);
            return;
        }

        Options.ResourcePathPatterns resourceActions = options.getResourceActions();
        if (resourceActions == null) resourceActions = this.options.getResourceActions();
        else BeanUtils.setDefaults(resourceActions, this.options.getResourceActions());
        log.debug("got merged options: {}", options);

        Path resultPathObject = Paths.get(resultPath).normalize().toAbsolutePath();
        for (Resource resource : resources) {
            log.debug("handle resource {}", resource);
            String resourcePath = resource.getPath();

            if (matches(resourcePath, resourceActions.getIgnore(), "ignore")) {
                continue;
            }

            String resolvedResourcePath = resourcePath;
            if (matches(resourcePath, resourceActions.getEvaluateResourcePath(), "evaluateResourcePath")) {
                resolvedResourcePath = templateEngine.evaluate(resourcePath, variables);
                log.debug("resource path is evaluated to '{}'", resolvedResourcePath);
            }

            Path subResultPath = resultPathObject.resolve(resultPathResolver.resolve(resolvedResourcePath));
            log.debug("got result path '{}'", subResultPath);
            if (resource.isDirectory()) {
                if (Files.exists(subResultPath)) {
                    log.trace("skip exists result directory");
                } else {
                    //目录也必须处理，当目录下没有文件时，需要一个空目录
                    Files.createDirectories(subResultPath);
                    log.trace("created result directory");
                }
                continue;
            }

            if (matches(resourcePath, resourceActions.getEvaluateResourceContent(), "evaluateResourceContent")) {
                InputStream inputStream = Objects.requireNonNull(resource.getInputStream());
                FileUtils.createFileIfAbsent(subResultPath);
                try (InputStreamReader template = new InputStreamReader(inputStream);
                     Writer result = Files.newBufferedWriter(subResultPath)) {
                    templateEngine.evaluate(template, variables, result);
                    log.debug("resource content is evaluated to result");
                }
                continue;
            }

            if (matches(resourcePath, resourceActions.getCopyResourceContent(), "copyResourceContent")) {
                try (InputStream inputStream = Objects.requireNonNull(resource.getInputStream())) {
                    FileUtils.createFileIfAbsent(subResultPath);
                    Files.copy(inputStream, subResultPath, StandardCopyOption.REPLACE_EXISTING);
                    log.debug("resource content is copied to result");
                }
            } else {
                log.warn("resource '{}' is unspecified which is equivalent to being ignored", resourcePath);
            }
        }
    }

    private static boolean matches(String resourcePath, @Nullable String pattern, String action) {
        if (pattern == null) return false;
        boolean matches = Pattern.compile(pattern).matcher(resourcePath).find();
        if (matches) log.trace("resource is matched by {} pattern '{}'", action, pattern);
        return matches;
    }

}

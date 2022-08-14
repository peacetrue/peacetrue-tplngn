package com.github.peacetrue.io;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.List;

import static com.github.peacetrue.test.SourcePathUtils.classToPath;

/**
 * @author peace
 */
@Slf4j
class ClasspathResourcesLoaderTest {

    @Test
    void getResourcesFromClasspath() throws IOException {
        String location = classToPath(getClass(), false, true);
        log.info("location: {}", location);
        List<Resource> resources = ClasspathResourcesLoader.DEFAULT.getResources(location);
        log.info("resources.size: {}", resources.size());
        Assertions.assertFalse(resources.isEmpty());
    }

    @Test
    void getResourcesFromJar() throws IOException {
        String location = classToPath(Logger.class, false, true);
        log.info("location: {}", location);
        List<Resource> resources = ClasspathResourcesLoader.DEFAULT.getResources(location);
        log.info("resources.size: {}", resources.size());
        Assertions.assertFalse(resources.isEmpty());
    }

}

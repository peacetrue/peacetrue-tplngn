package com.github.peacetrue.io;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.github.peacetrue.io.ResourcesLoader.LOCATION_PREFIX_CLASSPATH;
import static com.github.peacetrue.io.ResourcesLoader.LOCATION_PREFIX_FILE;
import static com.github.peacetrue.test.SourcePathUtils.classToPath;
import static com.github.peacetrue.test.SourcePathUtils.getCustomAbsolutePath;

/**
 * @author peace
 **/
@Slf4j
class ConditionalResourcesLoaderTest {

    @Test
    void getResources() throws IOException {
        ConditionalResourcesLoader resourcesLoader = new ConditionalResourcesLoader();
        String location = classToPath(getClass(), false, true);
        log.info("location: {}", location);
        Assertions.assertThrows(UnsupportedOperationException.class, () -> resourcesLoader.getResources(location));

        List<Resource> resources = resourcesLoader.getResources(LOCATION_PREFIX_CLASSPATH + location);
        log.info("resources.size: {}", resources.size());
        Assertions.assertFalse(resources.isEmpty());

        String fileLocation = getCustomAbsolutePath(true, false, File.separator + location);
        log.info("fileLocation: {}", fileLocation);
        resources = resourcesLoader.getResources(LOCATION_PREFIX_FILE + fileLocation);
        log.info("resources.size: {}", resources.size());
        Assertions.assertFalse(resources.isEmpty());
    }
}

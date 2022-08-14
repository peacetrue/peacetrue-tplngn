package com.github.peacetrue.io;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static com.github.peacetrue.io.ResourcesUtilsTest.templatesDirPath;

/**
 * @author peace
 */
@Slf4j
class FileSystemResourcesLoaderTest {

    @Test
    void emptyWhenLocationAbsent() throws IOException {
        String location = templatesDirPath.resolve("antora").toString();
        Assertions.assertTrue(FileSystemResourcesLoader.DEFAULT.getResources(location + "/not-exists").isEmpty());
    }

    @Test
    void unsupportedOperation() {
        Assertions.assertThrows(UnsupportedOperationException.class, () -> getResources("antora.tar"));
    }

    @Test
    void getResources() throws IOException {
        getResources("antora");
        getResources("antora.zip");
        getResources("antora.jar");
    }

    private void getResources(String templateName) throws IOException {
        String location = templatesDirPath.resolve(templateName).toString();
        List<Resource> resources = FileSystemResourcesLoader.DEFAULT.getResources(location);
        Assertions.assertFalse(resources.isEmpty());
    }
}

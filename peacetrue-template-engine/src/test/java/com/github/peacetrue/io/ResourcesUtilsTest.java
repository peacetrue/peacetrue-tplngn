package com.github.peacetrue.io;

import com.github.peacetrue.test.SourcePathUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author peace
 **/
@Slf4j
public class ResourcesUtilsTest {

    public static final Path templatesDirPath = Paths.get(SourcePathUtils.getProjectAbsolutePath() + "/../docs/antora/modules/ROOT/examples/templates");

    @Test
    void getDirectoryResources() throws IOException {
        Path antoraPath = templatesDirPath.resolve("antora");
        log.info("antoraPath: {}", antoraPath);
        Assertions.assertTrue(Files.exists(antoraPath));
        List<Resource> resources = ResourcesUtils.getDirectoryResources(antoraPath);
        Assertions.assertFalse(resources.isEmpty());
    }

    @Test
    void getZipResources() throws IOException {
        Path zipPath = templatesDirPath.resolve("antora.zip");
        log.info("zipPath: {}", zipPath);
        Assertions.assertTrue(Files.exists(zipPath));
        List<Resource> resources = ResourcesUtils.getZipResources(zipPath.toString(), "antora/");
        Assertions.assertFalse(resources.isEmpty());
    }

    @Test
    void getJarResources() throws IOException {
        Path jarPath = templatesDirPath.resolve("antora.jar");
        log.info("jarPath: {}", jarPath);
        Assertions.assertTrue(Files.exists(jarPath));
        List<Resource> resources = ResourcesUtils.getJarResources(jarPath.toString(), "antora/");
        Assertions.assertFalse(resources.isEmpty());
    }
}

package com.github.peacetrue.io;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

/**
 * 文件系统资源集合加载器。
 *
 * @author peace
 */
@Slf4j
public class FileSystemResourcesLoader implements ResourcesLoader {

    /** 默认文件系统资源集合加载器 */
    public static final FileSystemResourcesLoader DEFAULT = new FileSystemResourcesLoader();

    private FileSystemResourcesLoader() {
    }

    @Override
    public List<Resource> getResources(String location) throws IOException {
        log.info("get resources from location: {}", location);
        Path path = Paths.get(location).normalize().toAbsolutePath();
        log.debug("got location absolute path: {}", path);
        if (Files.notExists(path)) return Collections.emptyList();

        if (Files.isDirectory(path)) {
            return ResourcesUtils.getDirectoryResources(path);
        }

        String stringPath = path.toString();
        if (stringPath.endsWith(".zip")) {
            return ResourcesUtils.getZipResources(stringPath, FilenameUtils.getBaseName(stringPath));
        }

        if (stringPath.endsWith(".jar")) {
            return ResourcesUtils.getJarResources(stringPath, FilenameUtils.getBaseName(stringPath));
        }

        throw new UnsupportedOperationException(String.format(
                "Unsupported file path '%s', must be directory, zip or jar", stringPath
        ));
    }

}

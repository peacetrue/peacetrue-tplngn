package com.github.peacetrue.io;

import lombok.Setter;

import java.io.IOException;
import java.util.List;

/**
 * 条件性的资源集合加载器。
 *
 * @author peace
 **/
@Setter
public class ConditionalResourcesLoader implements ResourcesLoader {

    /** 默认加载器 */
    public static final ConditionalResourcesLoader DEFAULT = new ConditionalResourcesLoader();

    private final FileSystemResourcesLoader fileSystemResourcesLoader = FileSystemResourcesLoader.DEFAULT;
    private ClasspathResourcesLoader classpathResourcesLoader = ClasspathResourcesLoader.DEFAULT;

    @Override
    public List<Resource> getResources(String location) throws IOException {
        if (location.startsWith(LOCATION_PREFIX_FILE)) {
            return fileSystemResourcesLoader.getResources(location.substring(LOCATION_PREFIX_FILE.length()));
        }
        if (location.startsWith(LOCATION_PREFIX_CLASSPATH)) {
            return classpathResourcesLoader.getResources(location.substring(LOCATION_PREFIX_CLASSPATH.length()));
        }
        throw new UnsupportedOperationException(String.format("Unsupported location %s, prefix must be '%s' or '%s'",
                location, LOCATION_PREFIX_FILE, LOCATION_PREFIX_CLASSPATH
        ));
    }

}

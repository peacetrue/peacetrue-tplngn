package com.github.peacetrue.io;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * 类路径资源集合加载器。
 *
 * @author peace
 */
@Slf4j
public class ClasspathResourcesLoader implements ResourcesLoader {

    /** 使用加载当前类的类加载器 */
    public static final ClasspathResourcesLoader DEFAULT = new ClasspathResourcesLoader();

    private final ClassLoader classLoader;

    private ClasspathResourcesLoader() {
        this(ClasspathResourcesLoader.class.getClassLoader());
    }

    /**
     * 指定一个类加载器。
     *
     * @param classLoader 类加载器
     */
    public ClasspathResourcesLoader(ClassLoader classLoader) {
        this.classLoader = Objects.requireNonNull(classLoader);
    }

    /**
     * 获取类路径资源集合。
     *
     * @param location 类路径，格式为：a/b/c
     * @return 类路径资源集合
     * @throws IOException 获取资源时发生读取异常
     */
    @Override
    public List<Resource> getResources(String location) throws IOException {
        log.info("get classpath resources from location: {}", location);
        Enumeration<URL> urls = this.classLoader.getResources(location);
        log.debug("got resource urls: {}", urls);
        List<Resource> resources = new LinkedList<>();
        int index = 0;
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            log.debug("got resource url({}): {}", index++, url);
            String absolutePath = url.getFile();
            log.debug("got resource absolute path: {}", absolutePath);
            if (absolutePath.startsWith(LOCATION_PREFIX_FILE)) {
                // file:/Users/xiayx/.m2/repository/ch/qos/logback/logback-classic/1.2.3/logback-classic-1.2.3.jar!/org/slf4j
                absolutePath = absolutePath.substring(LOCATION_PREFIX_FILE.length());
                // /Users/xiayx/.m2/repository/ch/qos/logback/logback-classic/1.2.3/logback-classic-1.2.3.jar!/org/slf4j
                String jarPath = absolutePath.substring(0, absolutePath.length() - location.length() - 2);
                // /Users/xiayx/.m2/repository/ch/qos/logback/logback-classic/1.2.3/logback-classic-1.2.3.jar
                log.debug("got jar path: {}", jarPath);
                resources.addAll(ResourcesUtils.getJarResources(jarPath, location));
            } else {
                resources.addAll(FileSystemResourcesLoader.DEFAULT.getResources(absolutePath));
            }
        }
        return resources;
    }


}

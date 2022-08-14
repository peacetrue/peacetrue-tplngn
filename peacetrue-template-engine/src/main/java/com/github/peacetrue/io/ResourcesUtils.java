package com.github.peacetrue.io;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 资源工具类。
 *
 * @author peace
 **/
@Slf4j
public class ResourcesUtils {

    private ResourcesUtils() {
    }

    /**
     * 获取目录下资源。
     *
     * @param dirPath 目录路径
     * @return 资源集合
     * @throws IOException                       获取资源时发生读取异常
     * @throws java.nio.file.NoSuchFileException 目录不存在时，抛出此异常
     */
    public static List<Resource> getDirectoryResources(Path dirPath) throws IOException {
        log.info("get resources in directory '{}'", dirPath);
        List<Resource> resources = new LinkedList<>();
        Files.walkFileTree(dirPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                ResourceImpl resource = new ResourceImpl(dirPath.relativize(dir).toString());
                log.debug("got resource: {}", resource);
                resources.add(resource);
                return super.preVisitDirectory(dir, attrs);
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                ResourceImpl resource = new ResourceImpl(dirPath.relativize(file).toString(), Files.newInputStream(file));
                log.debug("got resource: {}", resource);
                resources.add(resource);
                return super.visitFile(file, attrs);
            }
        });
        return resources;
    }

    private static void handleEntry(List<Resource> resources, String prefixPath,
                                    ZipFile zipFile, ZipEntry zipEntry) throws IOException {
        log.debug("got resource entry {}", zipEntry);
        // dir : antora/ ，目录以 / 结尾
        // file: antora/LICENSE，文件不以 / 结尾
        String name = zipEntry.getName();
        if (!name.startsWith(prefixPath)) {
            log.trace("skip resource not starts with {}", prefixPath);
            return;
        }

        ResourceImpl resource = new ResourceImpl(
                // antora/ - antora/ =
                // antora/docs - antora/ = docs/
                // antora/LICENSE - antora/ = LICENSE
                name.substring(prefixPath.length()),
                zipEntry.isDirectory() ? null : zipFile.getInputStream(zipEntry)
        );
        log.debug("got resource: {}", resource);
        resources.add(resource);
    }

    /**
     * 从 zip 包中，获取路径以指定前缀起始的资源。
     *
     * @param zipPath    zip 包路径
     * @param prefixPath 前缀路径
     * @return 资源集合
     * @throws IOException 获取资源时发生读取异常
     */
    public static List<Resource> getZipResources(String zipPath, String prefixPath) throws IOException {
        log.info("get resources start with '{}' in zip '{}'", prefixPath, zipPath);
        if (!prefixPath.endsWith(File.separator)) prefixPath += File.separator;
        List<Resource> resources = new LinkedList<>();
        ZipFile zipFile = new ZipFile(zipPath);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = entries.nextElement();
            handleEntry(resources, prefixPath, zipFile, zipEntry);
        }
        return resources;
    }

    /**
     * 从 jar 包中，获取路径以指定前缀起始的资源。
     *
     * @param jarPath    jar 包路径
     * @param prefixPath 前缀路径
     * @return 资源集合
     * @throws IOException 获取资源时发生读取异常
     */
    public static List<Resource> getJarResources(String jarPath, String prefixPath) throws IOException {
        log.info("get resources start with '{}' in jar '{}'", prefixPath, jarPath);
        if (!prefixPath.endsWith(File.separator)) prefixPath += File.separator;
        List<Resource> resources = new LinkedList<>();
        JarFile jarFile = new JarFile(jarPath);
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            handleEntry(resources, prefixPath, jarFile, jarEntry);
        }
        return resources;
    }
}

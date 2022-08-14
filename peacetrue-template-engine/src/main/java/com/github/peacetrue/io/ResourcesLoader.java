package com.github.peacetrue.io;

import java.io.IOException;
import java.util.List;

/**
 * 资源集合加载器，获取指定位置下所有资源，区别于单个文件的资源加载器。
 *
 * @author peace
 */
public interface ResourcesLoader {

    /** 文件路径位置前缀 */
    String LOCATION_PREFIX_FILE = "file:";
    /** 类路径位置前缀 */
    String LOCATION_PREFIX_CLASSPATH = "classpath:";

    /**
     * 获取指定位置下所有资源。
     *
     * @param location 指定位置，可以使用位置前缀，例如 classpath:com/github
     * @return 资源集合
     * @throws IOException 获取资源时发生读取异常
     */
    List<Resource> getResources(String location) throws IOException;

}

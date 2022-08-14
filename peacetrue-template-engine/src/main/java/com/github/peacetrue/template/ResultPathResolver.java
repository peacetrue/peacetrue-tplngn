package com.github.peacetrue.template;

/**
 * 资源路径解析器。
 *
 * @author peace
 **/
@FunctionalInterface
public interface ResultPathResolver {

    /**
     * 解析资源路径，得到结果路径。
     *
     * @param resourcePath 资源路径
     * @return 结果路径
     */
    String resolve(String resourcePath);

}

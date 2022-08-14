package com.github.peacetrue.template;

/**
 * 模板断言。
 *
 * @author peace
 **/
@FunctionalInterface
public interface TemplatePredicate {

    /**
     * 判断资源是否模板。
     *
     * @param resourcePath 资源路径
     * @return {@code true} 如果资源是模板，否则 {@code false}
     */
    boolean isTemplate(String resourcePath);

}

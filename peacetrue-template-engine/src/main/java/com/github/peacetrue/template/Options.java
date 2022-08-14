package com.github.peacetrue.template;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 目录模板引擎渲染选项。
 * <p>
 * 使用 Java 正则表达式，根据资源路径，采取局部匹配模式，筛选资源。（局部匹配|全文匹配）
 * 对资源路径有 2 种处理方式：不变、渲染。
 * 对资源内容有 3 种处理方式：忽略、渲染、拷贝
 *
 * @author peace
 * @see DirectoryTemplateEngine
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Options {

    /** 资源处理规则。 */
    private ResourcePathPatterns resourceActions;

    /** 使用 Java 正则表达式，根据资源路径，采取局部匹配模式，筛选资源，执行不同的处理逻辑。 */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResourcePathPatterns {
        /** 忽略。 */
        private String ignore;
        /** 渲染资源路径。 */
        private String evaluateResourcePath;
        /** 渲染资源内容。 */
        private String evaluateResourceContent;
        /** 拷贝资源内容。 */
        private String copyResourceContent;

    }

}

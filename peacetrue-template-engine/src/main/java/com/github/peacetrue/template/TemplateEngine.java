package com.github.peacetrue.template;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

/**
 * 模板引擎接口，用于避免直接依赖具体模板引擎。
 *
 * @author peace
 * @see <a href="https://velocity.apache.org/">Velocity</a>
 * @see <a href="https://freemarker.apache.org/">FreeMarker</a>
 **/
public interface TemplateEngine {

    /**
     * 使用变量渲染文本内容模板。
     *
     * @param template  文本内容模板
     * @param variables 模板变量
     * @return 文本内容结果
     */
    default String evaluate(String template, Map<String, ?> variables) {
        StringWriter result = new StringWriter();
        this.evaluate(new StringReader(template), variables, result);
        return result.toString();
    }

    /**
     * 使用变量渲染输入流模板。
     *
     * @param template  输入流模板
     * @param variables 模板变量
     * @param result    输出流结果
     */
    void evaluate(Reader template, Map<String, ?> variables, Writer result);

}

package com.github.peacetrue.template;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

/**
 * 目录模板引擎。模板引擎用于渲染单个文件，目录模板引擎用于渲染目录下多个文件。
 *
 * @author peace
 * @see TemplateEngine
 **/
public interface DirectoryTemplateEngine {

    /**
     * 使用变量渲染目录模板。
     *
     * @param templateLocation 目录模板位置
     * @param options          选项
     * @param variables        变量
     * @param resultPath       结果路径
     * @throws IOException 渲染过程中发生读写异常
     */
    void evaluate(String templateLocation, Options options, Map<String, ?> variables, String resultPath) throws IOException;

//    /**
//     * 使用模板变量渲染压缩文件输入流模板。
//     *
//     * @param zipTemplate 压缩文件输入流模板
//     * @param options     渲染选项
//     * @param variables   模板变量
//     * @param zipResult   压缩文件输出流结果
//     * @throws IOException 渲染过程中发生读写异常
//     */
//    void evaluate(Reader zipTemplate, Options options, Map<String, ?> variables, Writer zipResult) throws IOException;

}

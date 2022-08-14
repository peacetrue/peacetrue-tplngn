package com.github.peacetrue.log;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 日志工具类。
 *
 * @author peace
 **/
public class LoggerUtils {

    private LoggerUtils() {
    }

    /**
     * 格式化方法参数。
     * <p>
     * 展示效果如下：
     * <pre>
     * formatArgs:
     * 	    templateLocation : file:template
     * 	    optionsLocation  : file:antora-options.properties
     * 	    variablesLocation: file:antora-variables.properties
     * 	    resultPath       : result
     * </pre>
     *
     * @param args 方法参数。key 为参数名，value 为参数值
     * @return 方法参数的字符串表示
     */
    public static String formatArgs(Map<String, ?> args) {
        int largestKeyLength = args.keySet().stream()
                .max(Comparator.comparing(String::length))
                .orElse("")
                .length();
        return args.entrySet().stream()
                .map(entry -> String.format("%s: %s", StringUtils.rightPad(entry.getKey(), largestKeyLength), entry.getValue()))
                .collect(Collectors.joining("\n\t", "\n\t", ""));
    }

}

package com.github.peacetrue.log;

import com.github.peacetrue.util.MapUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author peace
 **/
@Slf4j
class LoggerUtilsTest {

    @Test
    void formatArgs() {
        log.info("formatArgs: {}", LoggerUtils.formatArgs(MapUtils.from(
                new String[]{"templateLocation", "optionsLocation", "variablesLocation", "resultPath"},
                new String[]{"file:template", "file:antora-options.properties", "file:antora-variables.properties", "result"}
        )));
    }
}

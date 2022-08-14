package com.github.peacetrue.template;

import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.app.VelocityEngine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

/**
 * @author peace
 **/
@Slf4j
class VelocityTemplateEngineTest {

    @Test
    void evaluate() {
        VelocityTemplateEngine templateEngine = new VelocityTemplateEngine(new VelocityEngine());
        String result = templateEngine.evaluate("$esc.d", Collections.emptyMap());
        Assertions.assertEquals("$", result);
    }

}

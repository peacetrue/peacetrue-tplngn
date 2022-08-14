package com.github.peacetrue.template;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

/**
 * @author peace
 **/
class OptionsTest {

    @Test
    void wholeMatch() {
        Assertions.assertTrue("spring-shell.log".matches(".*\\.log"), "全词匹配");
        Assertions.assertTrue(Pattern.compile(".*\\.log").matcher("spring-shell.log").matches(), "全词匹配");
        Assertions.assertFalse("spring-shell.log".matches("log$"), "全词匹配");
    }

    @Test
    void partialMatch() {
        Assertions.assertTrue(Pattern.compile("").matcher("spring-shell.log").find(), "局部匹配");
        Assertions.assertTrue(Pattern.compile(".*\\.log").matcher("spring-shell.log").find(), "局部匹配");
        Assertions.assertTrue(Pattern.compile("log$").matcher("spring-shell.log").find(), "局部匹配");
        Assertions.assertFalse(Pattern.compile("^log$").matcher("spring-shell.log").find(), "局部匹配");
    }

}

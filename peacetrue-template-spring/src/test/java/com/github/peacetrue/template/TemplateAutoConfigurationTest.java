package com.github.peacetrue.template;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author peace
 **/
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {
        TemplateAutoConfiguration.class
})
class TemplateAutoConfigurationTest {

    @Autowired
    private DirectoryTemplateEngine directoryTemplateEngine;

    @Test
    void directoryTemplateEngine() {
        Assertions.assertNotNull(directoryTemplateEngine);
    }
}

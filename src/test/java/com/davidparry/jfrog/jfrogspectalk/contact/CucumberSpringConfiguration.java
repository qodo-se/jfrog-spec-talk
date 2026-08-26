package com.davidparry.jfrog.jfrogspectalk.contact;

import com.davidparry.jfrog.jfrogspectalk.JfrogSpecTalkApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = JfrogSpecTalkApplication.class)
@AutoConfigureMockMvc
public class CucumberSpringConfiguration {
}

package com.emusicstore;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource(locations = { "file:src/main/webapp/WEB-INF/applicationContext.xml" })
class TestConfig {

}
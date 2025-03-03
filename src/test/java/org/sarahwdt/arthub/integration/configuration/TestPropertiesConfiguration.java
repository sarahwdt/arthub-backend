package org.sarahwdt.arthub.integration.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestPropertiesConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "app.test")
    public TestProperties testProperties() {
        return new TestProperties();
    }
}

package org.sarahwdt.arthub.configuration;

import org.sarahwdt.arthub.configuration.property.Jwt;
import org.sarahwdt.arthub.configuration.property.Registration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
public class PropertiesConfiguration {
    @Bean
    @ConfigurationProperties("app.security.jwt")
    public Jwt jwt() {
        return new Jwt();
    }

    @Bean
    @ConfigurationProperties("app.registration")
    public Registration registration() {
        return new Registration();
    }
}

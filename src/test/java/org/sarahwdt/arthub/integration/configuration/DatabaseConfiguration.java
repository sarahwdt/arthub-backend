package org.sarahwdt.arthub.integration.configuration;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ActiveProfiles("test-flyway")
@TestConfiguration(proxyBeanMethods = false)
public class DatabaseConfiguration {

    @Autowired
    private Flyway flyway;

    @BeforeEach
    public void cleanUp() {
        flyway.clean();
        flyway.migrate();
    }
}

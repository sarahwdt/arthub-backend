package org.sarahwdt.arthub.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.internal.mapping.Jackson2Mapper;
import io.restassured.mapper.ObjectMapper;
import io.restassured.path.json.mapper.factory.DefaultJackson2ObjectMapperFactory;
import io.restassured.specification.RequestSpecification;
import org.sarahwdt.arthub.configuration.property.Jwt;
import org.sarahwdt.arthub.configuration.property.Registration;
import org.sarahwdt.arthub.configuration.security.SecurityConfiguration;
import org.sarahwdt.arthub.dto.JwtPrincipal;
import org.sarahwdt.arthub.integration.configuration.DatabaseConfiguration;
import org.sarahwdt.arthub.integration.configuration.TestProperties;
import org.sarahwdt.arthub.model.user.Privilege;
import org.sarahwdt.arthub.repository.RefreshTokenRepository;
import org.sarahwdt.arthub.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@ActiveProfiles("test-flyway")
@Import(DatabaseConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractE2eTest {
    private final static String BASE_URI = "http://localhost";

    @LocalServerPort
    private int port;

    @Value("${spring.data.rest.base-path}")
    private String baseRestPath;

    @Value("${server.servlet.context-path}")
    private String basePath;

    @Autowired
    private TestProperties testProperties;

    @Autowired
    protected RefreshTokenRepository refreshTokenRepository;

    @Autowired
    protected JwtService jwtService;

    @Autowired
    protected Jwt jwtProperties;

    @Autowired
    protected Registration registrationProperties;

    protected final static ObjectMapper objectMapper = new Jackson2Mapper(new DefaultJackson2ObjectMapperFactory());

    protected RequestSpecification request() {
        return RestAssured.given()
                .port(port)
                .baseUri(BASE_URI)
                .basePath(basePath)
                .contentType(ContentType.JSON);
    }

    protected CharSequence authAs(Privilege... privileges) {
        Set<Privilege> privilegeSet = new HashSet<>(Arrays.asList(privileges));
        return jwtService.generateToken(new JwtPrincipal(1, getUser(), privilegeSet));
    }

    protected RequestSpecification authenticated(Privilege... privileges) {
        return request().header(HttpHeaders.AUTHORIZATION, SecurityConfiguration.AUTH_HEADER_PREFIX + authAs(privileges));
    }

    protected RequestSpecification authenticated() {
        return authenticated(Privilege.values());
    }

    protected String getUser() {
        return testProperties.getUser();
    }

    protected String getEmail() {
        return testProperties.getEmail();
    }

    protected String getPassword() {
        return testProperties.getPassword();
    }

    protected String getRole() {
        return testProperties.getRole();
    }
}

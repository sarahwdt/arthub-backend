package org.sarahwdt.arthub.integration;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.sarahwdt.arthub.configuration.security.SecurityConfiguration;
import org.sarahwdt.arthub.dto.ChangePasswordRequest;
import org.sarahwdt.arthub.dto.LoginRequest;
import org.sarahwdt.arthub.dto.RegistrationRequest;
import org.springframework.http.HttpHeaders;

public class CurrentUserTest extends AbstractE2eTest {

    @Test
    void Should_Return() {
        RegistrationRequest request = new RegistrationRequest("emai@mail.com",
                "Password!1".toCharArray(), "Password!1".toCharArray());
        request()
                .body(request, objectMapper)
                .post("/auth/registration")
                .then()
                .statusCode(201)
                .assertThat()
                .body("email", Matchers.equalTo("emai@mail.com"),
                        "role.name", Matchers.equalTo(registrationProperties.getRole()),
                        "role.authorities", Matchers.containsInAnyOrder(registrationProperties.getPrivileges().toArray()));
    }

    @Test
    void Should_ChangePassword() {
        LoginRequest loginRequest = new LoginRequest(getEmail(), getPassword().toCharArray());
        String accessToken = request()
                .body(loginRequest, objectMapper)
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract().path("accessToken");

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(getPassword().toCharArray(),
                "Password!1".toCharArray(), "Password!1".toCharArray());
        request()
                .body(changePasswordRequest, objectMapper)
                .header(HttpHeaders.AUTHORIZATION, SecurityConfiguration.AUTH_HEADER_PREFIX + accessToken)
                .post("/change-password")
                .then()
                .statusCode(200);

        loginRequest = new LoginRequest(getEmail(), "Password!1".toCharArray());
        request()
                .body(loginRequest, objectMapper)
                .post("/auth/login")
                .then()
                .statusCode(200);
    }
}

package org.sarahwdt.arthub.integration;

import io.jsonwebtoken.JwtException;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.sarahwdt.arthub.dto.JwtPrincipal;
import org.sarahwdt.arthub.dto.LoginRequest;
import org.sarahwdt.arthub.dto.RefreshRequest;

import java.time.Duration;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class AuthTest extends AbstractE2eTest {

    @Test
    void Should_ReceiveValidAccessTokenResponse_When_LoginSuccessfully() {
        Object request = new LoginRequest(getUser(), getPassword().toCharArray());
        ValidatableResponse response = request()
                .body(request, objectMapper)
                .post("/auth/login")
                .then()
                .statusCode(200);

        String accessToken = response.extract().path("accessToken");
        assertThat(accessToken).isNotNull();
        try {
            JwtPrincipal jwtPrincipal = jwtService.extractValidPrincipal(accessToken);
            assertThat(jwtPrincipal.id()).isEqualTo(1);
            assertThat(jwtPrincipal.username()).isEqualTo(getUser());
            assertThat(jwtPrincipal.authorities()).isEmpty();
        } catch (JwtException e) {
            fail("Invalid JWT token", e);
        }

        String refreshTokenStr = response.extract().path("refreshToken");
        assertThat(refreshTokenStr).isNotNull();

        try {
            UUID refreshToken = UUID.fromString(refreshTokenStr);
            refreshTokenRepository.findWithUserWithRoleByToken(refreshToken)
                    .orElseGet(() -> fail("Refresh token not found"));
        } catch (IllegalArgumentException e) {
            fail("Invalid refresh token", e);
        }

        Integer expiresIn = response.extract().path("expiresIn");
        assertThat(expiresIn).isNotNull();
        Duration duration = Duration.ofSeconds(expiresIn);
        assertThat(duration).isEqualTo(jwtProperties.getExpiration());
    }

    @Test
    @Disabled
    void Should_Receive403_When_LoginWithInvalidCredentials() {
        Object request = new LoginRequest("invalid", "invalid".toCharArray());
        request()
                .body(request, objectMapper)
                .post("/auth/login")
                .then()
                .statusCode(403);
    }

    @Test
    void Should_ReceiveNewAccessToken_When_RefreshSuccessful() throws InterruptedException {
        Object request = new LoginRequest(getUser(), getPassword().toCharArray());
        ValidatableResponse response = request()
                .body(request, objectMapper)
                .post("/auth/login")
                .then()
                .statusCode(200);

        String refreshToken = response.extract().path("refreshToken");
        String accessToken = response.extract().path("accessToken");

        Thread.sleep(Duration.ofMillis(100));
        RefreshRequest refreshRequest = new RefreshRequest(UUID.fromString(refreshToken));
        ValidatableResponse refreshResponse = request()
                .body(refreshRequest, objectMapper)
                .post("/auth/refresh")
                .then()
                .statusCode(200);

        String newAccessToken = refreshResponse.extract().path("accessToken");
        String newRefreshToken = refreshResponse.extract().path("refreshToken");
        assertThat(refreshToken).isEqualTo(newRefreshToken);
        assertThat(accessToken).isNotEqualTo(newAccessToken);

        assertAssertTokenResponse(refreshResponse);
    }

    private void assertAssertTokenResponse(ValidatableResponse response) {
        String accessToken = response.extract().path("accessToken");
        assertThat(accessToken).isNotNull();
        try {
            JwtPrincipal jwtPrincipal = jwtService.extractValidPrincipal(accessToken);
            assertThat(jwtPrincipal.id()).isEqualTo(1);
            assertThat(jwtPrincipal.username()).isEqualTo(getUser());
            assertThat(jwtPrincipal.authorities()).isEmpty();
        } catch (JwtException e) {
            fail("Invalid JWT token", e);
        }

        String refreshTokenStr = response.extract().path("refreshToken");
        assertThat(refreshTokenStr).isNotNull();

        try {
            UUID refreshToken = UUID.fromString(refreshTokenStr);
            refreshTokenRepository.findWithUserWithRoleByToken(refreshToken)
                    .orElseGet(() -> fail("Refresh token not found"));
        } catch (IllegalArgumentException e) {
            fail("Invalid refresh token", e);
        }

        Integer expiresIn = response.extract().path("expiresIn");
        assertThat(expiresIn).isNotNull();
        Duration duration = Duration.ofSeconds(expiresIn);
        assertThat(duration).isEqualTo(jwtProperties.getExpiration());
    }
}

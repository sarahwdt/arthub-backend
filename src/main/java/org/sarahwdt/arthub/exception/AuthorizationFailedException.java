package org.sarahwdt.arthub.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class AuthorizationFailedException extends BusinessLogicException {
    private AuthorizationFailedException() {
    }

    private static class RefreshTokenIsExpired extends AuthorizationFailedException {

    }

    private static class RefreshTokenNotFound extends AuthorizationFailedException {

    }

    @Override
    protected String messageCodeCategory() {
        return "authorization";
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.UNAUTHORIZED;
    }

    public static AuthorizationFailedException refreshTokenIsExpired() {
        return new RefreshTokenIsExpired();
    }

    public static AuthorizationFailedException refreshTokenNotFound() {
        return new RefreshTokenNotFound();
    }
}

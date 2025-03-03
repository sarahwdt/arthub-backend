package org.sarahwdt.arthub.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class AuthorizationFailedException extends BusinessLogicException {
    private AuthorizationFailedException() {
    }

    @AllArgsConstructor
    private static class UserNotFound extends AuthorizationFailedException {
        private String username;

        @Override
        public Object[] getDetailMessageArguments() {
            return new Object[]{username};
        }
    }

    private static class PasswordIsIncorrect extends AuthorizationFailedException {
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

    public static AuthorizationFailedException userNotFound(String username) {
        return new UserNotFound(username);
    }

    public static AuthorizationFailedException passwordIsIncorrect() {
        return new PasswordIsIncorrect();
    }

    public static AuthorizationFailedException refreshTokenNotFound() {
        return new RefreshTokenNotFound();
    }
}

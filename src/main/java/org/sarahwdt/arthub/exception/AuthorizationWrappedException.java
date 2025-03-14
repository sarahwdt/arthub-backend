package org.sarahwdt.arthub.exception;

import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;

@Getter
public class AuthorizationWrappedException extends BusinessLogicException {
    private final HttpStatus httpStatus;

    private final Exception wrappedException;

    private AuthorizationWrappedException(Exception wrappedException, HttpStatus httpStatus) {
        this.wrappedException = wrappedException;
        this.httpStatus = httpStatus;
    }

    @Override
    protected String messageCodeCategory() {
        return "authorization.wrapped";
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return httpStatus;
    }

    public static AuthorizationWrappedException wrap(AuthenticationException wrappedException) {
        return new AuthorizationWrappedException(wrappedException, HttpStatus.UNAUTHORIZED);
    }

    public static AuthorizationWrappedException wrap(AccessDeniedException wrappedException) {
        return new AuthorizationWrappedException(wrappedException, HttpStatus.FORBIDDEN);
    }
}

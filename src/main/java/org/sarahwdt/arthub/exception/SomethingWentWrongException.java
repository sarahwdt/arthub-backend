package org.sarahwdt.arthub.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class SomethingWentWrongException extends BusinessLogicException {

    private SomethingWentWrongException() {}

    private static class NewTokenRequired extends SomethingWentWrongException {}

    @Override
    protected String messageCodeCategory() {
        return "unexpected";
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public static SomethingWentWrongException newTokenRequired() {
        return new NewTokenRequired();
    }
}

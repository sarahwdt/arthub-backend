package org.sarahwdt.arthub.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.validation.BindingResult;


@Getter
@AllArgsConstructor
public class ValidationException extends BusinessLogicException {
    private final BindingResult bindingResult;

    @Override
    protected String messageCodeCategory() {
        return "validation";
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.UNPROCESSABLE_ENTITY;
    }

    public static ValidationException create(BindingResult bindingResult) {
        return new ValidationException(bindingResult);
    }
}

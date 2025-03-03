package org.sarahwdt.arthub.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class ValidationException extends BusinessLogicException {
    private ValidationException() {
    }

    private static class PasswordsDoesntMatch extends ValidationException {
    }

    private static class OldPasswordIsIncorrect extends ValidationException {
    }

    @AllArgsConstructor
    private static class IncorrectLength extends ValidationException {
        private int min, max;

        @Override
        public Object[] getDetailMessageArguments() {
            return new Object[]{min, max};
        }
    }

    private static class MissingCapitalLetter extends ValidationException {
    }

    private static class MissingLowerLetter extends ValidationException {
    }

    private static class MissingDigit extends ValidationException {
    }

    private static class MissingSpecialCharacter extends ValidationException {
    }

    @Override
    protected String messageCodeCategory() {
        return "validation";
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.UNPROCESSABLE_ENTITY;
    }

    public static ValidationException passwordsDoesntMatch() {
        return new PasswordsDoesntMatch();
    }

    public static ValidationException oldPasswordIsIncorrect() {
        return new OldPasswordIsIncorrect();
    }

    public static ValidationException incorrectLength(int min, int max) {
        return new IncorrectLength(min, max);
    }

    public static ValidationException missingCapitalLetter() {
        return new MissingCapitalLetter();
    }

    public static ValidationException missingLowerLetter() {
        return new MissingLowerLetter();
    }

    public static ValidationException missingDigit() {
        return new MissingDigit();
    }

    public static ValidationException missingSpecialCharacter() {
        return new MissingSpecialCharacter();
    }
}

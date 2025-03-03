package org.sarahwdt.arthub.service;

import org.sarahwdt.arthub.exception.ValidationException;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.regex.Pattern;

@Service
public class PasswordValidationService {
    private final static int MIN_PASSWORD_LENGTH = 8;
    private final static int MAX_PASSWORD_LENGTH = 64;
    private final static Pattern SPECIAL_CHARACTERS = Pattern.compile("\\p{Punct}");
    private final static Pattern CAPITAL_LETTERS = Pattern.compile("\\p{Lu}");
    private final static Pattern LOWER_LETTERS = Pattern.compile("\\p{Ll}");
    private final static Pattern DIGITS = Pattern.compile("\\d");

    public void validatePassword(char[] password) {
        if (password.length < MIN_PASSWORD_LENGTH) {
            throw ValidationException.incorrectLength(MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH);
        }
        if (password.length > MAX_PASSWORD_LENGTH) {
            throw ValidationException.incorrectLength(MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH);
        }
        CharSequence charSequence = CharBuffer.wrap(password);
        if (!CAPITAL_LETTERS.matcher(charSequence).find()) {
            throw ValidationException.missingCapitalLetter();
        }
        if (!LOWER_LETTERS.matcher(charSequence).find()) {
            throw ValidationException.missingLowerLetter();
        }
        if (!DIGITS.matcher(charSequence).find()) {
            throw ValidationException.missingDigit();
        }
        if (!SPECIAL_CHARACTERS.matcher(charSequence).find()) {
            throw ValidationException.missingSpecialCharacter();
        }
    }
}

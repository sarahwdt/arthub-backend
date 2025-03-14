package org.sarahwdt.arthub.service;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.nio.CharBuffer;
import java.util.regex.Pattern;

import static org.sarahwdt.arthub.util.I18nPlaceholders.Validation.*;

@Service
public class PasswordValidationService {
    private final static int MIN_PASSWORD_LENGTH = 8;
    private final static int MAX_PASSWORD_LENGTH = 64;
    private final static Pattern SPECIAL_CHARACTERS = Pattern.compile("\\p{Punct}");
    private final static Pattern UPPER_LETTERS = Pattern.compile("\\p{Lu}");
    private final static Pattern LOWER_LETTERS = Pattern.compile("\\p{Ll}");
    private final static Pattern DIGITS = Pattern.compile("\\d");

    public void validatePassword(@Nullable char[] password, BindingResult bindingResult, String fieldName) {
        if (password == null) {
            return;
        }
        if (password.length < MIN_PASSWORD_LENGTH) {
            bindingResult.addError(new FieldError(
                    bindingResult.getObjectName(), fieldName, null, false,
                    new String[]{PASSWORDS_IS_TOO_SHORT}, new Object[]{MIN_PASSWORD_LENGTH}, null));
        }
        if (password.length > MAX_PASSWORD_LENGTH) {
            bindingResult.addError(new FieldError(
                    bindingResult.getObjectName(), fieldName, null, false,
                    new String[]{PASSWORDS_IS_TOO_LONG}, new Object[]{MAX_PASSWORD_LENGTH}, null));
        }
        CharSequence charSequence = CharBuffer.wrap(password);
        if (!UPPER_LETTERS.matcher(charSequence).find()) {
            bindingResult.addError(new FieldError(
                    bindingResult.getObjectName(), fieldName, null, false,
                    new String[]{PASSWORDS_HAS_NO_UPPERCASE_LETTERS}, null, null));
        }
        if (!LOWER_LETTERS.matcher(charSequence).find()) {
            bindingResult.addError(new FieldError(
                    bindingResult.getObjectName(), fieldName, null, false,
                    new String[]{PASSWORDS_HAS_NO_LOWERCASE_LETTERS}, null, null));
        }
        if (!DIGITS.matcher(charSequence).find()) {
            bindingResult.addError(new FieldError(
                    bindingResult.getObjectName(), fieldName, null, false,
                    new String[]{PASSWORDS_HAS_NO_DIGITS}, null, null));
        }
        if (!SPECIAL_CHARACTERS.matcher(charSequence).find()) {
            bindingResult.addError(new FieldError(
                    bindingResult.getObjectName(), fieldName, null, false,
                    new String[]{PASSWORDS_HAS_NO_SPECIAL_CHARACTERS}, null, null));
        }
    }
}

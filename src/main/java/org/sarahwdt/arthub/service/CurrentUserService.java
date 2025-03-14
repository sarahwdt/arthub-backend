package org.sarahwdt.arthub.service;

import lombok.RequiredArgsConstructor;
import org.sarahwdt.arthub.dto.*;
import org.sarahwdt.arthub.exception.AuthorizationWrappedException;
import org.sarahwdt.arthub.exception.SomethingWentWrongException;
import org.sarahwdt.arthub.exception.ValidationException;
import org.sarahwdt.arthub.model.user.Credentials;
import org.sarahwdt.arthub.model.user.Role;
import org.sarahwdt.arthub.model.user.User;
import org.sarahwdt.arthub.repository.CredentialsRepository;
import org.sarahwdt.arthub.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.nio.CharBuffer;
import java.util.Arrays;

import static org.sarahwdt.arthub.util.I18nPlaceholders.Validation.PASSWORDS_DOESNT_MATCH;
import static org.sarahwdt.arthub.util.I18nPlaceholders.Validation.PASSWORD_IS_INCORRECT;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final CredentialsRepository credentialsRepository;

    private final RoleService roleService;
    private final PasswordValidationService passwordValidationService;

    public void changePassword(ChangePasswordRequest request, BindingResult bindingResult) {
        if (!Arrays.equals(request.newPassword(), request.passwordConfirmation())) {
            bindingResult.addError(new FieldError(
                    bindingResult.getObjectName(), "passwordConfirmation", null, false,
                    new String[]{PASSWORDS_DOESNT_MATCH}, null, null));
        }

        passwordValidationService.validatePassword(request.newPassword(), bindingResult, "newPassword");
        // if old password is null further validations have no sense
        if (request.oldPassword() == null) {
            throw ValidationException.create(bindingResult);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication != null && authentication.getPrincipal() != null
                && authentication.getPrincipal() instanceof JwtPrincipal jwtPrincipal)) {
            throw AuthorizationWrappedException.wrap(new AccessDeniedException("Unauthorized"));
        }
        int id = jwtPrincipal.id();
        Credentials credentials = credentialsRepository.findById(id)
                .orElseThrow(SomethingWentWrongException::newTokenRequired);

        String currentPasswordHash = credentials.getPassword();
        CharSequence oldPassword = CharBuffer.wrap(request.oldPassword());

        if (!passwordEncoder.matches(oldPassword, currentPasswordHash)) {
            bindingResult.addError(new FieldError(
                    bindingResult.getObjectName(), "oldPassword", null, false,
                    new String[]{PASSWORD_IS_INCORRECT}, null, null));
        }

        if (bindingResult.hasErrors()) {
            throw ValidationException.create(bindingResult);
        }

        // check that new password is non-null
        if (request.newPassword() == null) {
            throw new IllegalStateException("New password is required, but newPassword is null");
        }

        String newPasswordHash = passwordEncoder.encode(CharBuffer.wrap(request.newPassword()));
        credentials.setPassword(newPasswordHash);
        credentialsRepository.save(credentials);
    }
}

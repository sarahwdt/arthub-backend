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

import java.nio.CharBuffer;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class CurrentUserService {
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final CredentialsRepository credentialsRepository;

    private final RoleService roleService;
    private final PasswordValidationService passwordValidationService;

    public void changePassword(ChangePasswordRequest request) {
        if (!Arrays.equals(request.newPassword(), request.confirmPassword())) {
            throw ValidationException.passwordsDoesntMatch();
        }

        passwordValidationService.validatePassword(request.newPassword());

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
            throw ValidationException.oldPasswordIsIncorrect();
        }

        String newPasswordHash = passwordEncoder.encode(CharBuffer.wrap(request.newPassword()));
        credentials.setPassword(newPasswordHash);
        credentialsRepository.save(credentials);
    }

    public UserResponse registration(RegistrationRequest request) {
        if (!Arrays.equals(request.password(), request.confirmPassword())) {
            throw ValidationException.passwordsDoesntMatch();
        }

        passwordValidationService.validatePassword(request.password());

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());

        Credentials credentials = new Credentials();
        credentials.setPassword(passwordEncoder.encode(CharBuffer.wrap(request.password())));
        credentials.setUser(user);
        user.setCredentials(credentials);

        Role role = roleService.getRoleForNewlyRegistered();
        user.setRole(role);

        user = userRepository.save(user);
        RoleResponse roleResponse = new RoleResponse(role.getName(), role.getAuthorities());
        return new UserResponse(user.getUsername(), user.getEmail(), roleResponse);
    }
}

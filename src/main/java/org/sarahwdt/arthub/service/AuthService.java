package org.sarahwdt.arthub.service;

import lombok.RequiredArgsConstructor;
import org.sarahwdt.arthub.configuration.property.Jwt;
import org.sarahwdt.arthub.dto.*;
import org.sarahwdt.arthub.exception.AuthorizationFailedException;
import org.sarahwdt.arthub.exception.ValidationException;
import org.sarahwdt.arthub.model.user.Credentials;
import org.sarahwdt.arthub.model.user.RefreshToken;
import org.sarahwdt.arthub.model.user.Role;
import org.sarahwdt.arthub.model.user.User;
import org.sarahwdt.arthub.repository.RefreshTokenRepository;
import org.sarahwdt.arthub.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.nio.CharBuffer;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.sarahwdt.arthub.util.I18nPlaceholders.Validation.*;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final Jwt properties;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    private final RoleService roleService;
    private final PasswordValidationService passwordValidationService;

    public UserResponse registration(RegistrationRequest request, BindingResult bindingResult) {
        if (!Arrays.equals(request.password(), request.passwordConfirmation())) {
            bindingResult.addError(new FieldError(
                    bindingResult.getObjectName(), "passwordConfirmation", null, false,
                    new String[]{PASSWORDS_DOESNT_MATCH}, null, null));
        }

        passwordValidationService.validatePassword(request.password(), bindingResult, "password");

        if (bindingResult.hasErrors()) {
            throw ValidationException.create(bindingResult);
        }

        User user = new User();
        user.setEmail(request.email());

        Credentials credentials = new Credentials();
        credentials.setPassword(passwordEncoder.encode(CharBuffer.wrap(request.password())));
        credentials.setUser(user);
        user.setCredentials(credentials);

        Role role = roleService.getRoleForNewlyRegistered();
        user.setRole(role);

        user = userRepository.save(user);
        RoleResponse roleResponse = new RoleResponse(role.getName(), role.getAuthorities());
        return new UserResponse(user.getEmail(), roleResponse);
    }

    public AccessTokenResponse login(LoginRequest request, BindingResult bindingResult) {
        try {
            Optional<User> userOpt = userRepository.findWithRoleAndCredentialsByEmail(request.email());
            if (userOpt.isEmpty()) {
                bindingResult.addError(new FieldError(
                        bindingResult.getObjectName(), "email", request.email(), false,
                        new String[]{USER_NOT_FOUND}, new Object[]{request.email()}, null));
                throw ValidationException.create(bindingResult);
            }
            User user = userOpt.get();

            String password = user.getCredentials().getPassword();
            CharBuffer challenger = CharBuffer.wrap(request.password());
            if (!passwordEncoder.matches(challenger, password)) {
                bindingResult.addError(new FieldError(bindingResult.getObjectName(), "password", null, false,
                        new String[]{PASSWORD_IS_INCORRECT}, null, null));
                throw ValidationException.create(bindingResult);
            }

            return generateTokenResponse(user, refreshTokenService.emitToken(user));
        } finally {
            Arrays.fill(request.password(), '*');
        }
    }

    public AccessTokenResponse refresh(RefreshRequest request) {
        UUID token = request.refreshToken();
        RefreshToken refreshToken = refreshTokenRepository.findWithUserWithRoleByToken(token)
                .orElseThrow(AuthorizationFailedException::refreshTokenNotFound);

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw AuthorizationFailedException.refreshTokenIsExpired();
        }

        User user = refreshToken.getUser();
        return generateTokenResponse(user, refreshToken.getToken());
    }

    private AccessTokenResponse generateTokenResponse(User user, UUID refreshToken) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("Something went wrong");
        }
        JwtPrincipal principal = new JwtPrincipal(user.getId(), user.getEmail(), user.getRole().getAuthorities());
        CharSequence token = jwtService.generateToken(principal);
        long expirationSeconds = properties.getExpiration().getSeconds();

        return new AccessTokenResponse(token, refreshToken, expirationSeconds, user.getRole().getAuthorities());
    }
}

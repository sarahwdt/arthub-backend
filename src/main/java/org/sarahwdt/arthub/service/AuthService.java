package org.sarahwdt.arthub.service;

import lombok.RequiredArgsConstructor;
import org.sarahwdt.arthub.configuration.property.Jwt;
import org.sarahwdt.arthub.dto.AccessTokenResponse;
import org.sarahwdt.arthub.dto.JwtPrincipal;
import org.sarahwdt.arthub.dto.LoginRequest;
import org.sarahwdt.arthub.dto.RefreshRequest;
import org.sarahwdt.arthub.exception.AuthorizationFailedException;
import org.sarahwdt.arthub.model.user.RefreshToken;
import org.sarahwdt.arthub.model.user.User;
import org.sarahwdt.arthub.repository.RefreshTokenRepository;
import org.sarahwdt.arthub.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final Jwt properties;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    public AccessTokenResponse login(LoginRequest request) {
        try {
            User user = userRepository.findWithRoleAndCredentialsByUsername(request.username())
                    .orElseThrow(() -> AuthorizationFailedException.userNotFound(request.username()));

            String password = user.getCredentials().getPassword();
            CharBuffer challenger = CharBuffer.wrap(request.password());
            if (!passwordEncoder.matches(challenger, password)) {
                throw AuthorizationFailedException.passwordIsIncorrect();
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
        JwtPrincipal principal = new JwtPrincipal(user.getId(), user.getUsername(), user.getRole().getAuthorities());
        CharSequence token = jwtService.generateToken(principal);
        long expirationSeconds = properties.getExpiration().getSeconds();

        return new AccessTokenResponse(token, refreshToken, expirationSeconds);
    }
}

package org.sarahwdt.arthub.service;

import lombok.RequiredArgsConstructor;
import org.sarahwdt.arthub.configuration.property.Jwt;
import org.sarahwdt.arthub.model.user.RefreshToken;
import org.sarahwdt.arthub.model.user.User;
import org.sarahwdt.arthub.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final Jwt properties;

    public UUID emitToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID());
        refreshToken.setExpiryDate(Instant.now().plus(properties.getRefreshExpiration()));

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }
}

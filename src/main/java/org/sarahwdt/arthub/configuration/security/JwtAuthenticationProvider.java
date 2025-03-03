package org.sarahwdt.arthub.configuration.security;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.sarahwdt.arthub.dto.JwtPrincipal;
import org.sarahwdt.arthub.service.JwtService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtService jwtService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            if (authentication.getPrincipal() instanceof String token) {
                token = token.replace(SecurityConfiguration.AUTH_HEADER_PREFIX, "");
                JwtPrincipal jwtPrincipal = jwtService.extractValidPrincipal(token);
                return new JwtAuthenticationToken(jwtPrincipal);
            }
        } catch (JwtException ignored) {}
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PreAuthenticatedAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

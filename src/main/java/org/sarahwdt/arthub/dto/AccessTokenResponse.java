package org.sarahwdt.arthub.dto;

import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;

public record AccessTokenResponse(

        @NonNull
        CharSequence accessToken,

        @NonNull
        UUID refreshToken,
        
        long expiresIn,

        @NonNull
        Collection<? extends GrantedAuthority> authorities
) {}

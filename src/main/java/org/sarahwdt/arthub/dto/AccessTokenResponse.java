package org.sarahwdt.arthub.dto;

import org.springframework.lang.NonNull;

import java.util.UUID;

public record AccessTokenResponse(

        @NonNull
        CharSequence accessToken,

        @NonNull
        UUID refreshToken,
        
        long expiresIn
) {}

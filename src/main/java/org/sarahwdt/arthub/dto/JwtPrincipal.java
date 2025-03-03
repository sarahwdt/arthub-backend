package org.sarahwdt.arthub.dto;

import org.sarahwdt.arthub.model.user.Privilege;
import org.springframework.lang.NonNull;
import org.springframework.security.core.AuthenticatedPrincipal;

import java.util.Set;

public record JwtPrincipal(
        int id,

        @NonNull
        String username,

        @NonNull
        Set<Privilege> authorities
) implements AuthenticatedPrincipal {

    @Override
    public String getName() {
        return username;
    }
}

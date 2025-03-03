package org.sarahwdt.arthub.configuration.security;

import org.sarahwdt.arthub.dto.JwtPrincipal;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private final JwtPrincipal principal;
    private boolean authenticated;

    public JwtAuthenticationToken(JwtPrincipal principal) {
        super(principal.authorities());
        this.authenticated = true;
        this.principal = principal;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
        if (!authenticated) {
            this.authenticated = false;
        } else {
            throw new IllegalArgumentException("Cannot set this token to trusted - " +
                    "use constructor which takes a GrantedAuthority list instead");
        }
    }
}

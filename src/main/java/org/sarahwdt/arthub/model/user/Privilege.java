package org.sarahwdt.arthub.model.user;

import org.springframework.security.core.GrantedAuthority;

public enum Privilege implements GrantedAuthority {
    READ_CLIENTS,
    WRITE_CLIENTS;

    @Override
    public String getAuthority() {
        return this.name();
    }
}

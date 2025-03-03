package org.sarahwdt.arthub.dto;

import org.sarahwdt.arthub.model.user.Privilege;
import org.springframework.lang.NonNull;

import java.util.Set;

public record RoleResponse(

        @NonNull
        String name,

        @NonNull
        Set<Privilege> authorities
) { }

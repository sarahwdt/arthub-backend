package org.sarahwdt.arthub.dto;

import org.springframework.lang.NonNull;

public record UserResponse(

        @NonNull
        String email,

        @NonNull
        RoleResponse role
) { }

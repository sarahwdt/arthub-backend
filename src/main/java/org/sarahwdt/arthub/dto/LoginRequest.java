package org.sarahwdt.arthub.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.lang.NonNull;

public record LoginRequest(

        @NotNull
        @NonNull
        String username,

        @NotNull
        @NonNull
        char[] password
) { }

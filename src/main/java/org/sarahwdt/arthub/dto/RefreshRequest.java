package org.sarahwdt.arthub.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.lang.NonNull;

import java.util.UUID;

public record RefreshRequest(

        @NotNull
        @NonNull
        UUID refreshToken
) {
}

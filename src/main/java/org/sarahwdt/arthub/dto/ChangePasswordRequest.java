package org.sarahwdt.arthub.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.lang.Nullable;

public record ChangePasswordRequest(

        @NotNull
        @Nullable
        char[] oldPassword,

        @NotNull
        @Nullable
        char[] newPassword,

        @NotNull
        @Nullable
        char[] passwordConfirmation
) {
}

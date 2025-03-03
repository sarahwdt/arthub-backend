package org.sarahwdt.arthub.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.lang.NonNull;

public record ChangePasswordRequest(

        @NotNull
        @NonNull
        char[] oldPassword,

        @NotNull
        @NonNull
        char[] newPassword,

        @NotNull
        @NonNull
        char[] confirmPassword
) {
}

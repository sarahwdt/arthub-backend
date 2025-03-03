package org.sarahwdt.arthub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.lang.NonNull;

public record RegistrationRequest(

        @Size(min = 3, max = 32)
        @NotBlank
        @NonNull
        String username,

        @Email
        @Size(max = 320)
        @NonNull
        String email,

        @NotNull
        @NonNull
        char[] password,

        @NotNull
        @NonNull
        char[] confirmPassword
) {}

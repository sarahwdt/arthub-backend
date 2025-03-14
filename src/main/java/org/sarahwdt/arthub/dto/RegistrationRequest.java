package org.sarahwdt.arthub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.lang.NonNull;

public record RegistrationRequest(

        @Email
        @Size(max = 320)
        @NotBlank
        @NonNull
        String email,

        @NotNull(message = "{jakarta.validation.constraints.NotBlank.message}")
        @NonNull
        char[] password,

        @NotNull(message = "{jakarta.validation.constraints.NotBlank.message}")
        @NonNull
        char[] passwordConfirmation
) {}

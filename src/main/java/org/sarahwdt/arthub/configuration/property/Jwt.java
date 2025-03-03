package org.sarahwdt.arthub.configuration.property;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Validated
@NoArgsConstructor
@Getter
@Setter(onMethod_ = @ConfigurationPropertiesBinding)
public class Jwt {
    /**
     * secret for jwt generation
     */
    @NotNull
    @Size(min = 32, max = 32)
    private String secret;

    /**
     * token expiration duration
     */
    @NotNull
    private Duration expiration = Duration.ofHours(1);

    /**
     * refresh token expiration duration
     */
    @NotNull
    private Duration refreshExpiration = Duration.ofDays(1);
}

package org.sarahwdt.arthub.configuration.property;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sarahwdt.arthub.model.user.Privilege;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.validation.annotation.Validated;

import java.util.Set;


@Validated
@NoArgsConstructor
@Getter
@Setter(onMethod_ = @ConfigurationPropertiesBinding)
public class Registration {
    /**
     * default role for newly registered
     */
    @Size(max = 32)
    @Pattern(regexp = "[A-Z]+")
    @NotNull
    private String role = "User";

    /**
     * default set of privileges for newly registered
     */
    @NotNull
    private Set<Privilege> privileges = Set.of();
}

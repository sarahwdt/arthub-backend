package org.sarahwdt.arthub.integration.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;

@Getter
@Setter(onMethod_ = @ConfigurationPropertiesBinding)
public class TestProperties {
    private String email = "admin@mail.com";

    private String password = "12345678";

    private String role = "ADMIN";
}

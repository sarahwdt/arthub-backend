package org.sarahwdt.arthub.configuration;

import org.sarahwdt.arthub.model.user.User;
import org.sarahwdt.arthub.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.Instant;
import java.util.Optional;

@EnableJpaAuditing(auditorAwareRef = "auditorAware", dateTimeProviderRef = "dateTimeProvider")
@Configuration
public class PersistenceConfiguration {
    @Bean
    public AuditorAware<User> auditorAware(UserRepository userRepository) {
        return new UserAuditorAware(userRepository);
    }

    @Bean
    public DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(Instant.now());
    }
}

package org.sarahwdt.arthub.configuration;

import lombok.RequiredArgsConstructor;
import org.sarahwdt.arthub.dto.JwtPrincipal;
import org.sarahwdt.arthub.model.user.User;
import org.sarahwdt.arthub.repository.UserRepository;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@RequiredArgsConstructor
public class UserAuditorAware implements AuditorAware<User> {
    private final UserRepository userRepository;

    @NonNull
    @Override
    public Optional<User> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() != null
                && authentication.getPrincipal() instanceof JwtPrincipal jwtPrincipal) {
            return userRepository.findById(jwtPrincipal.id());
        }
        return Optional.empty();
    }
}

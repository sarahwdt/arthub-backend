package org.sarahwdt.arthub.repository;

import org.sarahwdt.arthub.model.user.RefreshToken;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;
import java.util.UUID;

@RepositoryRestResource(exported = false)
public interface RefreshTokenRepository extends Repository<RefreshToken, UUID> {
    RefreshToken save(RefreshToken refreshToken);

    @EntityGraph(attributePaths = {"user", "user.role", "user.role.authorities"})
    Optional<RefreshToken> findWithUserWithRoleByToken(UUID idToken);
}

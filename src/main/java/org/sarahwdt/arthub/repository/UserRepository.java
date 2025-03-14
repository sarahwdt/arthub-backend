package org.sarahwdt.arthub.repository;

import org.sarahwdt.arthub.model.user.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface UserRepository extends Repository<User, Integer> {
    User save(User user);

    @EntityGraph(attributePaths = {"role", "role.authorities", "credentials"})
    Optional<User> findWithRoleAndCredentialsByEmail(String email);

    Optional<User> findById(int id);
}

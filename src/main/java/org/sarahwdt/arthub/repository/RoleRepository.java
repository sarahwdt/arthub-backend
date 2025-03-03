package org.sarahwdt.arthub.repository;

import org.sarahwdt.arthub.model.user.Role;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface RoleRepository extends Repository<Role, String> {
    Optional<Role> findByName(String name);

    Role save(Role role);
}

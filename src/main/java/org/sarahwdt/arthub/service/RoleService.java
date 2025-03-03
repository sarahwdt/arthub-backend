package org.sarahwdt.arthub.service;

import lombok.RequiredArgsConstructor;
import org.sarahwdt.arthub.configuration.property.Registration;
import org.sarahwdt.arthub.model.user.Privilege;
import org.sarahwdt.arthub.model.user.Role;
import org.sarahwdt.arthub.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final Registration properties;

    public Role getRoleForNewlyRegistered() {
        return roleRepository.findByName(properties.getRole())
                .orElseGet(() -> newRole(properties.getRole(), properties.getPrivileges()));
    }

    private Role newRole(String name, Set<Privilege> privileges) {
        Role role = new Role();
        role.setName(name);
        role.setAuthorities(privileges);
        return roleRepository.save(role);
    }
}

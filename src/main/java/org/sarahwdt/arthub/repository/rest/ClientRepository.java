package org.sarahwdt.arthub.repository.rest;

import org.sarahwdt.arthub.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

@RepositoryRestResource
@PreAuthorize("hasAuthority('ADMIN')")
public interface ClientRepository extends JpaRepository<Client, Integer> {
}

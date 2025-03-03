package org.sarahwdt.arthub.repository;

import org.sarahwdt.arthub.model.user.Credentials;
import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface CredentialsRepository extends Repository<Credentials, Integer> {
    Optional<Credentials> findById(Integer id);

    Credentials save(Credentials credentials);
}

package com.serveup.repository;

import com.serveup.domain.AppUser;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AppUser entity.
 *
 * When extending this class, extend AppUserRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface AppUserRepository extends AppUserRepositoryWithBagRelationships, JpaRepository<AppUser, UUID> {
    default Optional<AppUser> findOneWithEagerRelationships(UUID id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<AppUser> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<AppUser> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}

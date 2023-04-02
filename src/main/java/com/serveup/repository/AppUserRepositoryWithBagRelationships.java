package com.serveup.repository;

import com.serveup.domain.AppUser;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface AppUserRepositoryWithBagRelationships {
    Optional<AppUser> fetchBagRelationships(Optional<AppUser> appUser);

    List<AppUser> fetchBagRelationships(List<AppUser> appUsers);

    Page<AppUser> fetchBagRelationships(Page<AppUser> appUsers);
}

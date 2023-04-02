package com.serveup.repository;

import com.serveup.domain.Location;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Location entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocationRepository extends JpaRepository<Location, UUID> {}

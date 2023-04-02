package com.serveup.repository;

import com.serveup.domain.Rating;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Rating entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RatingRepository extends JpaRepository<Rating, UUID> {}

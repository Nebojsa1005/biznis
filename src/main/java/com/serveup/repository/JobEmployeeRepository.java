package com.serveup.repository;

import com.serveup.domain.JobEmployee;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the JobEmployee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobEmployeeRepository extends JpaRepository<JobEmployee, Long> {}

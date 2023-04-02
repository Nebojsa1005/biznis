package com.serveup.web.rest;

import com.serveup.domain.JobEmployee;
import com.serveup.repository.JobEmployeeRepository;
import com.serveup.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.serveup.domain.JobEmployee}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class JobEmployeeResource {

    private final Logger log = LoggerFactory.getLogger(JobEmployeeResource.class);

    private static final String ENTITY_NAME = "jobEmployee";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JobEmployeeRepository jobEmployeeRepository;

    public JobEmployeeResource(JobEmployeeRepository jobEmployeeRepository) {
        this.jobEmployeeRepository = jobEmployeeRepository;
    }

    /**
     * {@code POST  /job-employees} : Create a new jobEmployee.
     *
     * @param jobEmployee the jobEmployee to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new jobEmployee, or with status {@code 400 (Bad Request)} if the jobEmployee has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/job-employees")
    public ResponseEntity<JobEmployee> createJobEmployee(@RequestBody JobEmployee jobEmployee) throws URISyntaxException {
        log.debug("REST request to save JobEmployee : {}", jobEmployee);
        if (jobEmployee.getId() != null) {
            throw new BadRequestAlertException("A new jobEmployee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JobEmployee result = jobEmployeeRepository.save(jobEmployee);
        return ResponseEntity
            .created(new URI("/api/job-employees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /job-employees/:id} : Updates an existing jobEmployee.
     *
     * @param id the id of the jobEmployee to save.
     * @param jobEmployee the jobEmployee to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jobEmployee,
     * or with status {@code 400 (Bad Request)} if the jobEmployee is not valid,
     * or with status {@code 500 (Internal Server Error)} if the jobEmployee couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/job-employees/{id}")
    public ResponseEntity<JobEmployee> updateJobEmployee(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody JobEmployee jobEmployee
    ) throws URISyntaxException {
        log.debug("REST request to update JobEmployee : {}, {}", id, jobEmployee);
        if (jobEmployee.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jobEmployee.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jobEmployeeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        JobEmployee result = jobEmployeeRepository.save(jobEmployee);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jobEmployee.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /job-employees/:id} : Partial updates given fields of an existing jobEmployee, field will ignore if it is null
     *
     * @param id the id of the jobEmployee to save.
     * @param jobEmployee the jobEmployee to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jobEmployee,
     * or with status {@code 400 (Bad Request)} if the jobEmployee is not valid,
     * or with status {@code 404 (Not Found)} if the jobEmployee is not found,
     * or with status {@code 500 (Internal Server Error)} if the jobEmployee couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/job-employees/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<JobEmployee> partialUpdateJobEmployee(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody JobEmployee jobEmployee
    ) throws URISyntaxException {
        log.debug("REST request to partial update JobEmployee partially : {}, {}", id, jobEmployee);
        if (jobEmployee.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jobEmployee.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jobEmployeeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<JobEmployee> result = jobEmployeeRepository
            .findById(jobEmployee.getId())
            .map(existingJobEmployee -> {
                if (jobEmployee.getType() != null) {
                    existingJobEmployee.setType(jobEmployee.getType());
                }
                if (jobEmployee.getNumber() != null) {
                    existingJobEmployee.setNumber(jobEmployee.getNumber());
                }
                if (jobEmployee.getFound() != null) {
                    existingJobEmployee.setFound(jobEmployee.getFound());
                }

                return existingJobEmployee;
            })
            .map(jobEmployeeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jobEmployee.getId().toString())
        );
    }

    /**
     * {@code GET  /job-employees} : get all the jobEmployees.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jobEmployees in body.
     */
    @GetMapping("/job-employees")
    public List<JobEmployee> getAllJobEmployees() {
        log.debug("REST request to get all JobEmployees");
        return jobEmployeeRepository.findAll();
    }

    /**
     * {@code GET  /job-employees/:id} : get the "id" jobEmployee.
     *
     * @param id the id of the jobEmployee to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the jobEmployee, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/job-employees/{id}")
    public ResponseEntity<JobEmployee> getJobEmployee(@PathVariable Long id) {
        log.debug("REST request to get JobEmployee : {}", id);
        Optional<JobEmployee> jobEmployee = jobEmployeeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(jobEmployee);
    }

    /**
     * {@code DELETE  /job-employees/:id} : delete the "id" jobEmployee.
     *
     * @param id the id of the jobEmployee to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/job-employees/{id}")
    public ResponseEntity<Void> deleteJobEmployee(@PathVariable Long id) {
        log.debug("REST request to delete JobEmployee : {}", id);
        jobEmployeeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

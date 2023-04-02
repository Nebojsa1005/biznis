package com.serveup.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.serveup.IntegrationTest;
import com.serveup.domain.JobEmployee;
import com.serveup.domain.enumeration.EmployeeType;
import com.serveup.repository.JobEmployeeRepository;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link JobEmployeeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class JobEmployeeResourceIT {

    private static final EmployeeType DEFAULT_TYPE = EmployeeType.WAITER;
    private static final EmployeeType UPDATED_TYPE = EmployeeType.BARTENDER;

    private static final Integer DEFAULT_NUMBER = 1;
    private static final Integer UPDATED_NUMBER = 2;

    private static final Integer DEFAULT_FOUND = 1;
    private static final Integer UPDATED_FOUND = 2;

    private static final String ENTITY_API_URL = "/api/job-employees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private JobEmployeeRepository jobEmployeeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJobEmployeeMockMvc;

    private JobEmployee jobEmployee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobEmployee createEntity(EntityManager em) {
        JobEmployee jobEmployee = new JobEmployee().type(DEFAULT_TYPE).number(DEFAULT_NUMBER).found(DEFAULT_FOUND);
        return jobEmployee;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobEmployee createUpdatedEntity(EntityManager em) {
        JobEmployee jobEmployee = new JobEmployee().type(UPDATED_TYPE).number(UPDATED_NUMBER).found(UPDATED_FOUND);
        return jobEmployee;
    }

    @BeforeEach
    public void initTest() {
        jobEmployee = createEntity(em);
    }

    @Test
    @Transactional
    void createJobEmployee() throws Exception {
        int databaseSizeBeforeCreate = jobEmployeeRepository.findAll().size();
        // Create the JobEmployee
        restJobEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobEmployee)))
            .andExpect(status().isCreated());

        // Validate the JobEmployee in the database
        List<JobEmployee> jobEmployeeList = jobEmployeeRepository.findAll();
        assertThat(jobEmployeeList).hasSize(databaseSizeBeforeCreate + 1);
        JobEmployee testJobEmployee = jobEmployeeList.get(jobEmployeeList.size() - 1);
        assertThat(testJobEmployee.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testJobEmployee.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testJobEmployee.getFound()).isEqualTo(DEFAULT_FOUND);
    }

    @Test
    @Transactional
    void createJobEmployeeWithExistingId() throws Exception {
        // Create the JobEmployee with an existing ID
        jobEmployee.setId(1L);

        int databaseSizeBeforeCreate = jobEmployeeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobEmployee)))
            .andExpect(status().isBadRequest());

        // Validate the JobEmployee in the database
        List<JobEmployee> jobEmployeeList = jobEmployeeRepository.findAll();
        assertThat(jobEmployeeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllJobEmployees() throws Exception {
        // Initialize the database
        jobEmployeeRepository.saveAndFlush(jobEmployee);

        // Get all the jobEmployeeList
        restJobEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobEmployee.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].found").value(hasItem(DEFAULT_FOUND)));
    }

    @Test
    @Transactional
    void getJobEmployee() throws Exception {
        // Initialize the database
        jobEmployeeRepository.saveAndFlush(jobEmployee);

        // Get the jobEmployee
        restJobEmployeeMockMvc
            .perform(get(ENTITY_API_URL_ID, jobEmployee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(jobEmployee.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.found").value(DEFAULT_FOUND));
    }

    @Test
    @Transactional
    void getNonExistingJobEmployee() throws Exception {
        // Get the jobEmployee
        restJobEmployeeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingJobEmployee() throws Exception {
        // Initialize the database
        jobEmployeeRepository.saveAndFlush(jobEmployee);

        int databaseSizeBeforeUpdate = jobEmployeeRepository.findAll().size();

        // Update the jobEmployee
        JobEmployee updatedJobEmployee = jobEmployeeRepository.findById(jobEmployee.getId()).get();
        // Disconnect from session so that the updates on updatedJobEmployee are not directly saved in db
        em.detach(updatedJobEmployee);
        updatedJobEmployee.type(UPDATED_TYPE).number(UPDATED_NUMBER).found(UPDATED_FOUND);

        restJobEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedJobEmployee.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedJobEmployee))
            )
            .andExpect(status().isOk());

        // Validate the JobEmployee in the database
        List<JobEmployee> jobEmployeeList = jobEmployeeRepository.findAll();
        assertThat(jobEmployeeList).hasSize(databaseSizeBeforeUpdate);
        JobEmployee testJobEmployee = jobEmployeeList.get(jobEmployeeList.size() - 1);
        assertThat(testJobEmployee.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testJobEmployee.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testJobEmployee.getFound()).isEqualTo(UPDATED_FOUND);
    }

    @Test
    @Transactional
    void putNonExistingJobEmployee() throws Exception {
        int databaseSizeBeforeUpdate = jobEmployeeRepository.findAll().size();
        jobEmployee.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, jobEmployee.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jobEmployee))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobEmployee in the database
        List<JobEmployee> jobEmployeeList = jobEmployeeRepository.findAll();
        assertThat(jobEmployeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchJobEmployee() throws Exception {
        int databaseSizeBeforeUpdate = jobEmployeeRepository.findAll().size();
        jobEmployee.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jobEmployee))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobEmployee in the database
        List<JobEmployee> jobEmployeeList = jobEmployeeRepository.findAll();
        assertThat(jobEmployeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamJobEmployee() throws Exception {
        int databaseSizeBeforeUpdate = jobEmployeeRepository.findAll().size();
        jobEmployee.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobEmployeeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobEmployee)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the JobEmployee in the database
        List<JobEmployee> jobEmployeeList = jobEmployeeRepository.findAll();
        assertThat(jobEmployeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateJobEmployeeWithPatch() throws Exception {
        // Initialize the database
        jobEmployeeRepository.saveAndFlush(jobEmployee);

        int databaseSizeBeforeUpdate = jobEmployeeRepository.findAll().size();

        // Update the jobEmployee using partial update
        JobEmployee partialUpdatedJobEmployee = new JobEmployee();
        partialUpdatedJobEmployee.setId(jobEmployee.getId());

        partialUpdatedJobEmployee.type(UPDATED_TYPE).found(UPDATED_FOUND);

        restJobEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJobEmployee.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJobEmployee))
            )
            .andExpect(status().isOk());

        // Validate the JobEmployee in the database
        List<JobEmployee> jobEmployeeList = jobEmployeeRepository.findAll();
        assertThat(jobEmployeeList).hasSize(databaseSizeBeforeUpdate);
        JobEmployee testJobEmployee = jobEmployeeList.get(jobEmployeeList.size() - 1);
        assertThat(testJobEmployee.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testJobEmployee.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testJobEmployee.getFound()).isEqualTo(UPDATED_FOUND);
    }

    @Test
    @Transactional
    void fullUpdateJobEmployeeWithPatch() throws Exception {
        // Initialize the database
        jobEmployeeRepository.saveAndFlush(jobEmployee);

        int databaseSizeBeforeUpdate = jobEmployeeRepository.findAll().size();

        // Update the jobEmployee using partial update
        JobEmployee partialUpdatedJobEmployee = new JobEmployee();
        partialUpdatedJobEmployee.setId(jobEmployee.getId());

        partialUpdatedJobEmployee.type(UPDATED_TYPE).number(UPDATED_NUMBER).found(UPDATED_FOUND);

        restJobEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJobEmployee.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJobEmployee))
            )
            .andExpect(status().isOk());

        // Validate the JobEmployee in the database
        List<JobEmployee> jobEmployeeList = jobEmployeeRepository.findAll();
        assertThat(jobEmployeeList).hasSize(databaseSizeBeforeUpdate);
        JobEmployee testJobEmployee = jobEmployeeList.get(jobEmployeeList.size() - 1);
        assertThat(testJobEmployee.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testJobEmployee.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testJobEmployee.getFound()).isEqualTo(UPDATED_FOUND);
    }

    @Test
    @Transactional
    void patchNonExistingJobEmployee() throws Exception {
        int databaseSizeBeforeUpdate = jobEmployeeRepository.findAll().size();
        jobEmployee.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, jobEmployee.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(jobEmployee))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobEmployee in the database
        List<JobEmployee> jobEmployeeList = jobEmployeeRepository.findAll();
        assertThat(jobEmployeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchJobEmployee() throws Exception {
        int databaseSizeBeforeUpdate = jobEmployeeRepository.findAll().size();
        jobEmployee.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(jobEmployee))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobEmployee in the database
        List<JobEmployee> jobEmployeeList = jobEmployeeRepository.findAll();
        assertThat(jobEmployeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamJobEmployee() throws Exception {
        int databaseSizeBeforeUpdate = jobEmployeeRepository.findAll().size();
        jobEmployee.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(jobEmployee))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the JobEmployee in the database
        List<JobEmployee> jobEmployeeList = jobEmployeeRepository.findAll();
        assertThat(jobEmployeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteJobEmployee() throws Exception {
        // Initialize the database
        jobEmployeeRepository.saveAndFlush(jobEmployee);

        int databaseSizeBeforeDelete = jobEmployeeRepository.findAll().size();

        // Delete the jobEmployee
        restJobEmployeeMockMvc
            .perform(delete(ENTITY_API_URL_ID, jobEmployee.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<JobEmployee> jobEmployeeList = jobEmployeeRepository.findAll();
        assertThat(jobEmployeeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

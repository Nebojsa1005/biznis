package com.serveup.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.serveup.IntegrationTest;
import com.serveup.domain.AppUser;
import com.serveup.domain.enumeration.UserType;
import com.serveup.repository.AppUserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link AppUserResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AppUserResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Long DEFAULT_PHONE = 1L;
    private static final Long UPDATED_PHONE = 2L;

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final Double DEFAULT_AVG_SALARY = 1D;
    private static final Double UPDATED_AVG_SALARY = 2D;

    private static final Double DEFAULT_AVG_RATING = 1D;
    private static final Double UPDATED_AVG_RATING = 2D;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final UserType DEFAULT_USER_TYPE = UserType.EMPLOYEE;
    private static final UserType UPDATED_USER_TYPE = UserType.EMPLOYER;

    private static final String ENTITY_API_URL = "/api/app-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private AppUserRepository appUserRepository;

    @Mock
    private AppUserRepository appUserRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppUserMockMvc;

    private AppUser appUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppUser createEntity(EntityManager em) {
        AppUser appUser = new AppUser()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .avgSalary(DEFAULT_AVG_SALARY)
            .avgRating(DEFAULT_AVG_RATING)
            .isActive(DEFAULT_IS_ACTIVE)
            .userType(DEFAULT_USER_TYPE);
        return appUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppUser createUpdatedEntity(EntityManager em) {
        AppUser appUser = new AppUser()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .avgSalary(UPDATED_AVG_SALARY)
            .avgRating(UPDATED_AVG_RATING)
            .isActive(UPDATED_IS_ACTIVE)
            .userType(UPDATED_USER_TYPE);
        return appUser;
    }

    @BeforeEach
    public void initTest() {
        appUser = createEntity(em);
    }

    @Test
    @Transactional
    void createAppUser() throws Exception {
        int databaseSizeBeforeCreate = appUserRepository.findAll().size();
        // Create the AppUser
        restAppUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appUser)))
            .andExpect(status().isCreated());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeCreate + 1);
        AppUser testAppUser = appUserList.get(appUserList.size() - 1);
        assertThat(testAppUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testAppUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testAppUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testAppUser.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testAppUser.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testAppUser.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testAppUser.getAvgSalary()).isEqualTo(DEFAULT_AVG_SALARY);
        assertThat(testAppUser.getAvgRating()).isEqualTo(DEFAULT_AVG_RATING);
        assertThat(testAppUser.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testAppUser.getUserType()).isEqualTo(DEFAULT_USER_TYPE);
    }

    @Test
    @Transactional
    void createAppUserWithExistingId() throws Exception {
        // Create the AppUser with an existing ID
        appUserRepository.saveAndFlush(appUser);

        int databaseSizeBeforeCreate = appUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appUser)))
            .andExpect(status().isBadRequest());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAppUsers() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList
        restAppUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appUser.getId().toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.intValue())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].avgSalary").value(hasItem(DEFAULT_AVG_SALARY.doubleValue())))
            .andExpect(jsonPath("$.[*].avgRating").value(hasItem(DEFAULT_AVG_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].userType").value(hasItem(DEFAULT_USER_TYPE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAppUsersWithEagerRelationshipsIsEnabled() throws Exception {
        when(appUserRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAppUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(appUserRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAppUsersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(appUserRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAppUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(appUserRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAppUser() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        // Get the appUser
        restAppUserMockMvc
            .perform(get(ENTITY_API_URL_ID, appUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appUser.getId().toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.intValue()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.avgSalary").value(DEFAULT_AVG_SALARY.doubleValue()))
            .andExpect(jsonPath("$.avgRating").value(DEFAULT_AVG_RATING.doubleValue()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.userType").value(DEFAULT_USER_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAppUser() throws Exception {
        // Get the appUser
        restAppUserMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAppUser() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();

        // Update the appUser
        AppUser updatedAppUser = appUserRepository.findById(appUser.getId()).get();
        // Disconnect from session so that the updates on updatedAppUser are not directly saved in db
        em.detach(updatedAppUser);
        updatedAppUser
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .avgSalary(UPDATED_AVG_SALARY)
            .avgRating(UPDATED_AVG_RATING)
            .isActive(UPDATED_IS_ACTIVE)
            .userType(UPDATED_USER_TYPE);

        restAppUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAppUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAppUser))
            )
            .andExpect(status().isOk());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate);
        AppUser testAppUser = appUserList.get(appUserList.size() - 1);
        assertThat(testAppUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testAppUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testAppUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAppUser.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testAppUser.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testAppUser.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testAppUser.getAvgSalary()).isEqualTo(UPDATED_AVG_SALARY);
        assertThat(testAppUser.getAvgRating()).isEqualTo(UPDATED_AVG_RATING);
        assertThat(testAppUser.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testAppUser.getUserType()).isEqualTo(UPDATED_USER_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingAppUser() throws Exception {
        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();
        appUser.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAppUser() throws Exception {
        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();
        appUser.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAppUser() throws Exception {
        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();
        appUser.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAppUserWithPatch() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();

        // Update the appUser using partial update
        AppUser partialUpdatedAppUser = new AppUser();
        partialUpdatedAppUser.setId(appUser.getId());

        partialUpdatedAppUser
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .avgSalary(UPDATED_AVG_SALARY)
            .userType(UPDATED_USER_TYPE);

        restAppUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppUser))
            )
            .andExpect(status().isOk());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate);
        AppUser testAppUser = appUserList.get(appUserList.size() - 1);
        assertThat(testAppUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testAppUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testAppUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testAppUser.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testAppUser.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testAppUser.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testAppUser.getAvgSalary()).isEqualTo(UPDATED_AVG_SALARY);
        assertThat(testAppUser.getAvgRating()).isEqualTo(DEFAULT_AVG_RATING);
        assertThat(testAppUser.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testAppUser.getUserType()).isEqualTo(UPDATED_USER_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateAppUserWithPatch() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();

        // Update the appUser using partial update
        AppUser partialUpdatedAppUser = new AppUser();
        partialUpdatedAppUser.setId(appUser.getId());

        partialUpdatedAppUser
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .avgSalary(UPDATED_AVG_SALARY)
            .avgRating(UPDATED_AVG_RATING)
            .isActive(UPDATED_IS_ACTIVE)
            .userType(UPDATED_USER_TYPE);

        restAppUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppUser))
            )
            .andExpect(status().isOk());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate);
        AppUser testAppUser = appUserList.get(appUserList.size() - 1);
        assertThat(testAppUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testAppUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testAppUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAppUser.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testAppUser.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testAppUser.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testAppUser.getAvgSalary()).isEqualTo(UPDATED_AVG_SALARY);
        assertThat(testAppUser.getAvgRating()).isEqualTo(UPDATED_AVG_RATING);
        assertThat(testAppUser.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testAppUser.getUserType()).isEqualTo(UPDATED_USER_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingAppUser() throws Exception {
        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();
        appUser.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAppUser() throws Exception {
        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();
        appUser.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAppUser() throws Exception {
        int databaseSizeBeforeUpdate = appUserRepository.findAll().size();
        appUser.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(appUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppUser in the database
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAppUser() throws Exception {
        // Initialize the database
        appUserRepository.saveAndFlush(appUser);

        int databaseSizeBeforeDelete = appUserRepository.findAll().size();

        // Delete the appUser
        restAppUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, appUser.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AppUser> appUserList = appUserRepository.findAll();
        assertThat(appUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

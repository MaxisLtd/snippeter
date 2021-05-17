package com.mbeliakov.snippeter.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mbeliakov.snippeter.IntegrationTest;
import com.mbeliakov.snippeter.domain.ProjectChapter;
import com.mbeliakov.snippeter.repository.ProjectChapterRepository;
import com.mbeliakov.snippeter.service.dto.ProjectChapterModel;
import com.mbeliakov.snippeter.service.mapper.ProjectChapterMapper;
import java.util.List;
import java.util.Random;
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
 * Integration tests for the {@link ProjectChapterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProjectChapterResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/project-chapters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProjectChapterRepository projectChapterRepository;

    @Autowired
    private ProjectChapterMapper projectChapterMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProjectChapterMockMvc;

    private ProjectChapter projectChapter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjectChapter createEntity(EntityManager em) {
        ProjectChapter projectChapter = new ProjectChapter().code(DEFAULT_CODE).name(DEFAULT_NAME).type(DEFAULT_TYPE);
        return projectChapter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjectChapter createUpdatedEntity(EntityManager em) {
        ProjectChapter projectChapter = new ProjectChapter().code(UPDATED_CODE).name(UPDATED_NAME).type(UPDATED_TYPE);
        return projectChapter;
    }

    @BeforeEach
    public void initTest() {
        projectChapter = createEntity(em);
    }

    @Test
    @Transactional
    void createProjectChapter() throws Exception {
        int databaseSizeBeforeCreate = projectChapterRepository.findAll().size();
        // Create the ProjectChapter
        ProjectChapterModel projectChapterModel = projectChapterMapper.toDto(projectChapter);
        restProjectChapterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectChapterModel))
            )
            .andExpect(status().isCreated());

        // Validate the ProjectChapter in the database
        List<ProjectChapter> projectChapterList = projectChapterRepository.findAll();
        assertThat(projectChapterList).hasSize(databaseSizeBeforeCreate + 1);
        ProjectChapter testProjectChapter = projectChapterList.get(projectChapterList.size() - 1);
        assertThat(testProjectChapter.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testProjectChapter.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProjectChapter.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createProjectChapterWithExistingId() throws Exception {
        // Create the ProjectChapter with an existing ID
        projectChapter.setId(1L);
        ProjectChapterModel projectChapterModel = projectChapterMapper.toDto(projectChapter);

        int databaseSizeBeforeCreate = projectChapterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectChapterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectChapterModel))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectChapter in the database
        List<ProjectChapter> projectChapterList = projectChapterRepository.findAll();
        assertThat(projectChapterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectChapterRepository.findAll().size();
        // set the field null
        projectChapter.setCode(null);

        // Create the ProjectChapter, which fails.
        ProjectChapterModel projectChapterModel = projectChapterMapper.toDto(projectChapter);

        restProjectChapterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectChapterModel))
            )
            .andExpect(status().isBadRequest());

        List<ProjectChapter> projectChapterList = projectChapterRepository.findAll();
        assertThat(projectChapterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectChapterRepository.findAll().size();
        // set the field null
        projectChapter.setName(null);

        // Create the ProjectChapter, which fails.
        ProjectChapterModel projectChapterModel = projectChapterMapper.toDto(projectChapter);

        restProjectChapterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectChapterModel))
            )
            .andExpect(status().isBadRequest());

        List<ProjectChapter> projectChapterList = projectChapterRepository.findAll();
        assertThat(projectChapterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProjectChapters() throws Exception {
        // Initialize the database
        projectChapterRepository.saveAndFlush(projectChapter);

        // Get all the projectChapterList
        restProjectChapterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectChapter.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }

    @Test
    @Transactional
    void getProjectChapter() throws Exception {
        // Initialize the database
        projectChapterRepository.saveAndFlush(projectChapter);

        // Get the projectChapter
        restProjectChapterMockMvc
            .perform(get(ENTITY_API_URL_ID, projectChapter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(projectChapter.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }

    @Test
    @Transactional
    void getNonExistingProjectChapter() throws Exception {
        // Get the projectChapter
        restProjectChapterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProjectChapter() throws Exception {
        // Initialize the database
        projectChapterRepository.saveAndFlush(projectChapter);

        int databaseSizeBeforeUpdate = projectChapterRepository.findAll().size();

        // Update the projectChapter
        ProjectChapter updatedProjectChapter = projectChapterRepository.findById(projectChapter.getId()).get();
        // Disconnect from session so that the updates on updatedProjectChapter are not directly saved in db
        em.detach(updatedProjectChapter);
        updatedProjectChapter.code(UPDATED_CODE).name(UPDATED_NAME).type(UPDATED_TYPE);
        ProjectChapterModel projectChapterModel = projectChapterMapper.toDto(updatedProjectChapter);

        restProjectChapterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, projectChapterModel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projectChapterModel))
            )
            .andExpect(status().isOk());

        // Validate the ProjectChapter in the database
        List<ProjectChapter> projectChapterList = projectChapterRepository.findAll();
        assertThat(projectChapterList).hasSize(databaseSizeBeforeUpdate);
        ProjectChapter testProjectChapter = projectChapterList.get(projectChapterList.size() - 1);
        assertThat(testProjectChapter.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testProjectChapter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProjectChapter.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingProjectChapter() throws Exception {
        int databaseSizeBeforeUpdate = projectChapterRepository.findAll().size();
        projectChapter.setId(count.incrementAndGet());

        // Create the ProjectChapter
        ProjectChapterModel projectChapterModel = projectChapterMapper.toDto(projectChapter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectChapterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, projectChapterModel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projectChapterModel))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectChapter in the database
        List<ProjectChapter> projectChapterList = projectChapterRepository.findAll();
        assertThat(projectChapterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProjectChapter() throws Exception {
        int databaseSizeBeforeUpdate = projectChapterRepository.findAll().size();
        projectChapter.setId(count.incrementAndGet());

        // Create the ProjectChapter
        ProjectChapterModel projectChapterModel = projectChapterMapper.toDto(projectChapter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectChapterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projectChapterModel))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectChapter in the database
        List<ProjectChapter> projectChapterList = projectChapterRepository.findAll();
        assertThat(projectChapterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProjectChapter() throws Exception {
        int databaseSizeBeforeUpdate = projectChapterRepository.findAll().size();
        projectChapter.setId(count.incrementAndGet());

        // Create the ProjectChapter
        ProjectChapterModel projectChapterModel = projectChapterMapper.toDto(projectChapter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectChapterMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectChapterModel))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProjectChapter in the database
        List<ProjectChapter> projectChapterList = projectChapterRepository.findAll();
        assertThat(projectChapterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProjectChapterWithPatch() throws Exception {
        // Initialize the database
        projectChapterRepository.saveAndFlush(projectChapter);

        int databaseSizeBeforeUpdate = projectChapterRepository.findAll().size();

        // Update the projectChapter using partial update
        ProjectChapter partialUpdatedProjectChapter = new ProjectChapter();
        partialUpdatedProjectChapter.setId(projectChapter.getId());

        partialUpdatedProjectChapter.name(UPDATED_NAME).type(UPDATED_TYPE);

        restProjectChapterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProjectChapter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProjectChapter))
            )
            .andExpect(status().isOk());

        // Validate the ProjectChapter in the database
        List<ProjectChapter> projectChapterList = projectChapterRepository.findAll();
        assertThat(projectChapterList).hasSize(databaseSizeBeforeUpdate);
        ProjectChapter testProjectChapter = projectChapterList.get(projectChapterList.size() - 1);
        assertThat(testProjectChapter.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testProjectChapter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProjectChapter.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateProjectChapterWithPatch() throws Exception {
        // Initialize the database
        projectChapterRepository.saveAndFlush(projectChapter);

        int databaseSizeBeforeUpdate = projectChapterRepository.findAll().size();

        // Update the projectChapter using partial update
        ProjectChapter partialUpdatedProjectChapter = new ProjectChapter();
        partialUpdatedProjectChapter.setId(projectChapter.getId());

        partialUpdatedProjectChapter.code(UPDATED_CODE).name(UPDATED_NAME).type(UPDATED_TYPE);

        restProjectChapterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProjectChapter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProjectChapter))
            )
            .andExpect(status().isOk());

        // Validate the ProjectChapter in the database
        List<ProjectChapter> projectChapterList = projectChapterRepository.findAll();
        assertThat(projectChapterList).hasSize(databaseSizeBeforeUpdate);
        ProjectChapter testProjectChapter = projectChapterList.get(projectChapterList.size() - 1);
        assertThat(testProjectChapter.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testProjectChapter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProjectChapter.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingProjectChapter() throws Exception {
        int databaseSizeBeforeUpdate = projectChapterRepository.findAll().size();
        projectChapter.setId(count.incrementAndGet());

        // Create the ProjectChapter
        ProjectChapterModel projectChapterModel = projectChapterMapper.toDto(projectChapter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectChapterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, projectChapterModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(projectChapterModel))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectChapter in the database
        List<ProjectChapter> projectChapterList = projectChapterRepository.findAll();
        assertThat(projectChapterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProjectChapter() throws Exception {
        int databaseSizeBeforeUpdate = projectChapterRepository.findAll().size();
        projectChapter.setId(count.incrementAndGet());

        // Create the ProjectChapter
        ProjectChapterModel projectChapterModel = projectChapterMapper.toDto(projectChapter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectChapterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(projectChapterModel))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectChapter in the database
        List<ProjectChapter> projectChapterList = projectChapterRepository.findAll();
        assertThat(projectChapterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProjectChapter() throws Exception {
        int databaseSizeBeforeUpdate = projectChapterRepository.findAll().size();
        projectChapter.setId(count.incrementAndGet());

        // Create the ProjectChapter
        ProjectChapterModel projectChapterModel = projectChapterMapper.toDto(projectChapter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectChapterMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(projectChapterModel))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProjectChapter in the database
        List<ProjectChapter> projectChapterList = projectChapterRepository.findAll();
        assertThat(projectChapterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProjectChapter() throws Exception {
        // Initialize the database
        projectChapterRepository.saveAndFlush(projectChapter);

        int databaseSizeBeforeDelete = projectChapterRepository.findAll().size();

        // Delete the projectChapter
        restProjectChapterMockMvc
            .perform(delete(ENTITY_API_URL_ID, projectChapter.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProjectChapter> projectChapterList = projectChapterRepository.findAll();
        assertThat(projectChapterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

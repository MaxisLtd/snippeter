package com.mbeliakov.snippeter.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mbeliakov.snippeter.IntegrationTest;
import com.mbeliakov.snippeter.domain.LangType;
import com.mbeliakov.snippeter.repository.LangTypeRepository;
import com.mbeliakov.snippeter.service.dto.LangTypeModel;
import com.mbeliakov.snippeter.service.mapper.LangTypeMapper;
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
 * Integration tests for the {@link LangTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LangTypeResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/lang-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LangTypeRepository langTypeRepository;

    @Autowired
    private LangTypeMapper langTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLangTypeMockMvc;

    private LangType langType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LangType createEntity(EntityManager em) {
        LangType langType = new LangType().code(DEFAULT_CODE);
        return langType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LangType createUpdatedEntity(EntityManager em) {
        LangType langType = new LangType().code(UPDATED_CODE);
        return langType;
    }

    @BeforeEach
    public void initTest() {
        langType = createEntity(em);
    }

    @Test
    @Transactional
    void createLangType() throws Exception {
        int databaseSizeBeforeCreate = langTypeRepository.findAll().size();
        // Create the LangType
        LangTypeModel langTypeModel = langTypeMapper.toDto(langType);
        restLangTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(langTypeModel)))
            .andExpect(status().isCreated());

        // Validate the LangType in the database
        List<LangType> langTypeList = langTypeRepository.findAll();
        assertThat(langTypeList).hasSize(databaseSizeBeforeCreate + 1);
        LangType testLangType = langTypeList.get(langTypeList.size() - 1);
        assertThat(testLangType.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void createLangTypeWithExistingId() throws Exception {
        // Create the LangType with an existing ID
        langType.setId(1L);
        LangTypeModel langTypeModel = langTypeMapper.toDto(langType);

        int databaseSizeBeforeCreate = langTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLangTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(langTypeModel)))
            .andExpect(status().isBadRequest());

        // Validate the LangType in the database
        List<LangType> langTypeList = langTypeRepository.findAll();
        assertThat(langTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = langTypeRepository.findAll().size();
        // set the field null
        langType.setCode(null);

        // Create the LangType, which fails.
        LangTypeModel langTypeModel = langTypeMapper.toDto(langType);

        restLangTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(langTypeModel)))
            .andExpect(status().isBadRequest());

        List<LangType> langTypeList = langTypeRepository.findAll();
        assertThat(langTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLangTypes() throws Exception {
        // Initialize the database
        langTypeRepository.saveAndFlush(langType);

        // Get all the langTypeList
        restLangTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(langType.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    void getLangType() throws Exception {
        // Initialize the database
        langTypeRepository.saveAndFlush(langType);

        // Get the langType
        restLangTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, langType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(langType.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getNonExistingLangType() throws Exception {
        // Get the langType
        restLangTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLangType() throws Exception {
        // Initialize the database
        langTypeRepository.saveAndFlush(langType);

        int databaseSizeBeforeUpdate = langTypeRepository.findAll().size();

        // Update the langType
        LangType updatedLangType = langTypeRepository.findById(langType.getId()).get();
        // Disconnect from session so that the updates on updatedLangType are not directly saved in db
        em.detach(updatedLangType);
        updatedLangType.code(UPDATED_CODE);
        LangTypeModel langTypeModel = langTypeMapper.toDto(updatedLangType);

        restLangTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, langTypeModel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(langTypeModel))
            )
            .andExpect(status().isOk());

        // Validate the LangType in the database
        List<LangType> langTypeList = langTypeRepository.findAll();
        assertThat(langTypeList).hasSize(databaseSizeBeforeUpdate);
        LangType testLangType = langTypeList.get(langTypeList.size() - 1);
        assertThat(testLangType.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void putNonExistingLangType() throws Exception {
        int databaseSizeBeforeUpdate = langTypeRepository.findAll().size();
        langType.setId(count.incrementAndGet());

        // Create the LangType
        LangTypeModel langTypeModel = langTypeMapper.toDto(langType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLangTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, langTypeModel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(langTypeModel))
            )
            .andExpect(status().isBadRequest());

        // Validate the LangType in the database
        List<LangType> langTypeList = langTypeRepository.findAll();
        assertThat(langTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLangType() throws Exception {
        int databaseSizeBeforeUpdate = langTypeRepository.findAll().size();
        langType.setId(count.incrementAndGet());

        // Create the LangType
        LangTypeModel langTypeModel = langTypeMapper.toDto(langType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLangTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(langTypeModel))
            )
            .andExpect(status().isBadRequest());

        // Validate the LangType in the database
        List<LangType> langTypeList = langTypeRepository.findAll();
        assertThat(langTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLangType() throws Exception {
        int databaseSizeBeforeUpdate = langTypeRepository.findAll().size();
        langType.setId(count.incrementAndGet());

        // Create the LangType
        LangTypeModel langTypeModel = langTypeMapper.toDto(langType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLangTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(langTypeModel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LangType in the database
        List<LangType> langTypeList = langTypeRepository.findAll();
        assertThat(langTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLangTypeWithPatch() throws Exception {
        // Initialize the database
        langTypeRepository.saveAndFlush(langType);

        int databaseSizeBeforeUpdate = langTypeRepository.findAll().size();

        // Update the langType using partial update
        LangType partialUpdatedLangType = new LangType();
        partialUpdatedLangType.setId(langType.getId());

        restLangTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLangType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLangType))
            )
            .andExpect(status().isOk());

        // Validate the LangType in the database
        List<LangType> langTypeList = langTypeRepository.findAll();
        assertThat(langTypeList).hasSize(databaseSizeBeforeUpdate);
        LangType testLangType = langTypeList.get(langTypeList.size() - 1);
        assertThat(testLangType.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void fullUpdateLangTypeWithPatch() throws Exception {
        // Initialize the database
        langTypeRepository.saveAndFlush(langType);

        int databaseSizeBeforeUpdate = langTypeRepository.findAll().size();

        // Update the langType using partial update
        LangType partialUpdatedLangType = new LangType();
        partialUpdatedLangType.setId(langType.getId());

        partialUpdatedLangType.code(UPDATED_CODE);

        restLangTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLangType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLangType))
            )
            .andExpect(status().isOk());

        // Validate the LangType in the database
        List<LangType> langTypeList = langTypeRepository.findAll();
        assertThat(langTypeList).hasSize(databaseSizeBeforeUpdate);
        LangType testLangType = langTypeList.get(langTypeList.size() - 1);
        assertThat(testLangType.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingLangType() throws Exception {
        int databaseSizeBeforeUpdate = langTypeRepository.findAll().size();
        langType.setId(count.incrementAndGet());

        // Create the LangType
        LangTypeModel langTypeModel = langTypeMapper.toDto(langType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLangTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, langTypeModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(langTypeModel))
            )
            .andExpect(status().isBadRequest());

        // Validate the LangType in the database
        List<LangType> langTypeList = langTypeRepository.findAll();
        assertThat(langTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLangType() throws Exception {
        int databaseSizeBeforeUpdate = langTypeRepository.findAll().size();
        langType.setId(count.incrementAndGet());

        // Create the LangType
        LangTypeModel langTypeModel = langTypeMapper.toDto(langType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLangTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(langTypeModel))
            )
            .andExpect(status().isBadRequest());

        // Validate the LangType in the database
        List<LangType> langTypeList = langTypeRepository.findAll();
        assertThat(langTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLangType() throws Exception {
        int databaseSizeBeforeUpdate = langTypeRepository.findAll().size();
        langType.setId(count.incrementAndGet());

        // Create the LangType
        LangTypeModel langTypeModel = langTypeMapper.toDto(langType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLangTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(langTypeModel))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LangType in the database
        List<LangType> langTypeList = langTypeRepository.findAll();
        assertThat(langTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLangType() throws Exception {
        // Initialize the database
        langTypeRepository.saveAndFlush(langType);

        int databaseSizeBeforeDelete = langTypeRepository.findAll().size();

        // Delete the langType
        restLangTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, langType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LangType> langTypeList = langTypeRepository.findAll();
        assertThat(langTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

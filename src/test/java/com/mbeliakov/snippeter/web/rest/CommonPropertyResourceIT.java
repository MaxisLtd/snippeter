package com.mbeliakov.snippeter.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mbeliakov.snippeter.IntegrationTest;
import com.mbeliakov.snippeter.domain.CommonProperty;
import com.mbeliakov.snippeter.repository.CommonPropertyRepository;
import com.mbeliakov.snippeter.service.dto.CommonPropertyModel;
import com.mbeliakov.snippeter.service.mapper.CommonPropertyMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link CommonPropertyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CommonPropertyResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/common-properties";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommonPropertyRepository commonPropertyRepository;

    @Autowired
    private CommonPropertyMapper commonPropertyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommonPropertyMockMvc;

    private CommonProperty commonProperty;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommonProperty createEntity(EntityManager em) {
        CommonProperty commonProperty = new CommonProperty().code(DEFAULT_CODE).value(DEFAULT_VALUE);
        return commonProperty;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommonProperty createUpdatedEntity(EntityManager em) {
        CommonProperty commonProperty = new CommonProperty().code(UPDATED_CODE).value(UPDATED_VALUE);
        return commonProperty;
    }

    @BeforeEach
    public void initTest() {
        commonProperty = createEntity(em);
    }

    @Test
    @Transactional
    void createCommonProperty() throws Exception {
        int databaseSizeBeforeCreate = commonPropertyRepository.findAll().size();
        // Create the CommonProperty
        CommonPropertyModel commonPropertyModel = commonPropertyMapper.toDto(commonProperty);
        restCommonPropertyMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commonPropertyModel))
            )
            .andExpect(status().isCreated());

        // Validate the CommonProperty in the database
        List<CommonProperty> commonPropertyList = commonPropertyRepository.findAll();
        assertThat(commonPropertyList).hasSize(databaseSizeBeforeCreate + 1);
        CommonProperty testCommonProperty = commonPropertyList.get(commonPropertyList.size() - 1);
        assertThat(testCommonProperty.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCommonProperty.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void createCommonPropertyWithExistingId() throws Exception {
        // Create the CommonProperty with an existing ID
        commonProperty.setId(1L);
        CommonPropertyModel commonPropertyModel = commonPropertyMapper.toDto(commonProperty);

        int databaseSizeBeforeCreate = commonPropertyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommonPropertyMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commonPropertyModel))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommonProperty in the database
        List<CommonProperty> commonPropertyList = commonPropertyRepository.findAll();
        assertThat(commonPropertyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = commonPropertyRepository.findAll().size();
        // set the field null
        commonProperty.setCode(null);

        // Create the CommonProperty, which fails.
        CommonPropertyModel commonPropertyModel = commonPropertyMapper.toDto(commonProperty);

        restCommonPropertyMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commonPropertyModel))
            )
            .andExpect(status().isBadRequest());

        List<CommonProperty> commonPropertyList = commonPropertyRepository.findAll();
        assertThat(commonPropertyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCommonProperties() throws Exception {
        // Initialize the database
        commonPropertyRepository.saveAndFlush(commonProperty);

        // Get all the commonPropertyList
        restCommonPropertyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commonProperty.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    @Test
    @Transactional
    void getCommonProperty() throws Exception {
        // Initialize the database
        commonPropertyRepository.saveAndFlush(commonProperty);

        // Get the commonProperty
        restCommonPropertyMockMvc
            .perform(get(ENTITY_API_URL_ID, commonProperty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(commonProperty.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCommonProperty() throws Exception {
        // Get the commonProperty
        restCommonPropertyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCommonProperty() throws Exception {
        // Initialize the database
        commonPropertyRepository.saveAndFlush(commonProperty);

        int databaseSizeBeforeUpdate = commonPropertyRepository.findAll().size();

        // Update the commonProperty
        CommonProperty updatedCommonProperty = commonPropertyRepository.findById(commonProperty.getId()).get();
        // Disconnect from session so that the updates on updatedCommonProperty are not directly saved in db
        em.detach(updatedCommonProperty);
        updatedCommonProperty.code(UPDATED_CODE).value(UPDATED_VALUE);
        CommonPropertyModel commonPropertyModel = commonPropertyMapper.toDto(updatedCommonProperty);

        restCommonPropertyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commonPropertyModel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commonPropertyModel))
            )
            .andExpect(status().isOk());

        // Validate the CommonProperty in the database
        List<CommonProperty> commonPropertyList = commonPropertyRepository.findAll();
        assertThat(commonPropertyList).hasSize(databaseSizeBeforeUpdate);
        CommonProperty testCommonProperty = commonPropertyList.get(commonPropertyList.size() - 1);
        assertThat(testCommonProperty.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCommonProperty.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingCommonProperty() throws Exception {
        int databaseSizeBeforeUpdate = commonPropertyRepository.findAll().size();
        commonProperty.setId(count.incrementAndGet());

        // Create the CommonProperty
        CommonPropertyModel commonPropertyModel = commonPropertyMapper.toDto(commonProperty);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommonPropertyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commonPropertyModel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commonPropertyModel))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommonProperty in the database
        List<CommonProperty> commonPropertyList = commonPropertyRepository.findAll();
        assertThat(commonPropertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommonProperty() throws Exception {
        int databaseSizeBeforeUpdate = commonPropertyRepository.findAll().size();
        commonProperty.setId(count.incrementAndGet());

        // Create the CommonProperty
        CommonPropertyModel commonPropertyModel = commonPropertyMapper.toDto(commonProperty);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommonPropertyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commonPropertyModel))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommonProperty in the database
        List<CommonProperty> commonPropertyList = commonPropertyRepository.findAll();
        assertThat(commonPropertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommonProperty() throws Exception {
        int databaseSizeBeforeUpdate = commonPropertyRepository.findAll().size();
        commonProperty.setId(count.incrementAndGet());

        // Create the CommonProperty
        CommonPropertyModel commonPropertyModel = commonPropertyMapper.toDto(commonProperty);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommonPropertyMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commonPropertyModel))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommonProperty in the database
        List<CommonProperty> commonPropertyList = commonPropertyRepository.findAll();
        assertThat(commonPropertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommonPropertyWithPatch() throws Exception {
        // Initialize the database
        commonPropertyRepository.saveAndFlush(commonProperty);

        int databaseSizeBeforeUpdate = commonPropertyRepository.findAll().size();

        // Update the commonProperty using partial update
        CommonProperty partialUpdatedCommonProperty = new CommonProperty();
        partialUpdatedCommonProperty.setId(commonProperty.getId());

        restCommonPropertyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommonProperty.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommonProperty))
            )
            .andExpect(status().isOk());

        // Validate the CommonProperty in the database
        List<CommonProperty> commonPropertyList = commonPropertyRepository.findAll();
        assertThat(commonPropertyList).hasSize(databaseSizeBeforeUpdate);
        CommonProperty testCommonProperty = commonPropertyList.get(commonPropertyList.size() - 1);
        assertThat(testCommonProperty.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCommonProperty.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateCommonPropertyWithPatch() throws Exception {
        // Initialize the database
        commonPropertyRepository.saveAndFlush(commonProperty);

        int databaseSizeBeforeUpdate = commonPropertyRepository.findAll().size();

        // Update the commonProperty using partial update
        CommonProperty partialUpdatedCommonProperty = new CommonProperty();
        partialUpdatedCommonProperty.setId(commonProperty.getId());

        partialUpdatedCommonProperty.code(UPDATED_CODE).value(UPDATED_VALUE);

        restCommonPropertyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommonProperty.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommonProperty))
            )
            .andExpect(status().isOk());

        // Validate the CommonProperty in the database
        List<CommonProperty> commonPropertyList = commonPropertyRepository.findAll();
        assertThat(commonPropertyList).hasSize(databaseSizeBeforeUpdate);
        CommonProperty testCommonProperty = commonPropertyList.get(commonPropertyList.size() - 1);
        assertThat(testCommonProperty.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCommonProperty.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingCommonProperty() throws Exception {
        int databaseSizeBeforeUpdate = commonPropertyRepository.findAll().size();
        commonProperty.setId(count.incrementAndGet());

        // Create the CommonProperty
        CommonPropertyModel commonPropertyModel = commonPropertyMapper.toDto(commonProperty);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommonPropertyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commonPropertyModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commonPropertyModel))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommonProperty in the database
        List<CommonProperty> commonPropertyList = commonPropertyRepository.findAll();
        assertThat(commonPropertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommonProperty() throws Exception {
        int databaseSizeBeforeUpdate = commonPropertyRepository.findAll().size();
        commonProperty.setId(count.incrementAndGet());

        // Create the CommonProperty
        CommonPropertyModel commonPropertyModel = commonPropertyMapper.toDto(commonProperty);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommonPropertyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commonPropertyModel))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommonProperty in the database
        List<CommonProperty> commonPropertyList = commonPropertyRepository.findAll();
        assertThat(commonPropertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommonProperty() throws Exception {
        int databaseSizeBeforeUpdate = commonPropertyRepository.findAll().size();
        commonProperty.setId(count.incrementAndGet());

        // Create the CommonProperty
        CommonPropertyModel commonPropertyModel = commonPropertyMapper.toDto(commonProperty);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommonPropertyMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commonPropertyModel))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommonProperty in the database
        List<CommonProperty> commonPropertyList = commonPropertyRepository.findAll();
        assertThat(commonPropertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommonProperty() throws Exception {
        // Initialize the database
        commonPropertyRepository.saveAndFlush(commonProperty);

        int databaseSizeBeforeDelete = commonPropertyRepository.findAll().size();

        // Delete the commonProperty
        restCommonPropertyMockMvc
            .perform(delete(ENTITY_API_URL_ID, commonProperty.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CommonProperty> commonPropertyList = commonPropertyRepository.findAll();
        assertThat(commonPropertyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

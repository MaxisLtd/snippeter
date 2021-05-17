package com.mbeliakov.snippeter.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mbeliakov.snippeter.IntegrationTest;
import com.mbeliakov.snippeter.domain.SnippetSection;
import com.mbeliakov.snippeter.repository.SnippetSectionRepository;
import com.mbeliakov.snippeter.service.dto.SnippetSectionModel;
import com.mbeliakov.snippeter.service.mapper.SnippetSectionMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link SnippetSectionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SnippetSectionResourceIT {

    private static final String DEFAULT_LANG_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_LANG_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final Instant DEFAULT_CTS = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CTS = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_ORDER_POSITION = 1;
    private static final Integer UPDATED_ORDER_POSITION = 2;

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    private static final String ENTITY_API_URL = "/api/snippet-sections";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SnippetSectionRepository snippetSectionRepository;

    @Autowired
    private SnippetSectionMapper snippetSectionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSnippetSectionMockMvc;

    private SnippetSection snippetSection;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SnippetSection createEntity(EntityManager em) {
        SnippetSection snippetSection = new SnippetSection()
            .langType(DEFAULT_LANG_TYPE)
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .text(DEFAULT_TEXT)
            .cts(DEFAULT_CTS)
            .orderPosition(DEFAULT_ORDER_POSITION)
            .status(DEFAULT_STATUS);
        return snippetSection;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SnippetSection createUpdatedEntity(EntityManager em) {
        SnippetSection snippetSection = new SnippetSection()
            .langType(UPDATED_LANG_TYPE)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .text(UPDATED_TEXT)
            .cts(UPDATED_CTS)
            .orderPosition(UPDATED_ORDER_POSITION)
            .status(UPDATED_STATUS);
        return snippetSection;
    }

    @BeforeEach
    public void initTest() {
        snippetSection = createEntity(em);
    }

    @Test
    @Transactional
    void createSnippetSection() throws Exception {
        int databaseSizeBeforeCreate = snippetSectionRepository.findAll().size();
        // Create the SnippetSection
        SnippetSectionModel snippetSectionModel = snippetSectionMapper.toDto(snippetSection);
        restSnippetSectionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(snippetSectionModel))
            )
            .andExpect(status().isCreated());

        // Validate the SnippetSection in the database
        List<SnippetSection> snippetSectionList = snippetSectionRepository.findAll();
        assertThat(snippetSectionList).hasSize(databaseSizeBeforeCreate + 1);
        SnippetSection testSnippetSection = snippetSectionList.get(snippetSectionList.size() - 1);
        assertThat(testSnippetSection.getLangType()).isEqualTo(DEFAULT_LANG_TYPE);
        assertThat(testSnippetSection.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSnippetSection.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSnippetSection.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testSnippetSection.getCts()).isEqualTo(DEFAULT_CTS);
        assertThat(testSnippetSection.getOrderPosition()).isEqualTo(DEFAULT_ORDER_POSITION);
        assertThat(testSnippetSection.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createSnippetSectionWithExistingId() throws Exception {
        // Create the SnippetSection with an existing ID
        snippetSection.setId(1L);
        SnippetSectionModel snippetSectionModel = snippetSectionMapper.toDto(snippetSection);

        int databaseSizeBeforeCreate = snippetSectionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSnippetSectionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(snippetSectionModel))
            )
            .andExpect(status().isBadRequest());

        // Validate the SnippetSection in the database
        List<SnippetSection> snippetSectionList = snippetSectionRepository.findAll();
        assertThat(snippetSectionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = snippetSectionRepository.findAll().size();
        // set the field null
        snippetSection.setTitle(null);

        // Create the SnippetSection, which fails.
        SnippetSectionModel snippetSectionModel = snippetSectionMapper.toDto(snippetSection);

        restSnippetSectionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(snippetSectionModel))
            )
            .andExpect(status().isBadRequest());

        List<SnippetSection> snippetSectionList = snippetSectionRepository.findAll();
        assertThat(snippetSectionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCtsIsRequired() throws Exception {
        int databaseSizeBeforeTest = snippetSectionRepository.findAll().size();
        // set the field null
        snippetSection.setCts(null);

        // Create the SnippetSection, which fails.
        SnippetSectionModel snippetSectionModel = snippetSectionMapper.toDto(snippetSection);

        restSnippetSectionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(snippetSectionModel))
            )
            .andExpect(status().isBadRequest());

        List<SnippetSection> snippetSectionList = snippetSectionRepository.findAll();
        assertThat(snippetSectionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOrderPositionIsRequired() throws Exception {
        int databaseSizeBeforeTest = snippetSectionRepository.findAll().size();
        // set the field null
        snippetSection.setOrderPosition(null);

        // Create the SnippetSection, which fails.
        SnippetSectionModel snippetSectionModel = snippetSectionMapper.toDto(snippetSection);

        restSnippetSectionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(snippetSectionModel))
            )
            .andExpect(status().isBadRequest());

        List<SnippetSection> snippetSectionList = snippetSectionRepository.findAll();
        assertThat(snippetSectionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = snippetSectionRepository.findAll().size();
        // set the field null
        snippetSection.setStatus(null);

        // Create the SnippetSection, which fails.
        SnippetSectionModel snippetSectionModel = snippetSectionMapper.toDto(snippetSection);

        restSnippetSectionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(snippetSectionModel))
            )
            .andExpect(status().isBadRequest());

        List<SnippetSection> snippetSectionList = snippetSectionRepository.findAll();
        assertThat(snippetSectionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSnippetSections() throws Exception {
        // Initialize the database
        snippetSectionRepository.saveAndFlush(snippetSection);

        // Get all the snippetSectionList
        restSnippetSectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(snippetSection.getId().intValue())))
            .andExpect(jsonPath("$.[*].langType").value(hasItem(DEFAULT_LANG_TYPE)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())))
            .andExpect(jsonPath("$.[*].cts").value(hasItem(DEFAULT_CTS.toString())))
            .andExpect(jsonPath("$.[*].orderPosition").value(hasItem(DEFAULT_ORDER_POSITION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    void getSnippetSection() throws Exception {
        // Initialize the database
        snippetSectionRepository.saveAndFlush(snippetSection);

        // Get the snippetSection
        restSnippetSectionMockMvc
            .perform(get(ENTITY_API_URL_ID, snippetSection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(snippetSection.getId().intValue()))
            .andExpect(jsonPath("$.langType").value(DEFAULT_LANG_TYPE))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()))
            .andExpect(jsonPath("$.cts").value(DEFAULT_CTS.toString()))
            .andExpect(jsonPath("$.orderPosition").value(DEFAULT_ORDER_POSITION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingSnippetSection() throws Exception {
        // Get the snippetSection
        restSnippetSectionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSnippetSection() throws Exception {
        // Initialize the database
        snippetSectionRepository.saveAndFlush(snippetSection);

        int databaseSizeBeforeUpdate = snippetSectionRepository.findAll().size();

        // Update the snippetSection
        SnippetSection updatedSnippetSection = snippetSectionRepository.findById(snippetSection.getId()).get();
        // Disconnect from session so that the updates on updatedSnippetSection are not directly saved in db
        em.detach(updatedSnippetSection);
        updatedSnippetSection
            .langType(UPDATED_LANG_TYPE)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .text(UPDATED_TEXT)
            .cts(UPDATED_CTS)
            .orderPosition(UPDATED_ORDER_POSITION)
            .status(UPDATED_STATUS);
        SnippetSectionModel snippetSectionModel = snippetSectionMapper.toDto(updatedSnippetSection);

        restSnippetSectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, snippetSectionModel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(snippetSectionModel))
            )
            .andExpect(status().isOk());

        // Validate the SnippetSection in the database
        List<SnippetSection> snippetSectionList = snippetSectionRepository.findAll();
        assertThat(snippetSectionList).hasSize(databaseSizeBeforeUpdate);
        SnippetSection testSnippetSection = snippetSectionList.get(snippetSectionList.size() - 1);
        assertThat(testSnippetSection.getLangType()).isEqualTo(UPDATED_LANG_TYPE);
        assertThat(testSnippetSection.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSnippetSection.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSnippetSection.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testSnippetSection.getCts()).isEqualTo(UPDATED_CTS);
        assertThat(testSnippetSection.getOrderPosition()).isEqualTo(UPDATED_ORDER_POSITION);
        assertThat(testSnippetSection.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingSnippetSection() throws Exception {
        int databaseSizeBeforeUpdate = snippetSectionRepository.findAll().size();
        snippetSection.setId(count.incrementAndGet());

        // Create the SnippetSection
        SnippetSectionModel snippetSectionModel = snippetSectionMapper.toDto(snippetSection);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSnippetSectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, snippetSectionModel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(snippetSectionModel))
            )
            .andExpect(status().isBadRequest());

        // Validate the SnippetSection in the database
        List<SnippetSection> snippetSectionList = snippetSectionRepository.findAll();
        assertThat(snippetSectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSnippetSection() throws Exception {
        int databaseSizeBeforeUpdate = snippetSectionRepository.findAll().size();
        snippetSection.setId(count.incrementAndGet());

        // Create the SnippetSection
        SnippetSectionModel snippetSectionModel = snippetSectionMapper.toDto(snippetSection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSnippetSectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(snippetSectionModel))
            )
            .andExpect(status().isBadRequest());

        // Validate the SnippetSection in the database
        List<SnippetSection> snippetSectionList = snippetSectionRepository.findAll();
        assertThat(snippetSectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSnippetSection() throws Exception {
        int databaseSizeBeforeUpdate = snippetSectionRepository.findAll().size();
        snippetSection.setId(count.incrementAndGet());

        // Create the SnippetSection
        SnippetSectionModel snippetSectionModel = snippetSectionMapper.toDto(snippetSection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSnippetSectionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(snippetSectionModel))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SnippetSection in the database
        List<SnippetSection> snippetSectionList = snippetSectionRepository.findAll();
        assertThat(snippetSectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSnippetSectionWithPatch() throws Exception {
        // Initialize the database
        snippetSectionRepository.saveAndFlush(snippetSection);

        int databaseSizeBeforeUpdate = snippetSectionRepository.findAll().size();

        // Update the snippetSection using partial update
        SnippetSection partialUpdatedSnippetSection = new SnippetSection();
        partialUpdatedSnippetSection.setId(snippetSection.getId());

        partialUpdatedSnippetSection
            .langType(UPDATED_LANG_TYPE)
            .description(UPDATED_DESCRIPTION)
            .text(UPDATED_TEXT)
            .orderPosition(UPDATED_ORDER_POSITION)
            .status(UPDATED_STATUS);

        restSnippetSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSnippetSection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSnippetSection))
            )
            .andExpect(status().isOk());

        // Validate the SnippetSection in the database
        List<SnippetSection> snippetSectionList = snippetSectionRepository.findAll();
        assertThat(snippetSectionList).hasSize(databaseSizeBeforeUpdate);
        SnippetSection testSnippetSection = snippetSectionList.get(snippetSectionList.size() - 1);
        assertThat(testSnippetSection.getLangType()).isEqualTo(UPDATED_LANG_TYPE);
        assertThat(testSnippetSection.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSnippetSection.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSnippetSection.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testSnippetSection.getCts()).isEqualTo(DEFAULT_CTS);
        assertThat(testSnippetSection.getOrderPosition()).isEqualTo(UPDATED_ORDER_POSITION);
        assertThat(testSnippetSection.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateSnippetSectionWithPatch() throws Exception {
        // Initialize the database
        snippetSectionRepository.saveAndFlush(snippetSection);

        int databaseSizeBeforeUpdate = snippetSectionRepository.findAll().size();

        // Update the snippetSection using partial update
        SnippetSection partialUpdatedSnippetSection = new SnippetSection();
        partialUpdatedSnippetSection.setId(snippetSection.getId());

        partialUpdatedSnippetSection
            .langType(UPDATED_LANG_TYPE)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .text(UPDATED_TEXT)
            .cts(UPDATED_CTS)
            .orderPosition(UPDATED_ORDER_POSITION)
            .status(UPDATED_STATUS);

        restSnippetSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSnippetSection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSnippetSection))
            )
            .andExpect(status().isOk());

        // Validate the SnippetSection in the database
        List<SnippetSection> snippetSectionList = snippetSectionRepository.findAll();
        assertThat(snippetSectionList).hasSize(databaseSizeBeforeUpdate);
        SnippetSection testSnippetSection = snippetSectionList.get(snippetSectionList.size() - 1);
        assertThat(testSnippetSection.getLangType()).isEqualTo(UPDATED_LANG_TYPE);
        assertThat(testSnippetSection.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSnippetSection.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSnippetSection.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testSnippetSection.getCts()).isEqualTo(UPDATED_CTS);
        assertThat(testSnippetSection.getOrderPosition()).isEqualTo(UPDATED_ORDER_POSITION);
        assertThat(testSnippetSection.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingSnippetSection() throws Exception {
        int databaseSizeBeforeUpdate = snippetSectionRepository.findAll().size();
        snippetSection.setId(count.incrementAndGet());

        // Create the SnippetSection
        SnippetSectionModel snippetSectionModel = snippetSectionMapper.toDto(snippetSection);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSnippetSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, snippetSectionModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(snippetSectionModel))
            )
            .andExpect(status().isBadRequest());

        // Validate the SnippetSection in the database
        List<SnippetSection> snippetSectionList = snippetSectionRepository.findAll();
        assertThat(snippetSectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSnippetSection() throws Exception {
        int databaseSizeBeforeUpdate = snippetSectionRepository.findAll().size();
        snippetSection.setId(count.incrementAndGet());

        // Create the SnippetSection
        SnippetSectionModel snippetSectionModel = snippetSectionMapper.toDto(snippetSection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSnippetSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(snippetSectionModel))
            )
            .andExpect(status().isBadRequest());

        // Validate the SnippetSection in the database
        List<SnippetSection> snippetSectionList = snippetSectionRepository.findAll();
        assertThat(snippetSectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSnippetSection() throws Exception {
        int databaseSizeBeforeUpdate = snippetSectionRepository.findAll().size();
        snippetSection.setId(count.incrementAndGet());

        // Create the SnippetSection
        SnippetSectionModel snippetSectionModel = snippetSectionMapper.toDto(snippetSection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSnippetSectionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(snippetSectionModel))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SnippetSection in the database
        List<SnippetSection> snippetSectionList = snippetSectionRepository.findAll();
        assertThat(snippetSectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSnippetSection() throws Exception {
        // Initialize the database
        snippetSectionRepository.saveAndFlush(snippetSection);

        int databaseSizeBeforeDelete = snippetSectionRepository.findAll().size();

        // Delete the snippetSection
        restSnippetSectionMockMvc
            .perform(delete(ENTITY_API_URL_ID, snippetSection.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SnippetSection> snippetSectionList = snippetSectionRepository.findAll();
        assertThat(snippetSectionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

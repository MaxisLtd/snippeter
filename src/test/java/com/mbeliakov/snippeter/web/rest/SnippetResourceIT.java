package com.mbeliakov.snippeter.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mbeliakov.snippeter.IntegrationTest;
import com.mbeliakov.snippeter.domain.Snippet;
import com.mbeliakov.snippeter.repository.SnippetRepository;
import com.mbeliakov.snippeter.service.dto.SnippetModel;
import com.mbeliakov.snippeter.service.mapper.SnippetMapper;
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
 * Integration tests for the {@link SnippetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SnippetResourceIT {

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

    private static final String ENTITY_API_URL = "/api/snippets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SnippetRepository snippetRepository;

    @Autowired
    private SnippetMapper snippetMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSnippetMockMvc;

    private Snippet snippet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Snippet createEntity(EntityManager em) {
        Snippet snippet = new Snippet()
            .langType(DEFAULT_LANG_TYPE)
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .text(DEFAULT_TEXT)
            .cts(DEFAULT_CTS);
        return snippet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Snippet createUpdatedEntity(EntityManager em) {
        Snippet snippet = new Snippet()
            .langType(UPDATED_LANG_TYPE)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .text(UPDATED_TEXT)
            .cts(UPDATED_CTS);
        return snippet;
    }

    @BeforeEach
    public void initTest() {
        snippet = createEntity(em);
    }

    @Test
    @Transactional
    void createSnippet() throws Exception {
        int databaseSizeBeforeCreate = snippetRepository.findAll().size();
        // Create the Snippet
        SnippetModel snippetModel = snippetMapper.toDto(snippet);
        restSnippetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(snippetModel)))
            .andExpect(status().isCreated());

        // Validate the Snippet in the database
        List<Snippet> snippetList = snippetRepository.findAll();
        assertThat(snippetList).hasSize(databaseSizeBeforeCreate + 1);
        Snippet testSnippet = snippetList.get(snippetList.size() - 1);
        assertThat(testSnippet.getLangType()).isEqualTo(DEFAULT_LANG_TYPE);
        assertThat(testSnippet.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSnippet.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSnippet.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testSnippet.getCts()).isEqualTo(DEFAULT_CTS);
    }

    @Test
    @Transactional
    void createSnippetWithExistingId() throws Exception {
        // Create the Snippet with an existing ID
        snippet.setId(1L);
        SnippetModel snippetModel = snippetMapper.toDto(snippet);

        int databaseSizeBeforeCreate = snippetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSnippetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(snippetModel)))
            .andExpect(status().isBadRequest());

        // Validate the Snippet in the database
        List<Snippet> snippetList = snippetRepository.findAll();
        assertThat(snippetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = snippetRepository.findAll().size();
        // set the field null
        snippet.setTitle(null);

        // Create the Snippet, which fails.
        SnippetModel snippetModel = snippetMapper.toDto(snippet);

        restSnippetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(snippetModel)))
            .andExpect(status().isBadRequest());

        List<Snippet> snippetList = snippetRepository.findAll();
        assertThat(snippetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCtsIsRequired() throws Exception {
        int databaseSizeBeforeTest = snippetRepository.findAll().size();
        // set the field null
        snippet.setCts(null);

        // Create the Snippet, which fails.
        SnippetModel snippetModel = snippetMapper.toDto(snippet);

        restSnippetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(snippetModel)))
            .andExpect(status().isBadRequest());

        List<Snippet> snippetList = snippetRepository.findAll();
        assertThat(snippetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSnippets() throws Exception {
        // Initialize the database
        snippetRepository.saveAndFlush(snippet);

        // Get all the snippetList
        restSnippetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(snippet.getId().intValue())))
            .andExpect(jsonPath("$.[*].langType").value(hasItem(DEFAULT_LANG_TYPE)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())))
            .andExpect(jsonPath("$.[*].cts").value(hasItem(DEFAULT_CTS.toString())));
    }

    @Test
    @Transactional
    void getSnippet() throws Exception {
        // Initialize the database
        snippetRepository.saveAndFlush(snippet);

        // Get the snippet
        restSnippetMockMvc
            .perform(get(ENTITY_API_URL_ID, snippet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(snippet.getId().intValue()))
            .andExpect(jsonPath("$.langType").value(DEFAULT_LANG_TYPE))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()))
            .andExpect(jsonPath("$.cts").value(DEFAULT_CTS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSnippet() throws Exception {
        // Get the snippet
        restSnippetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSnippet() throws Exception {
        // Initialize the database
        snippetRepository.saveAndFlush(snippet);

        int databaseSizeBeforeUpdate = snippetRepository.findAll().size();

        // Update the snippet
        Snippet updatedSnippet = snippetRepository.findById(snippet.getId()).get();
        // Disconnect from session so that the updates on updatedSnippet are not directly saved in db
        em.detach(updatedSnippet);
        updatedSnippet
            .langType(UPDATED_LANG_TYPE)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .text(UPDATED_TEXT)
            .cts(UPDATED_CTS);
        SnippetModel snippetModel = snippetMapper.toDto(updatedSnippet);

        restSnippetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, snippetModel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(snippetModel))
            )
            .andExpect(status().isOk());

        // Validate the Snippet in the database
        List<Snippet> snippetList = snippetRepository.findAll();
        assertThat(snippetList).hasSize(databaseSizeBeforeUpdate);
        Snippet testSnippet = snippetList.get(snippetList.size() - 1);
        assertThat(testSnippet.getLangType()).isEqualTo(UPDATED_LANG_TYPE);
        assertThat(testSnippet.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSnippet.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSnippet.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testSnippet.getCts()).isEqualTo(UPDATED_CTS);
    }

    @Test
    @Transactional
    void putNonExistingSnippet() throws Exception {
        int databaseSizeBeforeUpdate = snippetRepository.findAll().size();
        snippet.setId(count.incrementAndGet());

        // Create the Snippet
        SnippetModel snippetModel = snippetMapper.toDto(snippet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSnippetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, snippetModel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(snippetModel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Snippet in the database
        List<Snippet> snippetList = snippetRepository.findAll();
        assertThat(snippetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSnippet() throws Exception {
        int databaseSizeBeforeUpdate = snippetRepository.findAll().size();
        snippet.setId(count.incrementAndGet());

        // Create the Snippet
        SnippetModel snippetModel = snippetMapper.toDto(snippet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSnippetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(snippetModel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Snippet in the database
        List<Snippet> snippetList = snippetRepository.findAll();
        assertThat(snippetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSnippet() throws Exception {
        int databaseSizeBeforeUpdate = snippetRepository.findAll().size();
        snippet.setId(count.incrementAndGet());

        // Create the Snippet
        SnippetModel snippetModel = snippetMapper.toDto(snippet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSnippetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(snippetModel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Snippet in the database
        List<Snippet> snippetList = snippetRepository.findAll();
        assertThat(snippetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSnippetWithPatch() throws Exception {
        // Initialize the database
        snippetRepository.saveAndFlush(snippet);

        int databaseSizeBeforeUpdate = snippetRepository.findAll().size();

        // Update the snippet using partial update
        Snippet partialUpdatedSnippet = new Snippet();
        partialUpdatedSnippet.setId(snippet.getId());

        partialUpdatedSnippet.langType(UPDATED_LANG_TYPE);

        restSnippetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSnippet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSnippet))
            )
            .andExpect(status().isOk());

        // Validate the Snippet in the database
        List<Snippet> snippetList = snippetRepository.findAll();
        assertThat(snippetList).hasSize(databaseSizeBeforeUpdate);
        Snippet testSnippet = snippetList.get(snippetList.size() - 1);
        assertThat(testSnippet.getLangType()).isEqualTo(UPDATED_LANG_TYPE);
        assertThat(testSnippet.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSnippet.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSnippet.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testSnippet.getCts()).isEqualTo(DEFAULT_CTS);
    }

    @Test
    @Transactional
    void fullUpdateSnippetWithPatch() throws Exception {
        // Initialize the database
        snippetRepository.saveAndFlush(snippet);

        int databaseSizeBeforeUpdate = snippetRepository.findAll().size();

        // Update the snippet using partial update
        Snippet partialUpdatedSnippet = new Snippet();
        partialUpdatedSnippet.setId(snippet.getId());

        partialUpdatedSnippet
            .langType(UPDATED_LANG_TYPE)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .text(UPDATED_TEXT)
            .cts(UPDATED_CTS);

        restSnippetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSnippet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSnippet))
            )
            .andExpect(status().isOk());

        // Validate the Snippet in the database
        List<Snippet> snippetList = snippetRepository.findAll();
        assertThat(snippetList).hasSize(databaseSizeBeforeUpdate);
        Snippet testSnippet = snippetList.get(snippetList.size() - 1);
        assertThat(testSnippet.getLangType()).isEqualTo(UPDATED_LANG_TYPE);
        assertThat(testSnippet.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSnippet.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSnippet.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testSnippet.getCts()).isEqualTo(UPDATED_CTS);
    }

    @Test
    @Transactional
    void patchNonExistingSnippet() throws Exception {
        int databaseSizeBeforeUpdate = snippetRepository.findAll().size();
        snippet.setId(count.incrementAndGet());

        // Create the Snippet
        SnippetModel snippetModel = snippetMapper.toDto(snippet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSnippetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, snippetModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(snippetModel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Snippet in the database
        List<Snippet> snippetList = snippetRepository.findAll();
        assertThat(snippetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSnippet() throws Exception {
        int databaseSizeBeforeUpdate = snippetRepository.findAll().size();
        snippet.setId(count.incrementAndGet());

        // Create the Snippet
        SnippetModel snippetModel = snippetMapper.toDto(snippet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSnippetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(snippetModel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Snippet in the database
        List<Snippet> snippetList = snippetRepository.findAll();
        assertThat(snippetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSnippet() throws Exception {
        int databaseSizeBeforeUpdate = snippetRepository.findAll().size();
        snippet.setId(count.incrementAndGet());

        // Create the Snippet
        SnippetModel snippetModel = snippetMapper.toDto(snippet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSnippetMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(snippetModel))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Snippet in the database
        List<Snippet> snippetList = snippetRepository.findAll();
        assertThat(snippetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSnippet() throws Exception {
        // Initialize the database
        snippetRepository.saveAndFlush(snippet);

        int databaseSizeBeforeDelete = snippetRepository.findAll().size();

        // Delete the snippet
        restSnippetMockMvc
            .perform(delete(ENTITY_API_URL_ID, snippet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Snippet> snippetList = snippetRepository.findAll();
        assertThat(snippetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

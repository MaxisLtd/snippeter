package com.mbeliakov.snippeter.web.rest;

import com.mbeliakov.snippeter.repository.SnippetSectionRepository;
import com.mbeliakov.snippeter.service.SnippetSectionService;
import com.mbeliakov.snippeter.service.dto.SnippetSectionModel;
import com.mbeliakov.snippeter.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mbeliakov.snippeter.domain.SnippetSection}.
 */
@RestController
@RequestMapping("/api")
public class SnippetSectionResource {

    private final Logger log = LoggerFactory.getLogger(SnippetSectionResource.class);

    private static final String ENTITY_NAME = "snippetSection";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SnippetSectionService snippetSectionService;

    private final SnippetSectionRepository snippetSectionRepository;

    public SnippetSectionResource(SnippetSectionService snippetSectionService, SnippetSectionRepository snippetSectionRepository) {
        this.snippetSectionService = snippetSectionService;
        this.snippetSectionRepository = snippetSectionRepository;
    }

    /**
     * {@code POST  /snippet-sections} : Create a new snippetSection.
     *
     * @param snippetSectionModel the snippetSectionModel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new snippetSectionModel, or with status {@code 400 (Bad Request)} if the snippetSection has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/snippet-sections")
    public ResponseEntity<SnippetSectionModel> createSnippetSection(@Valid @RequestBody SnippetSectionModel snippetSectionModel)
        throws URISyntaxException {
        log.debug("REST request to save SnippetSection : {}", snippetSectionModel);
        if (snippetSectionModel.getId() != null) {
            throw new BadRequestAlertException("A new snippetSection cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SnippetSectionModel result = snippetSectionService.save(snippetSectionModel);
        return ResponseEntity
            .created(new URI("/api/snippet-sections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /snippet-sections/:id} : Updates an existing snippetSection.
     *
     * @param id the id of the snippetSectionModel to save.
     * @param snippetSectionModel the snippetSectionModel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated snippetSectionModel,
     * or with status {@code 400 (Bad Request)} if the snippetSectionModel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the snippetSectionModel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/snippet-sections/{id}")
    public ResponseEntity<SnippetSectionModel> updateSnippetSection(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SnippetSectionModel snippetSectionModel
    ) throws URISyntaxException {
        log.debug("REST request to update SnippetSection : {}, {}", id, snippetSectionModel);
        if (snippetSectionModel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, snippetSectionModel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!snippetSectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SnippetSectionModel result = snippetSectionService.save(snippetSectionModel);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, snippetSectionModel.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /snippet-sections/:id} : Partial updates given fields of an existing snippetSection, field will ignore if it is null
     *
     * @param id the id of the snippetSectionModel to save.
     * @param snippetSectionModel the snippetSectionModel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated snippetSectionModel,
     * or with status {@code 400 (Bad Request)} if the snippetSectionModel is not valid,
     * or with status {@code 404 (Not Found)} if the snippetSectionModel is not found,
     * or with status {@code 500 (Internal Server Error)} if the snippetSectionModel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/snippet-sections/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SnippetSectionModel> partialUpdateSnippetSection(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SnippetSectionModel snippetSectionModel
    ) throws URISyntaxException {
        log.debug("REST request to partial update SnippetSection partially : {}, {}", id, snippetSectionModel);
        if (snippetSectionModel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, snippetSectionModel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!snippetSectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SnippetSectionModel> result = snippetSectionService.partialUpdate(snippetSectionModel);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, snippetSectionModel.getId().toString())
        );
    }

    /**
     * {@code GET  /snippet-sections} : get all the snippetSections.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of snippetSections in body.
     */
    @GetMapping("/snippet-sections")
    public List<SnippetSectionModel> getAllSnippetSections() {
        log.debug("REST request to get all SnippetSections");
        return snippetSectionService.findAll();
    }

    /**
     * {@code GET  /snippet-sections/:id} : get the "id" snippetSection.
     *
     * @param id the id of the snippetSectionModel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the snippetSectionModel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/snippet-sections/{id}")
    public ResponseEntity<SnippetSectionModel> getSnippetSection(@PathVariable Long id) {
        log.debug("REST request to get SnippetSection : {}", id);
        Optional<SnippetSectionModel> snippetSectionModel = snippetSectionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(snippetSectionModel);
    }

    /**
     * {@code DELETE  /snippet-sections/:id} : delete the "id" snippetSection.
     *
     * @param id the id of the snippetSectionModel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/snippet-sections/{id}")
    public ResponseEntity<Void> deleteSnippetSection(@PathVariable Long id) {
        log.debug("REST request to delete SnippetSection : {}", id);
        snippetSectionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

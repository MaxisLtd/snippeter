package com.mbeliakov.snippeter.web.rest;

import com.mbeliakov.snippeter.repository.SnippetRepository;
import com.mbeliakov.snippeter.service.SnippetService;
import com.mbeliakov.snippeter.service.dto.SnippetModel;
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
 * REST controller for managing {@link com.mbeliakov.snippeter.domain.Snippet}.
 */
@RestController
@RequestMapping("/api")
public class SnippetResource {

    private final Logger log = LoggerFactory.getLogger(SnippetResource.class);

    private static final String ENTITY_NAME = "snippet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SnippetService snippetService;

    private final SnippetRepository snippetRepository;

    public SnippetResource(SnippetService snippetService, SnippetRepository snippetRepository) {
        this.snippetService = snippetService;
        this.snippetRepository = snippetRepository;
    }

    /**
     * {@code POST  /snippets} : Create a new snippet.
     *
     * @param snippetModel the snippetModel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new snippetModel, or with status {@code 400 (Bad Request)} if the snippet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/snippets")
    public ResponseEntity<SnippetModel> createSnippet(@Valid @RequestBody SnippetModel snippetModel) throws URISyntaxException {
        log.debug("REST request to save Snippet : {}", snippetModel);
        if (snippetModel.getId() != null) {
            throw new BadRequestAlertException("A new snippet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SnippetModel result = snippetService.save(snippetModel);
        return ResponseEntity
            .created(new URI("/api/snippets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /snippets/:id} : Updates an existing snippet.
     *
     * @param id the id of the snippetModel to save.
     * @param snippetModel the snippetModel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated snippetModel,
     * or with status {@code 400 (Bad Request)} if the snippetModel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the snippetModel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/snippets/{id}")
    public ResponseEntity<SnippetModel> updateSnippet(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SnippetModel snippetModel
    ) throws URISyntaxException {
        log.debug("REST request to update Snippet : {}, {}", id, snippetModel);
        if (snippetModel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, snippetModel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!snippetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SnippetModel result = snippetService.save(snippetModel);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, snippetModel.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /snippets/:id} : Partial updates given fields of an existing snippet, field will ignore if it is null
     *
     * @param id the id of the snippetModel to save.
     * @param snippetModel the snippetModel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated snippetModel,
     * or with status {@code 400 (Bad Request)} if the snippetModel is not valid,
     * or with status {@code 404 (Not Found)} if the snippetModel is not found,
     * or with status {@code 500 (Internal Server Error)} if the snippetModel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/snippets/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SnippetModel> partialUpdateSnippet(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SnippetModel snippetModel
    ) throws URISyntaxException {
        log.debug("REST request to partial update Snippet partially : {}, {}", id, snippetModel);
        if (snippetModel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, snippetModel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!snippetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SnippetModel> result = snippetService.partialUpdate(snippetModel);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, snippetModel.getId().toString())
        );
    }

    /**
     * {@code GET  /snippets} : get all the snippets.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of snippets in body.
     */
    @GetMapping("/snippets")
    public List<SnippetModel> getAllSnippets() {
        log.debug("REST request to get all Snippets");
        return snippetService.findAll();
    }

    /**
     * {@code GET  /snippets/:id} : get the "id" snippet.
     *
     * @param id the id of the snippetModel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the snippetModel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/snippets/{id}")
    public ResponseEntity<SnippetModel> getSnippet(@PathVariable Long id) {
        log.debug("REST request to get Snippet : {}", id);
        Optional<SnippetModel> snippetModel = snippetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(snippetModel);
    }

    /**
     * {@code DELETE  /snippets/:id} : delete the "id" snippet.
     *
     * @param id the id of the snippetModel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/snippets/{id}")
    public ResponseEntity<Void> deleteSnippet(@PathVariable Long id) {
        log.debug("REST request to delete Snippet : {}", id);
        snippetService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

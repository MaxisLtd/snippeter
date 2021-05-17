package com.mbeliakov.snippeter.web.rest;

import com.mbeliakov.snippeter.repository.LangTypeRepository;
import com.mbeliakov.snippeter.service.LangTypeService;
import com.mbeliakov.snippeter.service.dto.LangTypeModel;
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
 * REST controller for managing {@link com.mbeliakov.snippeter.domain.LangType}.
 */
@RestController
@RequestMapping("/api")
public class LangTypeResource {

    private final Logger log = LoggerFactory.getLogger(LangTypeResource.class);

    private static final String ENTITY_NAME = "langType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LangTypeService langTypeService;

    private final LangTypeRepository langTypeRepository;

    public LangTypeResource(LangTypeService langTypeService, LangTypeRepository langTypeRepository) {
        this.langTypeService = langTypeService;
        this.langTypeRepository = langTypeRepository;
    }

    /**
     * {@code POST  /lang-types} : Create a new langType.
     *
     * @param langTypeModel the langTypeModel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new langTypeModel, or with status {@code 400 (Bad Request)} if the langType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lang-types")
    public ResponseEntity<LangTypeModel> createLangType(@Valid @RequestBody LangTypeModel langTypeModel) throws URISyntaxException {
        log.debug("REST request to save LangType : {}", langTypeModel);
        if (langTypeModel.getId() != null) {
            throw new BadRequestAlertException("A new langType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LangTypeModel result = langTypeService.save(langTypeModel);
        return ResponseEntity
            .created(new URI("/api/lang-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lang-types/:id} : Updates an existing langType.
     *
     * @param id the id of the langTypeModel to save.
     * @param langTypeModel the langTypeModel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated langTypeModel,
     * or with status {@code 400 (Bad Request)} if the langTypeModel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the langTypeModel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lang-types/{id}")
    public ResponseEntity<LangTypeModel> updateLangType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LangTypeModel langTypeModel
    ) throws URISyntaxException {
        log.debug("REST request to update LangType : {}, {}", id, langTypeModel);
        if (langTypeModel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, langTypeModel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!langTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LangTypeModel result = langTypeService.save(langTypeModel);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, langTypeModel.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /lang-types/:id} : Partial updates given fields of an existing langType, field will ignore if it is null
     *
     * @param id the id of the langTypeModel to save.
     * @param langTypeModel the langTypeModel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated langTypeModel,
     * or with status {@code 400 (Bad Request)} if the langTypeModel is not valid,
     * or with status {@code 404 (Not Found)} if the langTypeModel is not found,
     * or with status {@code 500 (Internal Server Error)} if the langTypeModel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lang-types/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LangTypeModel> partialUpdateLangType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LangTypeModel langTypeModel
    ) throws URISyntaxException {
        log.debug("REST request to partial update LangType partially : {}, {}", id, langTypeModel);
        if (langTypeModel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, langTypeModel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!langTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LangTypeModel> result = langTypeService.partialUpdate(langTypeModel);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, langTypeModel.getId().toString())
        );
    }

    /**
     * {@code GET  /lang-types} : get all the langTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of langTypes in body.
     */
    @GetMapping("/lang-types")
    public List<LangTypeModel> getAllLangTypes() {
        log.debug("REST request to get all LangTypes");
        return langTypeService.findAll();
    }

    /**
     * {@code GET  /lang-types/:id} : get the "id" langType.
     *
     * @param id the id of the langTypeModel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the langTypeModel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lang-types/{id}")
    public ResponseEntity<LangTypeModel> getLangType(@PathVariable Long id) {
        log.debug("REST request to get LangType : {}", id);
        Optional<LangTypeModel> langTypeModel = langTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(langTypeModel);
    }

    /**
     * {@code DELETE  /lang-types/:id} : delete the "id" langType.
     *
     * @param id the id of the langTypeModel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lang-types/{id}")
    public ResponseEntity<Void> deleteLangType(@PathVariable Long id) {
        log.debug("REST request to delete LangType : {}", id);
        langTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

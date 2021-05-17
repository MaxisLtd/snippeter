package com.mbeliakov.snippeter.web.rest;

import com.mbeliakov.snippeter.repository.CommonPropertyRepository;
import com.mbeliakov.snippeter.service.CommonPropertyService;
import com.mbeliakov.snippeter.service.dto.CommonPropertyModel;
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
 * REST controller for managing {@link com.mbeliakov.snippeter.domain.CommonProperty}.
 */
@RestController
@RequestMapping("/api")
public class CommonPropertyResource {

    private final Logger log = LoggerFactory.getLogger(CommonPropertyResource.class);

    private static final String ENTITY_NAME = "commonProperty";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommonPropertyService commonPropertyService;

    private final CommonPropertyRepository commonPropertyRepository;

    public CommonPropertyResource(CommonPropertyService commonPropertyService, CommonPropertyRepository commonPropertyRepository) {
        this.commonPropertyService = commonPropertyService;
        this.commonPropertyRepository = commonPropertyRepository;
    }

    /**
     * {@code POST  /common-properties} : Create a new commonProperty.
     *
     * @param commonPropertyModel the commonPropertyModel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commonPropertyModel, or with status {@code 400 (Bad Request)} if the commonProperty has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/common-properties")
    public ResponseEntity<CommonPropertyModel> createCommonProperty(@Valid @RequestBody CommonPropertyModel commonPropertyModel)
        throws URISyntaxException {
        log.debug("REST request to save CommonProperty : {}", commonPropertyModel);
        if (commonPropertyModel.getId() != null) {
            throw new BadRequestAlertException("A new commonProperty cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CommonPropertyModel result = commonPropertyService.save(commonPropertyModel);
        return ResponseEntity
            .created(new URI("/api/common-properties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /common-properties/:id} : Updates an existing commonProperty.
     *
     * @param id the id of the commonPropertyModel to save.
     * @param commonPropertyModel the commonPropertyModel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commonPropertyModel,
     * or with status {@code 400 (Bad Request)} if the commonPropertyModel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commonPropertyModel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/common-properties/{id}")
    public ResponseEntity<CommonPropertyModel> updateCommonProperty(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CommonPropertyModel commonPropertyModel
    ) throws URISyntaxException {
        log.debug("REST request to update CommonProperty : {}, {}", id, commonPropertyModel);
        if (commonPropertyModel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commonPropertyModel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commonPropertyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CommonPropertyModel result = commonPropertyService.save(commonPropertyModel);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commonPropertyModel.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /common-properties/:id} : Partial updates given fields of an existing commonProperty, field will ignore if it is null
     *
     * @param id the id of the commonPropertyModel to save.
     * @param commonPropertyModel the commonPropertyModel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commonPropertyModel,
     * or with status {@code 400 (Bad Request)} if the commonPropertyModel is not valid,
     * or with status {@code 404 (Not Found)} if the commonPropertyModel is not found,
     * or with status {@code 500 (Internal Server Error)} if the commonPropertyModel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/common-properties/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CommonPropertyModel> partialUpdateCommonProperty(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CommonPropertyModel commonPropertyModel
    ) throws URISyntaxException {
        log.debug("REST request to partial update CommonProperty partially : {}, {}", id, commonPropertyModel);
        if (commonPropertyModel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commonPropertyModel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commonPropertyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CommonPropertyModel> result = commonPropertyService.partialUpdate(commonPropertyModel);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commonPropertyModel.getId().toString())
        );
    }

    /**
     * {@code GET  /common-properties} : get all the commonProperties.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of commonProperties in body.
     */
    @GetMapping("/common-properties")
    public List<CommonPropertyModel> getAllCommonProperties() {
        log.debug("REST request to get all CommonProperties");
        return commonPropertyService.findAll();
    }

    /**
     * {@code GET  /common-properties/:id} : get the "id" commonProperty.
     *
     * @param id the id of the commonPropertyModel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the commonPropertyModel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/common-properties/{id}")
    public ResponseEntity<CommonPropertyModel> getCommonProperty(@PathVariable Long id) {
        log.debug("REST request to get CommonProperty : {}", id);
        Optional<CommonPropertyModel> commonPropertyModel = commonPropertyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(commonPropertyModel);
    }

    /**
     * {@code DELETE  /common-properties/:id} : delete the "id" commonProperty.
     *
     * @param id the id of the commonPropertyModel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/common-properties/{id}")
    public ResponseEntity<Void> deleteCommonProperty(@PathVariable Long id) {
        log.debug("REST request to delete CommonProperty : {}", id);
        commonPropertyService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

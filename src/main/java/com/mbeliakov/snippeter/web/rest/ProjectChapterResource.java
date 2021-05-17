package com.mbeliakov.snippeter.web.rest;

import com.mbeliakov.snippeter.repository.ProjectChapterRepository;
import com.mbeliakov.snippeter.service.ProjectChapterService;
import com.mbeliakov.snippeter.service.dto.ProjectChapterModel;
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
 * REST controller for managing {@link com.mbeliakov.snippeter.domain.ProjectChapter}.
 */
@RestController
@RequestMapping("/api")
public class ProjectChapterResource {

    private final Logger log = LoggerFactory.getLogger(ProjectChapterResource.class);

    private static final String ENTITY_NAME = "projectChapter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProjectChapterService projectChapterService;

    private final ProjectChapterRepository projectChapterRepository;

    public ProjectChapterResource(ProjectChapterService projectChapterService, ProjectChapterRepository projectChapterRepository) {
        this.projectChapterService = projectChapterService;
        this.projectChapterRepository = projectChapterRepository;
    }

    /**
     * {@code POST  /project-chapters} : Create a new projectChapter.
     *
     * @param projectChapterModel the projectChapterModel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new projectChapterModel, or with status {@code 400 (Bad Request)} if the projectChapter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/project-chapters")
    public ResponseEntity<ProjectChapterModel> createProjectChapter(@Valid @RequestBody ProjectChapterModel projectChapterModel)
        throws URISyntaxException {
        log.debug("REST request to save ProjectChapter : {}", projectChapterModel);
        if (projectChapterModel.getId() != null) {
            throw new BadRequestAlertException("A new projectChapter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProjectChapterModel result = projectChapterService.save(projectChapterModel);
        return ResponseEntity
            .created(new URI("/api/project-chapters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /project-chapters/:id} : Updates an existing projectChapter.
     *
     * @param id the id of the projectChapterModel to save.
     * @param projectChapterModel the projectChapterModel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated projectChapterModel,
     * or with status {@code 400 (Bad Request)} if the projectChapterModel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the projectChapterModel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/project-chapters/{id}")
    public ResponseEntity<ProjectChapterModel> updateProjectChapter(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProjectChapterModel projectChapterModel
    ) throws URISyntaxException {
        log.debug("REST request to update ProjectChapter : {}, {}", id, projectChapterModel);
        if (projectChapterModel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, projectChapterModel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!projectChapterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProjectChapterModel result = projectChapterService.save(projectChapterModel);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, projectChapterModel.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /project-chapters/:id} : Partial updates given fields of an existing projectChapter, field will ignore if it is null
     *
     * @param id the id of the projectChapterModel to save.
     * @param projectChapterModel the projectChapterModel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated projectChapterModel,
     * or with status {@code 400 (Bad Request)} if the projectChapterModel is not valid,
     * or with status {@code 404 (Not Found)} if the projectChapterModel is not found,
     * or with status {@code 500 (Internal Server Error)} if the projectChapterModel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/project-chapters/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ProjectChapterModel> partialUpdateProjectChapter(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProjectChapterModel projectChapterModel
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProjectChapter partially : {}, {}", id, projectChapterModel);
        if (projectChapterModel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, projectChapterModel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!projectChapterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProjectChapterModel> result = projectChapterService.partialUpdate(projectChapterModel);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, projectChapterModel.getId().toString())
        );
    }

    /**
     * {@code GET  /project-chapters} : get all the projectChapters.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of projectChapters in body.
     */
    @GetMapping("/project-chapters")
    public List<ProjectChapterModel> getAllProjectChapters() {
        log.debug("REST request to get all ProjectChapters");
        return projectChapterService.findAll();
    }

    /**
     * {@code GET  /project-chapters/:id} : get the "id" projectChapter.
     *
     * @param id the id of the projectChapterModel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the projectChapterModel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/project-chapters/{id}")
    public ResponseEntity<ProjectChapterModel> getProjectChapter(@PathVariable Long id) {
        log.debug("REST request to get ProjectChapter : {}", id);
        Optional<ProjectChapterModel> projectChapterModel = projectChapterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(projectChapterModel);
    }

    /**
     * {@code DELETE  /project-chapters/:id} : delete the "id" projectChapter.
     *
     * @param id the id of the projectChapterModel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/project-chapters/{id}")
    public ResponseEntity<Void> deleteProjectChapter(@PathVariable Long id) {
        log.debug("REST request to delete ProjectChapter : {}", id);
        projectChapterService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

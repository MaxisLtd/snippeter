package com.mbeliakov.snippeter.service;

import com.mbeliakov.snippeter.service.dto.ProjectModel;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mbeliakov.snippeter.domain.Project}.
 */
public interface ProjectService {
    /**
     * Save a project.
     *
     * @param projectModel the entity to save.
     * @return the persisted entity.
     */
    ProjectModel save(ProjectModel projectModel);

    /**
     * Partially updates a project.
     *
     * @param projectModel the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProjectModel> partialUpdate(ProjectModel projectModel);

    /**
     * Get all the projects.
     *
     * @return the list of entities.
     */
    List<ProjectModel> findAll();

    /**
     * Get the "id" project.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProjectModel> findOne(Long id);

    /**
     * Delete the "id" project.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

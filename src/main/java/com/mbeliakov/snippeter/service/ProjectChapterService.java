package com.mbeliakov.snippeter.service;

import com.mbeliakov.snippeter.service.dto.ProjectChapterModel;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mbeliakov.snippeter.domain.ProjectChapter}.
 */
public interface ProjectChapterService {
    /**
     * Save a projectChapter.
     *
     * @param projectChapterModel the entity to save.
     * @return the persisted entity.
     */
    ProjectChapterModel save(ProjectChapterModel projectChapterModel);

    /**
     * Partially updates a projectChapter.
     *
     * @param projectChapterModel the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProjectChapterModel> partialUpdate(ProjectChapterModel projectChapterModel);

    /**
     * Get all the projectChapters.
     *
     * @return the list of entities.
     */
    List<ProjectChapterModel> findAll();

    /**
     * Get the "id" projectChapter.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProjectChapterModel> findOne(Long id);

    /**
     * Delete the "id" projectChapter.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

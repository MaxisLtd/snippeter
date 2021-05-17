package com.mbeliakov.snippeter.service;

import com.mbeliakov.snippeter.service.dto.SnippetSectionModel;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mbeliakov.snippeter.domain.SnippetSection}.
 */
public interface SnippetSectionService {
    /**
     * Save a snippetSection.
     *
     * @param snippetSectionModel the entity to save.
     * @return the persisted entity.
     */
    SnippetSectionModel save(SnippetSectionModel snippetSectionModel);

    /**
     * Partially updates a snippetSection.
     *
     * @param snippetSectionModel the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SnippetSectionModel> partialUpdate(SnippetSectionModel snippetSectionModel);

    /**
     * Get all the snippetSections.
     *
     * @return the list of entities.
     */
    List<SnippetSectionModel> findAll();

    /**
     * Get the "id" snippetSection.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SnippetSectionModel> findOne(Long id);

    /**
     * Delete the "id" snippetSection.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

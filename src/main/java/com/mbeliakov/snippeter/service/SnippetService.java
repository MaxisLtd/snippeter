package com.mbeliakov.snippeter.service;

import com.mbeliakov.snippeter.service.dto.SnippetModel;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mbeliakov.snippeter.domain.Snippet}.
 */
public interface SnippetService {
    /**
     * Save a snippet.
     *
     * @param snippetModel the entity to save.
     * @return the persisted entity.
     */
    SnippetModel save(SnippetModel snippetModel);

    /**
     * Partially updates a snippet.
     *
     * @param snippetModel the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SnippetModel> partialUpdate(SnippetModel snippetModel);

    /**
     * Get all the snippets.
     *
     * @return the list of entities.
     */
    List<SnippetModel> findAll();

    /**
     * Get the "id" snippet.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SnippetModel> findOne(Long id);

    /**
     * Delete the "id" snippet.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

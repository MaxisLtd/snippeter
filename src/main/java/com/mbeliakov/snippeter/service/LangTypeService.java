package com.mbeliakov.snippeter.service;

import com.mbeliakov.snippeter.service.dto.LangTypeModel;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mbeliakov.snippeter.domain.LangType}.
 */
public interface LangTypeService {
    /**
     * Save a langType.
     *
     * @param langTypeModel the entity to save.
     * @return the persisted entity.
     */
    LangTypeModel save(LangTypeModel langTypeModel);

    /**
     * Partially updates a langType.
     *
     * @param langTypeModel the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LangTypeModel> partialUpdate(LangTypeModel langTypeModel);

    /**
     * Get all the langTypes.
     *
     * @return the list of entities.
     */
    List<LangTypeModel> findAll();

    /**
     * Get the "id" langType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LangTypeModel> findOne(Long id);

    /**
     * Delete the "id" langType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

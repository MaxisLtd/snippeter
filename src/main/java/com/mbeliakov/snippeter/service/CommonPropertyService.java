package com.mbeliakov.snippeter.service;

import com.mbeliakov.snippeter.service.dto.CommonPropertyModel;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mbeliakov.snippeter.domain.CommonProperty}.
 */
public interface CommonPropertyService {
    /**
     * Save a commonProperty.
     *
     * @param commonPropertyModel the entity to save.
     * @return the persisted entity.
     */
    CommonPropertyModel save(CommonPropertyModel commonPropertyModel);

    /**
     * Partially updates a commonProperty.
     *
     * @param commonPropertyModel the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CommonPropertyModel> partialUpdate(CommonPropertyModel commonPropertyModel);

    /**
     * Get all the commonProperties.
     *
     * @return the list of entities.
     */
    List<CommonPropertyModel> findAll();

    /**
     * Get the "id" commonProperty.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CommonPropertyModel> findOne(Long id);

    /**
     * Delete the "id" commonProperty.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

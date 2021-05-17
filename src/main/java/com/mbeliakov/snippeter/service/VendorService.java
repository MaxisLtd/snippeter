package com.mbeliakov.snippeter.service;

import com.mbeliakov.snippeter.service.dto.VendorModel;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mbeliakov.snippeter.domain.Vendor}.
 */
public interface VendorService {
    /**
     * Save a vendor.
     *
     * @param vendorModel the entity to save.
     * @return the persisted entity.
     */
    VendorModel save(VendorModel vendorModel);

    /**
     * Partially updates a vendor.
     *
     * @param vendorModel the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VendorModel> partialUpdate(VendorModel vendorModel);

    /**
     * Get all the vendors.
     *
     * @return the list of entities.
     */
    List<VendorModel> findAll();

    /**
     * Get the "id" vendor.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VendorModel> findOne(Long id);

    /**
     * Delete the "id" vendor.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

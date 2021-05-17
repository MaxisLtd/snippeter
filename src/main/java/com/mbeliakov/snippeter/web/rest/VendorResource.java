package com.mbeliakov.snippeter.web.rest;

import com.mbeliakov.snippeter.repository.VendorRepository;
import com.mbeliakov.snippeter.service.VendorService;
import com.mbeliakov.snippeter.service.dto.VendorModel;
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
 * REST controller for managing {@link com.mbeliakov.snippeter.domain.Vendor}.
 */
@RestController
@RequestMapping("/api")
public class VendorResource {

    private final Logger log = LoggerFactory.getLogger(VendorResource.class);

    private static final String ENTITY_NAME = "vendor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VendorService vendorService;

    private final VendorRepository vendorRepository;

    public VendorResource(VendorService vendorService, VendorRepository vendorRepository) {
        this.vendorService = vendorService;
        this.vendorRepository = vendorRepository;
    }

    /**
     * {@code POST  /vendors} : Create a new vendor.
     *
     * @param vendorModel the vendorModel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vendorModel, or with status {@code 400 (Bad Request)} if the vendor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vendors")
    public ResponseEntity<VendorModel> createVendor(@Valid @RequestBody VendorModel vendorModel) throws URISyntaxException {
        log.debug("REST request to save Vendor : {}", vendorModel);
        if (vendorModel.getId() != null) {
            throw new BadRequestAlertException("A new vendor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VendorModel result = vendorService.save(vendorModel);
        return ResponseEntity
            .created(new URI("/api/vendors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vendors/:id} : Updates an existing vendor.
     *
     * @param id the id of the vendorModel to save.
     * @param vendorModel the vendorModel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vendorModel,
     * or with status {@code 400 (Bad Request)} if the vendorModel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vendorModel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vendors/{id}")
    public ResponseEntity<VendorModel> updateVendor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VendorModel vendorModel
    ) throws URISyntaxException {
        log.debug("REST request to update Vendor : {}, {}", id, vendorModel);
        if (vendorModel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vendorModel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vendorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VendorModel result = vendorService.save(vendorModel);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vendorModel.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vendors/:id} : Partial updates given fields of an existing vendor, field will ignore if it is null
     *
     * @param id the id of the vendorModel to save.
     * @param vendorModel the vendorModel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vendorModel,
     * or with status {@code 400 (Bad Request)} if the vendorModel is not valid,
     * or with status {@code 404 (Not Found)} if the vendorModel is not found,
     * or with status {@code 500 (Internal Server Error)} if the vendorModel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vendors/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<VendorModel> partialUpdateVendor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VendorModel vendorModel
    ) throws URISyntaxException {
        log.debug("REST request to partial update Vendor partially : {}, {}", id, vendorModel);
        if (vendorModel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vendorModel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vendorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VendorModel> result = vendorService.partialUpdate(vendorModel);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vendorModel.getId().toString())
        );
    }

    /**
     * {@code GET  /vendors} : get all the vendors.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vendors in body.
     */
    @GetMapping("/vendors")
    public List<VendorModel> getAllVendors() {
        log.debug("REST request to get all Vendors");
        return vendorService.findAll();
    }

    /**
     * {@code GET  /vendors/:id} : get the "id" vendor.
     *
     * @param id the id of the vendorModel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vendorModel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vendors/{id}")
    public ResponseEntity<VendorModel> getVendor(@PathVariable Long id) {
        log.debug("REST request to get Vendor : {}", id);
        Optional<VendorModel> vendorModel = vendorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vendorModel);
    }

    /**
     * {@code DELETE  /vendors/:id} : delete the "id" vendor.
     *
     * @param id the id of the vendorModel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vendors/{id}")
    public ResponseEntity<Void> deleteVendor(@PathVariable Long id) {
        log.debug("REST request to delete Vendor : {}", id);
        vendorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

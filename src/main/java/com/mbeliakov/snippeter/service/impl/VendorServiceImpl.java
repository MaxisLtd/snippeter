package com.mbeliakov.snippeter.service.impl;

import com.mbeliakov.snippeter.domain.Vendor;
import com.mbeliakov.snippeter.repository.VendorRepository;
import com.mbeliakov.snippeter.service.VendorService;
import com.mbeliakov.snippeter.service.dto.VendorModel;
import com.mbeliakov.snippeter.service.mapper.VendorMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Vendor}.
 */
@Service
@Transactional
public class VendorServiceImpl implements VendorService {

    private final Logger log = LoggerFactory.getLogger(VendorServiceImpl.class);

    private final VendorRepository vendorRepository;

    private final VendorMapper vendorMapper;

    public VendorServiceImpl(VendorRepository vendorRepository, VendorMapper vendorMapper) {
        this.vendorRepository = vendorRepository;
        this.vendorMapper = vendorMapper;
    }

    @Override
    public VendorModel save(VendorModel vendorModel) {
        log.debug("Request to save Vendor : {}", vendorModel);
        Vendor vendor = vendorMapper.toEntity(vendorModel);
        vendor = vendorRepository.save(vendor);
        return vendorMapper.toDto(vendor);
    }

    @Override
    public Optional<VendorModel> partialUpdate(VendorModel vendorModel) {
        log.debug("Request to partially update Vendor : {}", vendorModel);

        return vendorRepository
            .findById(vendorModel.getId())
            .map(
                existingVendor -> {
                    vendorMapper.partialUpdate(existingVendor, vendorModel);
                    return existingVendor;
                }
            )
            .map(vendorRepository::save)
            .map(vendorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VendorModel> findAll() {
        log.debug("Request to get all Vendors");
        return vendorRepository.findAll().stream().map(vendorMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VendorModel> findOne(Long id) {
        log.debug("Request to get Vendor : {}", id);
        return vendorRepository.findById(id).map(vendorMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Vendor : {}", id);
        vendorRepository.deleteById(id);
    }
}

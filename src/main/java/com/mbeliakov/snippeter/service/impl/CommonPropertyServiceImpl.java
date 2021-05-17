package com.mbeliakov.snippeter.service.impl;

import com.mbeliakov.snippeter.domain.CommonProperty;
import com.mbeliakov.snippeter.repository.CommonPropertyRepository;
import com.mbeliakov.snippeter.service.CommonPropertyService;
import com.mbeliakov.snippeter.service.dto.CommonPropertyModel;
import com.mbeliakov.snippeter.service.mapper.CommonPropertyMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CommonProperty}.
 */
@Service
@Transactional
public class CommonPropertyServiceImpl implements CommonPropertyService {

    private final Logger log = LoggerFactory.getLogger(CommonPropertyServiceImpl.class);

    private final CommonPropertyRepository commonPropertyRepository;

    private final CommonPropertyMapper commonPropertyMapper;

    public CommonPropertyServiceImpl(CommonPropertyRepository commonPropertyRepository, CommonPropertyMapper commonPropertyMapper) {
        this.commonPropertyRepository = commonPropertyRepository;
        this.commonPropertyMapper = commonPropertyMapper;
    }

    @Override
    public CommonPropertyModel save(CommonPropertyModel commonPropertyModel) {
        log.debug("Request to save CommonProperty : {}", commonPropertyModel);
        CommonProperty commonProperty = commonPropertyMapper.toEntity(commonPropertyModel);
        commonProperty = commonPropertyRepository.save(commonProperty);
        return commonPropertyMapper.toDto(commonProperty);
    }

    @Override
    public Optional<CommonPropertyModel> partialUpdate(CommonPropertyModel commonPropertyModel) {
        log.debug("Request to partially update CommonProperty : {}", commonPropertyModel);

        return commonPropertyRepository
            .findById(commonPropertyModel.getId())
            .map(
                existingCommonProperty -> {
                    commonPropertyMapper.partialUpdate(existingCommonProperty, commonPropertyModel);
                    return existingCommonProperty;
                }
            )
            .map(commonPropertyRepository::save)
            .map(commonPropertyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommonPropertyModel> findAll() {
        log.debug("Request to get all CommonProperties");
        return commonPropertyRepository
            .findAll()
            .stream()
            .map(commonPropertyMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CommonPropertyModel> findOne(Long id) {
        log.debug("Request to get CommonProperty : {}", id);
        return commonPropertyRepository.findById(id).map(commonPropertyMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CommonProperty : {}", id);
        commonPropertyRepository.deleteById(id);
    }
}

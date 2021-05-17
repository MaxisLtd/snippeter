package com.mbeliakov.snippeter.service.impl;

import com.mbeliakov.snippeter.domain.LangType;
import com.mbeliakov.snippeter.repository.LangTypeRepository;
import com.mbeliakov.snippeter.service.LangTypeService;
import com.mbeliakov.snippeter.service.dto.LangTypeModel;
import com.mbeliakov.snippeter.service.mapper.LangTypeMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LangType}.
 */
@Service
@Transactional
public class LangTypeServiceImpl implements LangTypeService {

    private final Logger log = LoggerFactory.getLogger(LangTypeServiceImpl.class);

    private final LangTypeRepository langTypeRepository;

    private final LangTypeMapper langTypeMapper;

    public LangTypeServiceImpl(LangTypeRepository langTypeRepository, LangTypeMapper langTypeMapper) {
        this.langTypeRepository = langTypeRepository;
        this.langTypeMapper = langTypeMapper;
    }

    @Override
    public LangTypeModel save(LangTypeModel langTypeModel) {
        log.debug("Request to save LangType : {}", langTypeModel);
        LangType langType = langTypeMapper.toEntity(langTypeModel);
        langType = langTypeRepository.save(langType);
        return langTypeMapper.toDto(langType);
    }

    @Override
    public Optional<LangTypeModel> partialUpdate(LangTypeModel langTypeModel) {
        log.debug("Request to partially update LangType : {}", langTypeModel);

        return langTypeRepository
            .findById(langTypeModel.getId())
            .map(
                existingLangType -> {
                    langTypeMapper.partialUpdate(existingLangType, langTypeModel);
                    return existingLangType;
                }
            )
            .map(langTypeRepository::save)
            .map(langTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LangTypeModel> findAll() {
        log.debug("Request to get all LangTypes");
        return langTypeRepository.findAll().stream().map(langTypeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LangTypeModel> findOne(Long id) {
        log.debug("Request to get LangType : {}", id);
        return langTypeRepository.findById(id).map(langTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LangType : {}", id);
        langTypeRepository.deleteById(id);
    }
}

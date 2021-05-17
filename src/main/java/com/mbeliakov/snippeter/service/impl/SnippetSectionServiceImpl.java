package com.mbeliakov.snippeter.service.impl;

import com.mbeliakov.snippeter.domain.SnippetSection;
import com.mbeliakov.snippeter.repository.SnippetSectionRepository;
import com.mbeliakov.snippeter.service.SnippetSectionService;
import com.mbeliakov.snippeter.service.dto.SnippetSectionModel;
import com.mbeliakov.snippeter.service.mapper.SnippetSectionMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SnippetSection}.
 */
@Service
@Transactional
public class SnippetSectionServiceImpl implements SnippetSectionService {

    private final Logger log = LoggerFactory.getLogger(SnippetSectionServiceImpl.class);

    private final SnippetSectionRepository snippetSectionRepository;

    private final SnippetSectionMapper snippetSectionMapper;

    public SnippetSectionServiceImpl(SnippetSectionRepository snippetSectionRepository, SnippetSectionMapper snippetSectionMapper) {
        this.snippetSectionRepository = snippetSectionRepository;
        this.snippetSectionMapper = snippetSectionMapper;
    }

    @Override
    public SnippetSectionModel save(SnippetSectionModel snippetSectionModel) {
        log.debug("Request to save SnippetSection : {}", snippetSectionModel);
        SnippetSection snippetSection = snippetSectionMapper.toEntity(snippetSectionModel);
        snippetSection = snippetSectionRepository.save(snippetSection);
        return snippetSectionMapper.toDto(snippetSection);
    }

    @Override
    public Optional<SnippetSectionModel> partialUpdate(SnippetSectionModel snippetSectionModel) {
        log.debug("Request to partially update SnippetSection : {}", snippetSectionModel);

        return snippetSectionRepository
            .findById(snippetSectionModel.getId())
            .map(
                existingSnippetSection -> {
                    snippetSectionMapper.partialUpdate(existingSnippetSection, snippetSectionModel);
                    return existingSnippetSection;
                }
            )
            .map(snippetSectionRepository::save)
            .map(snippetSectionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SnippetSectionModel> findAll() {
        log.debug("Request to get all SnippetSections");
        return snippetSectionRepository
            .findAll()
            .stream()
            .map(snippetSectionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SnippetSectionModel> findOne(Long id) {
        log.debug("Request to get SnippetSection : {}", id);
        return snippetSectionRepository.findById(id).map(snippetSectionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SnippetSection : {}", id);
        snippetSectionRepository.deleteById(id);
    }
}

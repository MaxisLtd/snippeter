package com.mbeliakov.snippeter.service.impl;

import com.mbeliakov.snippeter.domain.Snippet;
import com.mbeliakov.snippeter.repository.SnippetRepository;
import com.mbeliakov.snippeter.service.SnippetService;
import com.mbeliakov.snippeter.service.dto.SnippetModel;
import com.mbeliakov.snippeter.service.mapper.SnippetMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Snippet}.
 */
@Service
@Transactional
public class SnippetServiceImpl implements SnippetService {

    private final Logger log = LoggerFactory.getLogger(SnippetServiceImpl.class);

    private final SnippetRepository snippetRepository;

    private final SnippetMapper snippetMapper;

    public SnippetServiceImpl(SnippetRepository snippetRepository, SnippetMapper snippetMapper) {
        this.snippetRepository = snippetRepository;
        this.snippetMapper = snippetMapper;
    }

    @Override
    public SnippetModel save(SnippetModel snippetModel) {
        log.debug("Request to save Snippet : {}", snippetModel);
        Snippet snippet = snippetMapper.toEntity(snippetModel);
        snippet = snippetRepository.save(snippet);
        return snippetMapper.toDto(snippet);
    }

    @Override
    public Optional<SnippetModel> partialUpdate(SnippetModel snippetModel) {
        log.debug("Request to partially update Snippet : {}", snippetModel);

        return snippetRepository
            .findById(snippetModel.getId())
            .map(
                existingSnippet -> {
                    snippetMapper.partialUpdate(existingSnippet, snippetModel);
                    return existingSnippet;
                }
            )
            .map(snippetRepository::save)
            .map(snippetMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SnippetModel> findAll() {
        log.debug("Request to get all Snippets");
        return snippetRepository.findAll().stream().map(snippetMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SnippetModel> findOne(Long id) {
        log.debug("Request to get Snippet : {}", id);
        return snippetRepository.findById(id).map(snippetMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Snippet : {}", id);
        snippetRepository.deleteById(id);
    }
}

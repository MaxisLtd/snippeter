package com.mbeliakov.snippeter.service.impl;

import com.mbeliakov.snippeter.domain.ProjectChapter;
import com.mbeliakov.snippeter.repository.ProjectChapterRepository;
import com.mbeliakov.snippeter.service.ProjectChapterService;
import com.mbeliakov.snippeter.service.dto.ProjectChapterModel;
import com.mbeliakov.snippeter.service.mapper.ProjectChapterMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProjectChapter}.
 */
@Service
@Transactional
public class ProjectChapterServiceImpl implements ProjectChapterService {

    private final Logger log = LoggerFactory.getLogger(ProjectChapterServiceImpl.class);

    private final ProjectChapterRepository projectChapterRepository;

    private final ProjectChapterMapper projectChapterMapper;

    public ProjectChapterServiceImpl(ProjectChapterRepository projectChapterRepository, ProjectChapterMapper projectChapterMapper) {
        this.projectChapterRepository = projectChapterRepository;
        this.projectChapterMapper = projectChapterMapper;
    }

    @Override
    public ProjectChapterModel save(ProjectChapterModel projectChapterModel) {
        log.debug("Request to save ProjectChapter : {}", projectChapterModel);
        ProjectChapter projectChapter = projectChapterMapper.toEntity(projectChapterModel);
        projectChapter = projectChapterRepository.save(projectChapter);
        return projectChapterMapper.toDto(projectChapter);
    }

    @Override
    public Optional<ProjectChapterModel> partialUpdate(ProjectChapterModel projectChapterModel) {
        log.debug("Request to partially update ProjectChapter : {}", projectChapterModel);

        return projectChapterRepository
            .findById(projectChapterModel.getId())
            .map(
                existingProjectChapter -> {
                    projectChapterMapper.partialUpdate(existingProjectChapter, projectChapterModel);
                    return existingProjectChapter;
                }
            )
            .map(projectChapterRepository::save)
            .map(projectChapterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectChapterModel> findAll() {
        log.debug("Request to get all ProjectChapters");
        return projectChapterRepository
            .findAll()
            .stream()
            .map(projectChapterMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProjectChapterModel> findOne(Long id) {
        log.debug("Request to get ProjectChapter : {}", id);
        return projectChapterRepository.findById(id).map(projectChapterMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProjectChapter : {}", id);
        projectChapterRepository.deleteById(id);
    }
}

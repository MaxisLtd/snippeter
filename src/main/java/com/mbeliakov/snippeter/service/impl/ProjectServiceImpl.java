package com.mbeliakov.snippeter.service.impl;

import com.mbeliakov.snippeter.domain.Project;
import com.mbeliakov.snippeter.repository.ProjectRepository;
import com.mbeliakov.snippeter.service.ProjectService;
import com.mbeliakov.snippeter.service.dto.ProjectModel;
import com.mbeliakov.snippeter.service.mapper.ProjectMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Project}.
 */
@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final Logger log = LoggerFactory.getLogger(ProjectServiceImpl.class);

    private final ProjectRepository projectRepository;

    private final ProjectMapper projectMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    @Override
    public ProjectModel save(ProjectModel projectModel) {
        log.debug("Request to save Project : {}", projectModel);
        Project project = projectMapper.toEntity(projectModel);
        project = projectRepository.save(project);
        return projectMapper.toDto(project);
    }

    @Override
    public Optional<ProjectModel> partialUpdate(ProjectModel projectModel) {
        log.debug("Request to partially update Project : {}", projectModel);

        return projectRepository
            .findById(projectModel.getId())
            .map(
                existingProject -> {
                    projectMapper.partialUpdate(existingProject, projectModel);
                    return existingProject;
                }
            )
            .map(projectRepository::save)
            .map(projectMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectModel> findAll() {
        log.debug("Request to get all Projects");
        return projectRepository.findAll().stream().map(projectMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProjectModel> findOne(Long id) {
        log.debug("Request to get Project : {}", id);
        return projectRepository.findById(id).map(projectMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Project : {}", id);
        projectRepository.deleteById(id);
    }
}

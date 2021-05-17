package com.mbeliakov.snippeter.service.mapper;

import com.mbeliakov.snippeter.domain.*;
import com.mbeliakov.snippeter.service.dto.ProjectChapterModel;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProjectChapter} and its DTO {@link ProjectChapterModel}.
 */
@Mapper(componentModel = "spring", uses = { ProjectMapper.class })
public interface ProjectChapterMapper extends EntityMapper<ProjectChapterModel, ProjectChapter> {
    @Mapping(target = "project", source = "project", qualifiedByName = "id")
    ProjectChapterModel toDto(ProjectChapter s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProjectChapterModel toDtoId(ProjectChapter projectChapter);
}

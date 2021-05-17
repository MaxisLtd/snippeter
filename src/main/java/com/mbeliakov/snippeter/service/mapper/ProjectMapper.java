package com.mbeliakov.snippeter.service.mapper;

import com.mbeliakov.snippeter.domain.*;
import com.mbeliakov.snippeter.service.dto.ProjectModel;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Project} and its DTO {@link ProjectModel}.
 */
@Mapper(componentModel = "spring", uses = { VendorMapper.class })
public interface ProjectMapper extends EntityMapper<ProjectModel, Project> {
    @Mapping(target = "vendor", source = "vendor", qualifiedByName = "id")
    ProjectModel toDto(Project s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProjectModel toDtoId(Project project);
}

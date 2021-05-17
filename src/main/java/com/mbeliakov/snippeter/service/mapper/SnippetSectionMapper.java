package com.mbeliakov.snippeter.service.mapper;

import com.mbeliakov.snippeter.domain.*;
import com.mbeliakov.snippeter.service.dto.SnippetSectionModel;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SnippetSection} and its DTO {@link SnippetSectionModel}.
 */
@Mapper(componentModel = "spring", uses = { SnippetMapper.class })
public interface SnippetSectionMapper extends EntityMapper<SnippetSectionModel, SnippetSection> {
    @Mapping(target = "snippet", source = "snippet", qualifiedByName = "id")
    SnippetSectionModel toDto(SnippetSection s);
}

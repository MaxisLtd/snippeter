package com.mbeliakov.snippeter.service.mapper;

import com.mbeliakov.snippeter.domain.*;
import com.mbeliakov.snippeter.service.dto.SnippetModel;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Snippet} and its DTO {@link SnippetModel}.
 */
@Mapper(componentModel = "spring", uses = { ProjectChapterMapper.class })
public interface SnippetMapper extends EntityMapper<SnippetModel, Snippet> {
    @Mapping(target = "projectChapter", source = "projectChapter", qualifiedByName = "id")
    SnippetModel toDto(Snippet s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SnippetModel toDtoId(Snippet snippet);
}

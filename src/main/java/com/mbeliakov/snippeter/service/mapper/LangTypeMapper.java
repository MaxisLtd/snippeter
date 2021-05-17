package com.mbeliakov.snippeter.service.mapper;

import com.mbeliakov.snippeter.domain.*;
import com.mbeliakov.snippeter.service.dto.LangTypeModel;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LangType} and its DTO {@link LangTypeModel}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LangTypeMapper extends EntityMapper<LangTypeModel, LangType> {}

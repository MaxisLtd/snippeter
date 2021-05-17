package com.mbeliakov.snippeter.service.mapper;

import com.mbeliakov.snippeter.domain.*;
import com.mbeliakov.snippeter.service.dto.CommonPropertyModel;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CommonProperty} and its DTO {@link CommonPropertyModel}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CommonPropertyMapper extends EntityMapper<CommonPropertyModel, CommonProperty> {}

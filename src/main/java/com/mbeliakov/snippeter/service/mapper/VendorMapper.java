package com.mbeliakov.snippeter.service.mapper;

import com.mbeliakov.snippeter.domain.*;
import com.mbeliakov.snippeter.service.dto.VendorModel;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Vendor} and its DTO {@link VendorModel}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface VendorMapper extends EntityMapper<VendorModel, Vendor> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VendorModel toDtoId(Vendor vendor);
}

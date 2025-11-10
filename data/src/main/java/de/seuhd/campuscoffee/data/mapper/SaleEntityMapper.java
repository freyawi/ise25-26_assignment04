package de.seuhd.campuscoffee.data.mapper;

import de.seuhd.campuscoffee.data.persistence.SaleEntity;
import de.seuhd.campuscoffee.domain.model.Sale;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.jspecify.annotations.NonNull;

@Mapper(componentModel = "spring", uses = {CartItemEntityMapper.class})
public interface SaleEntityMapper {
    @NonNull
    Sale entityToModel(@NonNull SaleEntity entity);
    
    @NonNull
    SaleEntity modelToEntity(@NonNull Sale model);
    
    void updateEntity(@NonNull Sale model, @MappingTarget @NonNull SaleEntity entity);
}
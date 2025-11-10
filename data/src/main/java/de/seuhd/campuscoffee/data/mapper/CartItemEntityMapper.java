package de.seuhd.campuscoffee.data.mapper;

import de.seuhd.campuscoffee.data.persistence.CartItemEntity;
import de.seuhd.campuscoffee.domain.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.jspecify.annotations.NonNull;

@Mapper(componentModel = "spring", uses = {ProductEntityMapper.class})
public interface CartItemEntityMapper {
    @NonNull
    @Mapping(target = "sale", ignore = true)
    CartItem entityToModel(@NonNull CartItemEntity entity);
    
    @NonNull
    @Mapping(target = "sale", ignore = true)
    CartItemEntity modelToEntity(@NonNull CartItem model);
    
    @Mapping(target = "sale", ignore = true)
    void updateEntity(@NonNull CartItem model, @MappingTarget @NonNull CartItemEntity entity);
}
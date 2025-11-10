package de.seuhd.campuscoffee.data.mapper;

import de.seuhd.campuscoffee.data.persistence.ProductEntity;
import de.seuhd.campuscoffee.domain.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.jspecify.annotations.NonNull;

@Mapper(componentModel = "spring")
public interface ProductEntityMapper {
    @NonNull
    Product entityToModel(@NonNull ProductEntity entity);
    
    @NonNull
    ProductEntity modelToEntity(@NonNull Product model);
    
    void updateEntity(@NonNull Product model, @MappingTarget @NonNull ProductEntity entity);
}
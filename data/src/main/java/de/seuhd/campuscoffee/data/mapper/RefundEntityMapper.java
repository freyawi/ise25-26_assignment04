package de.seuhd.campuscoffee.data.mapper;

import de.seuhd.campuscoffee.data.persistence.RefundEntity;
import de.seuhd.campuscoffee.domain.model.Refund;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.jspecify.annotations.NonNull;

@Mapper(componentModel = "spring")
public interface RefundEntityMapper {
    @NonNull
    Refund entityToModel(@NonNull RefundEntity entity);
    
    @NonNull
    RefundEntity modelToEntity(@NonNull Refund model);
    
    void updateEntity(@NonNull Refund model, @MappingTarget @NonNull RefundEntity entity);
}
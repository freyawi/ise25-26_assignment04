package de.seuhd.campuscoffee.data.mapper;

import de.seuhd.campuscoffee.data.persistence.CashierEntity;
import de.seuhd.campuscoffee.domain.model.CashierAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.jspecify.annotations.NonNull;

@Mapper(componentModel = "spring")
public interface CashierEntityMapper {
    @NonNull
    CashierAccount entityToModel(@NonNull CashierEntity entity);
    
    @NonNull
    @Mapping(target = "shiftStartTime", constant = "")
    @Mapping(target = "shiftEndTime", constant = "")
    CashierEntity modelToEntity(@NonNull CashierAccount model);
    
    @Mapping(target = "shiftStartTime", ignore = true)
    @Mapping(target = "shiftEndTime", ignore = true)
    void updateEntity(@NonNull CashierAccount model, @MappingTarget @NonNull CashierEntity entity);
}
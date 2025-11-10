package de.seuhd.campuscoffee.data.mapper;

import de.seuhd.campuscoffee.data.persistence.CartItemEntity;
import de.seuhd.campuscoffee.domain.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.jspecify.annotations.NonNull;
import java.math.BigDecimal;

@Mapper(componentModel = "spring", uses = {ProductEntityMapper.class})
public interface CartItemEntityMapper {
    @NonNull
    @Mapping(target = "subtotal", expression = "java(entity.getUnitPrice().multiply(java.math.BigDecimal.valueOf(entity.getQuantity())))")
    @Mapping(target = "total", expression = "java(calculateTotal(entity))")
    CartItem entityToModel(@NonNull CartItemEntity entity);
    
    @NonNull
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sale", ignore = true)
    @Mapping(source = "unitPrice", target = "unitPrice")
    CartItemEntity modelToEntity(@NonNull CartItem model);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sale", ignore = true)
    void updateEntity(@NonNull CartItem model, @MappingTarget @NonNull CartItemEntity entity);
    
    default BigDecimal calculateTotal(CartItemEntity entity) {
        BigDecimal subtotal = entity.getUnitPrice().multiply(BigDecimal.valueOf(entity.getQuantity()));
        if (entity.getDiscount() != null) {
            return subtotal.subtract(entity.getDiscount());
        }
        return subtotal;
    }
}
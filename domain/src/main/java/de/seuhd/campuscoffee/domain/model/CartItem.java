package de.seuhd.campuscoffee.domain.model;

import lombok.Builder;
import lombok.Data;
import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;

@Data
@Builder(toBuilder = true)
public class CartItem {
    @NonNull
    private Product product;
    @NonNull
    private Integer quantity;
    @NonNull
    private BigDecimal unitPrice;
    private BigDecimal discount;
    @NonNull
    private BigDecimal subtotal;
    @NonNull
    private BigDecimal total;
}
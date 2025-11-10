package de.seuhd.campuscoffee.domain.model;

import lombok.Builder;
import lombok.Data;
import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;

@Data
@Builder(toBuilder = true)
public class Product {
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    @NonNull
    private BigDecimal price;
    @NonNull
    private String barcode;
    private String category;
    private boolean available;
}
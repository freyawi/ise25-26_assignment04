package de.seuhd.campuscoffee.domain.model;

import lombok.Builder;
import lombok.Data;
import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class Refund {
    private Long id;
    @NonNull
    private Long originalSaleId;
    @NonNull
    private BigDecimal amount;
    @NonNull
    private PaymentMethod refundMethod;
    @NonNull
    private LocalDateTime timestamp;
    @NonNull
    private Long cashierId;
    @NonNull
    private String cashierName;
    private String reason;
    @NonNull
    private String receiptNumber;
}
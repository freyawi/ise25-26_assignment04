package de.seuhd.campuscoffee.domain.model;

import lombok.Builder;
import lombok.Data;
import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class Sale {
    private Long id;
    @NonNull
    private List<CartItem> items;
    @NonNull
    private BigDecimal subtotal;
    private BigDecimal discount;
    private BigDecimal tip;
    @NonNull
    private BigDecimal total;
    @NonNull
    private PaymentMethod paymentMethod;
    @NonNull
    private LocalDateTime timestamp;
    @NonNull
    private String receiptNumber;
    @NonNull
    private Long cashierId;
    @NonNull
    private String cashierName;
    private BigDecimal cashReceived;
    private BigDecimal changeGiven;
    private String notes;
    @NonNull
    private SaleStatus status;
}
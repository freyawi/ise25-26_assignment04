package de.seuhd.campuscoffee.domain.ports;

import de.seuhd.campuscoffee.domain.model.Refund;
import de.seuhd.campuscoffee.domain.model.PaymentMethod;
import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service interface for Refund operations.
 */
public interface RefundService {
    @NonNull
    Refund createRefund(@NonNull Long saleId, @NonNull BigDecimal amount, 
                       @NonNull PaymentMethod refundMethod, @NonNull Long cashierId, 
                       @NonNull String reason);
    
    @NonNull
    Refund getRefund(@NonNull Long id);
    
    @NonNull
    List<Refund> getRefundsByDateRange(@NonNull LocalDateTime start, @NonNull LocalDateTime end);
    
    @NonNull
    List<Refund> getRefundsByCashier(@NonNull Long cashierId);
    
    @NonNull
    List<Refund> getDailyRefunds(@NonNull LocalDateTime date);
    
    @NonNull
    BigDecimal getTotalRefundsForDay(@NonNull LocalDateTime date);
}
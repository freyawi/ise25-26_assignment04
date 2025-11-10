package de.seuhd.campuscoffee.domain.ports;

import de.seuhd.campuscoffee.domain.model.Refund;
import de.seuhd.campuscoffee.domain.model.Sale;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Data port interface for Refund persistence operations.
 * This port defines the contract for accessing refund data,
 * allowing the domain layer to remain independent of persistence details.
 */
public interface RefundDataPort {
    @NonNull
    Refund save(@NonNull Refund refund);
    
    @NonNull
    Optional<Refund> findById(@NonNull Long id);
    
    @NonNull
    List<Refund> findByTimestampBetween(@NonNull LocalDateTime start, @NonNull LocalDateTime end);
    
    @NonNull
    List<Refund> findByCashierId(@NonNull Long cashierId);
    
    @NonNull
    Optional<Sale> findSaleById(@NonNull Long saleId);
    
    void saveSale(@NonNull Sale sale);
    
    void deleteById(@NonNull Long id);
}

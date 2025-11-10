package de.seuhd.campuscoffee.domain.ports;

import de.seuhd.campuscoffee.domain.model.Sale;
import de.seuhd.campuscoffee.domain.model.SaleStatus;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Data port interface for Sale persistence operations.
 * This port defines the contract for accessing sale data,
 * allowing the domain layer to remain independent of persistence details.
 */
public interface SaleDataPort {
    @NonNull
    Sale save(@NonNull Sale sale);
    
    @NonNull
    Optional<Sale> findById(@NonNull Long id);
    
    @NonNull
    List<Sale> findByTimestampBetween(@NonNull LocalDateTime start, @NonNull LocalDateTime end);
    
    @NonNull
    List<Sale> findByCashierId(@NonNull Long cashierId);
    
    @NonNull
    List<Sale> findByStatus(@NonNull SaleStatus status);
    
    void deleteById(@NonNull Long id);
}

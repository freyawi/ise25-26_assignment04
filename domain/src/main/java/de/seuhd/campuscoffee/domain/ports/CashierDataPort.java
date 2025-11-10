package de.seuhd.campuscoffee.domain.ports;

import de.seuhd.campuscoffee.domain.model.CashierAccount;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;

/**
 * Data port interface for Cashier persistence operations.
 * This port defines the contract for accessing cashier data,
 * allowing the domain layer to remain independent of persistence details.
 */
public interface CashierDataPort {
    @NonNull
    CashierAccount save(@NonNull CashierAccount cashier);
    
    @NonNull
    Optional<CashierAccount> findById(@NonNull Long id);
    
    @NonNull
    Optional<CashierAccount> findByUsername(@NonNull String username);
    
    @NonNull
    List<CashierAccount> findByActiveTrue();
    
    void deleteById(@NonNull Long id);
}

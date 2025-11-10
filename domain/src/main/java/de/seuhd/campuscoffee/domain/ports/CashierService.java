package de.seuhd.campuscoffee.domain.ports;

import de.seuhd.campuscoffee.domain.model.CashierAccount;
import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for Cashier operations.
 */
public interface CashierService {
    @NonNull
    CashierAccount createCashier(@NonNull CashierAccount cashier);
    
    @NonNull
    Optional<CashierAccount> login(@NonNull String username, @NonNull String password);
    
    void logout(@NonNull Long cashierId);
    
    @NonNull
    CashierAccount updateCashier(@NonNull CashierAccount cashier);
    
    void deactivateCashier(@NonNull Long cashierId);
    
    @NonNull
    List<CashierAccount> getAllActiveCashiers();
    
    void addTip(@NonNull Long cashierId, @NonNull BigDecimal amount);
    
    @NonNull
    BigDecimal getTipBalance(@NonNull Long cashierId);
    
    void withdrawTips(@NonNull Long cashierId, @NonNull BigDecimal amount);
    
    void startShift(@NonNull Long cashierId);
    
    void endShift(@NonNull Long cashierId);
    
    @NonNull
    Optional<CashierAccount> getCurrentlyLoggedInCashier(@NonNull Long posId);
}
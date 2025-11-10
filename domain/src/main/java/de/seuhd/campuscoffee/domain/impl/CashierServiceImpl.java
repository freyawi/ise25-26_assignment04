package de.seuhd.campuscoffee.domain.impl;

import de.seuhd.campuscoffee.domain.model.CashierAccount;
import de.seuhd.campuscoffee.domain.ports.CashierService;
import de.seuhd.campuscoffee.domain.ports.CashierDataPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CashierServiceImpl implements CashierService {
    private final CashierDataPort cashierDataPort;
    private final PasswordEncoder passwordEncoder;
    @Override
    @Transactional
    @NonNull
    public CashierAccount createCashier(@NonNull CashierAccount cashier) {
        log.debug("Creating new cashier: {}", cashier.getUsername());
        cashier.setPassword(passwordEncoder.encode(cashier.getPassword()));
        cashier.setActive(true);
        cashier.setTipBalance(BigDecimal.ZERO);
        return cashierDataPort.save(cashier);
    }

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public Optional<CashierAccount> login(@NonNull String username, @NonNull String password) {
        log.debug("Attempting login for user: {}", username);
        return cashierDataPort.findByUsername(username)
                .filter(cashier -> passwordEncoder.matches(password, cashier.getPassword()))
                .map(cashier -> {
                    cashier.setLastLogin(LocalDateTime.now());
                    return cashierDataPort.save(cashier);
                });
    }

    @Override
    @Transactional
    public void logout(@NonNull Long cashierId) {
        log.debug("Logging out cashier ID: {}", cashierId);
        cashierDataPort.findById(cashierId)
                .ifPresent(cashier -> {
                    endShift(cashierId);
                });
    }

    @Override
    @Transactional
    @NonNull
    public CashierAccount updateCashier(@NonNull CashierAccount cashier) {
        log.debug("Updating cashier: {}", cashier.getUsername());
        CashierAccount existing = cashierDataPort.findById(cashier.getId())
                .orElseThrow(() -> new IllegalArgumentException("Cashier not found with ID: " + cashier.getId()));
        
        if (cashier.getPassword() != null && !cashier.getPassword().isEmpty()) {
            cashier.setPassword(passwordEncoder.encode(cashier.getPassword()));
        } else {
            cashier.setPassword(existing.getPassword());
        }
        
        return cashierDataPort.save(cashier);
    }

    @Override
    @Transactional
    public void deactivateCashier(@NonNull Long cashierId) {
        log.debug("Deactivating cashier ID: {}", cashierId);
        cashierDataPort.findById(cashierId)
                .ifPresent(cashier -> {
                    cashier.setActive(false);
                    cashierDataPort.save(cashier);
                });
    }

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public List<CashierAccount> getAllActiveCashiers() {
        log.debug("Retrieving all active cashiers");
        return cashierDataPort.findByActiveTrue();
    }

    @Override
    @Transactional
    public void addTip(@NonNull Long cashierId, @NonNull BigDecimal amount) {
        log.debug("Adding tip amount {} to cashier ID: {}", amount, cashierId);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Tip amount must be positive");
        }
        
        CashierAccount cashier = cashierDataPort.findById(cashierId)
                .orElseThrow(() -> new IllegalArgumentException("Cashier not found with ID: " + cashierId));
        
        BigDecimal newBalance = cashier.getTipBalance().add(amount);
        cashier.setTipBalance(newBalance);
        cashierDataPort.save(cashier);
    }

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public BigDecimal getTipBalance(@NonNull Long cashierId) {
        return cashierDataPort.findById(cashierId)
                .map(CashierAccount::getTipBalance)
                .orElseThrow(() -> new IllegalArgumentException("Cashier not found with ID: " + cashierId));
    }

    @Override
    @Transactional
    public void withdrawTips(@NonNull Long cashierId, @NonNull BigDecimal amount) {
        log.debug("Withdrawing tip amount {} for cashier ID: {}", amount, cashierId);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        
        CashierAccount cashier = cashierDataPort.findById(cashierId)
                .orElseThrow(() -> new IllegalArgumentException("Cashier not found with ID: " + cashierId));
        
        if (cashier.getTipBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient tip balance");
        }
        
        BigDecimal newBalance = cashier.getTipBalance().subtract(amount);
        cashier.setTipBalance(newBalance);
        cashierDataPort.save(cashier);
    }

    @Override
    @Transactional
    public void startShift(@NonNull Long cashierId) {
        log.debug("Starting shift for cashier ID: {}", cashierId);
        CashierAccount cashier = cashierDataPort.findById(cashierId)
                .orElseThrow(() -> new IllegalArgumentException("Cashier not found with ID: " + cashierId));
        
        cashier.setActive(true);
        cashierDataPort.save(cashier);
    }

    @Override
    @Transactional
    public void endShift(@NonNull Long cashierId) {
        log.debug("Ending shift for cashier ID: {}", cashierId);
        CashierAccount cashier = cashierDataPort.findById(cashierId)
                .orElseThrow(() -> new IllegalArgumentException("Cashier not found with ID: " + cashierId));
        
        cashier.setActive(false);
        cashierDataPort.save(cashier);
    }

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public Optional<CashierAccount> getCurrentlyLoggedInCashier(@NonNull Long posId) {
        log.debug("Checking for currently logged in cashier at POS ID: {}", posId);
        return cashierDataPort.findByActiveTrue().stream()
                .filter(CashierAccount::isActive)
                .findFirst();
    }
}
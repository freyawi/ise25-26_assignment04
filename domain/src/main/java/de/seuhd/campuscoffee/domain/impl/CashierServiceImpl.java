package de.seuhd.campuscoffee.domain.impl;

import de.seuhd.campuscoffee.domain.model.CashierAccount;
import de.seuhd.campuscoffee.domain.ports.CashierService;
import de.seuhd.campuscoffee.data.persistence.CashierEntity;
import de.seuhd.campuscoffee.data.persistence.CashierRepository;
import de.seuhd.campuscoffee.data.mapper.CashierEntityMapper;
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
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CashierServiceImpl implements CashierService {
    private final CashierRepository cashierRepository;
    private final CashierEntityMapper cashierMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    @NonNull
    public CashierAccount createCashier(@NonNull CashierAccount cashier) {
        log.debug("Creating new cashier: {}", cashier.getUsername());
        CashierEntity entity = cashierMapper.modelToEntity(cashier);
        entity.setPassword(passwordEncoder.encode(cashier.getPassword()));
        entity.setActive(true);
        entity.setTipBalance(BigDecimal.ZERO);
        entity = cashierRepository.save(entity);
        return cashierMapper.entityToModel(entity);
    }

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public Optional<CashierAccount> login(@NonNull String username, @NonNull String password) {
        log.debug("Attempting login for user: {}", username);
        return cashierRepository.findByUsername(username)
                .filter(cashier -> passwordEncoder.matches(password, cashier.getPassword()))
                .map(entity -> {
                    entity.setLastLogin(LocalDateTime.now());
                    return cashierMapper.entityToModel(cashierRepository.save(entity));
                });
    }

    @Override
    @Transactional
    public void logout(@NonNull Long cashierId) {
        log.debug("Logging out cashier ID: {}", cashierId);
        cashierRepository.findById(cashierId)
                .ifPresent(entity -> {
                    endShift(cashierId);
                });
    }

    @Override
    @Transactional
    @NonNull
    public CashierAccount updateCashier(@NonNull CashierAccount cashier) {
        log.debug("Updating cashier: {}", cashier.getUsername());
        CashierEntity entity = cashierRepository.findById(cashier.getId())
                .orElseThrow(() -> new IllegalArgumentException("Cashier not found with ID: " + cashier.getId()));
        
        cashierMapper.updateEntity(cashier, entity);
        if (cashier.getPassword() != null && !cashier.getPassword().isEmpty()) {
            entity.setPassword(passwordEncoder.encode(cashier.getPassword()));
        }
        
        entity = cashierRepository.save(entity);
        return cashierMapper.entityToModel(entity);
    }

    @Override
    @Transactional
    public void deactivateCashier(@NonNull Long cashierId) {
        log.debug("Deactivating cashier ID: {}", cashierId);
        cashierRepository.findById(cashierId)
                .ifPresent(entity -> {
                    entity.setActive(false);
                    cashierRepository.save(entity);
                });
    }

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public List<CashierAccount> getAllActiveCashiers() {
        log.debug("Retrieving all active cashiers");
        return cashierRepository.findByActiveTrue().stream()
                .map(cashierMapper::entityToModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addTip(@NonNull Long cashierId, @NonNull BigDecimal amount) {
        log.debug("Adding tip amount {} to cashier ID: {}", amount, cashierId);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Tip amount must be positive");
        }
        
        CashierEntity cashier = cashierRepository.findById(cashierId)
                .orElseThrow(() -> new IllegalArgumentException("Cashier not found with ID: " + cashierId));
        
        BigDecimal newBalance = cashier.getTipBalance().add(amount);
        cashier.setTipBalance(newBalance);
        cashierRepository.save(cashier);
    }

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public BigDecimal getTipBalance(@NonNull Long cashierId) {
        return cashierRepository.findById(cashierId)
                .map(CashierEntity::getTipBalance)
                .orElseThrow(() -> new IllegalArgumentException("Cashier not found with ID: " + cashierId));
    }

    @Override
    @Transactional
    public void withdrawTips(@NonNull Long cashierId, @NonNull BigDecimal amount) {
        log.debug("Withdrawing tip amount {} for cashier ID: {}", amount, cashierId);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        
        CashierEntity cashier = cashierRepository.findById(cashierId)
                .orElseThrow(() -> new IllegalArgumentException("Cashier not found with ID: " + cashierId));
        
        if (cashier.getTipBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient tip balance");
        }
        
        BigDecimal newBalance = cashier.getTipBalance().subtract(amount);
        cashier.setTipBalance(newBalance);
        cashierRepository.save(cashier);
    }

    @Override
    @Transactional
    public void startShift(@NonNull Long cashierId) {
        log.debug("Starting shift for cashier ID: {}", cashierId);
        CashierEntity cashier = cashierRepository.findById(cashierId)
                .orElseThrow(() -> new IllegalArgumentException("Cashier not found with ID: " + cashierId));
        
        if (cashier.getShiftStartTime() != null && cashier.getShiftEndTime() == null) {
            throw new IllegalStateException("Shift already started");
        }
        
        cashier.setShiftStartTime(LocalDateTime.now());
        cashier.setShiftEndTime(null);
        cashierRepository.save(cashier);
    }

    @Override
    @Transactional
    public void endShift(@NonNull Long cashierId) {
        log.debug("Ending shift for cashier ID: {}", cashierId);
        CashierEntity cashier = cashierRepository.findById(cashierId)
                .orElseThrow(() -> new IllegalArgumentException("Cashier not found with ID: " + cashierId));
        
        if (cashier.getShiftStartTime() == null || cashier.getShiftEndTime() != null) {
            throw new IllegalStateException("No active shift to end");
        }
        
        cashier.setShiftEndTime(LocalDateTime.now());
        cashierRepository.save(cashier);
    }

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public Optional<CashierAccount> getCurrentlyLoggedInCashier(@NonNull Long posId) {
        log.debug("Checking for currently logged in cashier at POS ID: {}", posId);
        return cashierRepository.findByLastLoginIsNotNullAndShiftEndTimeIsNull()
                .map(cashierMapper::entityToModel);
    }
}
package de.seuhd.campuscoffee.data.impl;

import de.seuhd.campuscoffee.domain.model.CashierAccount;
import de.seuhd.campuscoffee.domain.ports.CashierDataPort;
import de.seuhd.campuscoffee.data.persistence.CashierEntity;
import de.seuhd.campuscoffee.data.persistence.CashierRepository;
import de.seuhd.campuscoffee.data.mapper.CashierEntityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class CashierDataPortImpl implements CashierDataPort {

    private final CashierRepository cashierRepository;
    private final CashierEntityMapper cashierMapper;

    @Override
    public CashierAccount save(CashierAccount cashier) {
        log.debug("Saving cashier: {}", cashier.getUsername());
        CashierEntity entity = cashierMapper.modelToEntity(cashier);
        entity = cashierRepository.save(entity);
        return cashierMapper.entityToModel(entity);
    }

    @Override
    public Optional<CashierAccount> findById(Long id) {
        log.debug("Finding cashier by ID: {}", id);
        return cashierRepository.findById(id)
                .map(cashierMapper::entityToModel);
    }

    @Override
    public Optional<CashierAccount> findByUsername(String username) {
        log.debug("Finding cashier by username: {}", username);
        return cashierRepository.findByUsername(username)
                .map(cashierMapper::entityToModel);
    }

    @Override
    public List<CashierAccount> findByActiveTrue() {
        log.debug("Finding all active cashiers");
        return cashierRepository.findByActiveTrue().stream()
                .map(cashierMapper::entityToModel)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Deleting cashier with ID: {}", id);
        cashierRepository.deleteById(id);
    }
}

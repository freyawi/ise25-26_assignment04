package de.seuhd.campuscoffee.data.impl;

import de.seuhd.campuscoffee.data.mapper.SaleEntityMapper;
import de.seuhd.campuscoffee.data.persistence.SaleEntity;
import de.seuhd.campuscoffee.data.persistence.SaleRepository;
import de.seuhd.campuscoffee.domain.model.Sale;
import de.seuhd.campuscoffee.domain.model.SaleStatus;
import de.seuhd.campuscoffee.domain.ports.SaleDataPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleDataPortImpl implements SaleDataPort {
    private final SaleRepository saleRepository;
    private final SaleEntityMapper saleEntityMapper;

    @Override
    @NonNull
    public Sale save(@NonNull Sale sale) {
        SaleEntity entity = saleEntityMapper.modelToEntity(sale);
        entity = saleRepository.save(entity);
        return saleEntityMapper.entityToModel(entity);
    }

    @Override
    @NonNull
    public Optional<Sale> findById(@NonNull Long id) {
        return saleRepository.findById(id)
                .map(saleEntityMapper::entityToModel);
    }

    @Override
    @NonNull
    public List<Sale> findByTimestampBetween(@NonNull LocalDateTime start, @NonNull LocalDateTime end) {
        return saleRepository.findByTimestampBetween(start, end).stream()
                .map(saleEntityMapper::entityToModel)
                .collect(Collectors.toList());
    }

    @Override
    @NonNull
    public List<Sale> findByCashierId(@NonNull Long cashierId) {
        return saleRepository.findByCashierId(cashierId).stream()
                .map(saleEntityMapper::entityToModel)
                .collect(Collectors.toList());
    }

    @Override
    @NonNull
    public List<Sale> findByStatus(@NonNull SaleStatus status) {
        return saleRepository.findByStatus(status).stream()
                .map(saleEntityMapper::entityToModel)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(@NonNull Long id) {
        saleRepository.deleteById(id);
    }
}

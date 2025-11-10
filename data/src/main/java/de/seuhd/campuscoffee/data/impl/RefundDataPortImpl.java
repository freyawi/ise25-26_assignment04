package de.seuhd.campuscoffee.data.impl;

import de.seuhd.campuscoffee.domain.model.Refund;
import de.seuhd.campuscoffee.domain.model.Sale;
import de.seuhd.campuscoffee.domain.ports.RefundDataPort;
import de.seuhd.campuscoffee.data.persistence.RefundEntity;
import de.seuhd.campuscoffee.data.persistence.SaleEntity;
import de.seuhd.campuscoffee.data.persistence.RefundRepository;
import de.seuhd.campuscoffee.data.persistence.SaleRepository;
import de.seuhd.campuscoffee.data.mapper.RefundEntityMapper;
import de.seuhd.campuscoffee.data.mapper.SaleEntityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefundDataPortImpl implements RefundDataPort {

    private final RefundRepository refundRepository;
    private final SaleRepository saleRepository;
    private final RefundEntityMapper refundMapper;
    private final SaleEntityMapper saleMapper;

    @Override
    public Refund save(Refund refund) {
        log.debug("Saving refund with receipt number: {}", refund.getReceiptNumber());
        RefundEntity entity = refundMapper.modelToEntity(refund);
        entity = refundRepository.save(entity);
        return refundMapper.entityToModel(entity);
    }

    @Override
    public Optional<Refund> findById(Long id) {
        log.debug("Finding refund by ID: {}", id);
        return refundRepository.findById(id)
                .map(refundMapper::entityToModel);
    }

    @Override
    public List<Refund> findByTimestampBetween(LocalDateTime start, LocalDateTime end) {
        log.debug("Finding refunds between {} and {}", start, end);
        return refundRepository.findByTimestampBetween(start, end).stream()
                .map(refundMapper::entityToModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Refund> findByCashierId(Long cashierId) {
        log.debug("Finding refunds for cashier ID: {}", cashierId);
        return refundRepository.findByCashierId(cashierId).stream()
                .map(refundMapper::entityToModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Sale> findSaleById(Long saleId) {
        log.debug("Finding sale by ID: {}", saleId);
        return saleRepository.findById(saleId)
                .map(saleMapper::entityToModel);
    }

    @Override
    public void saveSale(Sale sale) {
        log.debug("Saving sale with ID: {}", sale.getId());
        SaleEntity entity = saleMapper.modelToEntity(sale);
        saleRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Deleting refund with ID: {}", id);
        refundRepository.deleteById(id);
    }
}

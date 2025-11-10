package de.seuhd.campuscoffee.domain.impl;

import de.seuhd.campuscoffee.domain.model.Refund;
import de.seuhd.campuscoffee.domain.model.PaymentMethod;
import de.seuhd.campuscoffee.domain.model.SaleStatus;
import de.seuhd.campuscoffee.domain.ports.RefundService;
import de.seuhd.campuscoffee.data.persistence.*;
import de.seuhd.campuscoffee.data.mapper.RefundEntityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefundServiceImpl implements RefundService {
    private final RefundRepository refundRepository;
    private final SaleRepository saleRepository;
    private final RefundEntityMapper refundMapper;

    @Override
    @Transactional
    @NonNull
    public Refund createRefund(@NonNull Long saleId, @NonNull BigDecimal amount, 
                              @NonNull PaymentMethod refundMethod, @NonNull Long cashierId,
                              @NonNull String reason) {
        log.debug("Creating refund for sale ID: {} by cashier: {}", saleId, cashierId);
        
        SaleEntity sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new IllegalArgumentException("Sale not found with ID: " + saleId));
        
        if (sale.getStatus() != SaleStatus.COMPLETED) {
            throw new IllegalStateException("Can only refund completed sales");
        }
        
        if (amount.compareTo(sale.getTotal()) > 0) {
            throw new IllegalArgumentException("Refund amount cannot be greater than sale total");
        }
        
        RefundEntity refund = new RefundEntity();
        refund.setOriginalSaleId(saleId);
        refund.setAmount(amount);
        refund.setRefundMethod(refundMethod);
        refund.setCashierId(cashierId);
        refund.setReason(reason);
        refund.setReceiptNumber(generateRefundReceiptNumber());
        
        sale.setStatus(SaleStatus.REFUNDED);
        saleRepository.save(sale);
        
        refund = refundRepository.save(refund);
        return refundMapper.entityToModel(refund);
    }

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public Refund getRefund(@NonNull Long id) {
        log.debug("Retrieving refund with ID: {}", id);
        return refundRepository.findById(id)
                .map(refundMapper::entityToModel)
                .orElseThrow(() -> new IllegalArgumentException("Refund not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public List<Refund> getRefundsByDateRange(@NonNull LocalDateTime start, @NonNull LocalDateTime end) {
        log.debug("Retrieving refunds between {} and {}", start, end);
        return refundRepository.findByTimestampBetween(start, end).stream()
                .map(refundMapper::entityToModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public List<Refund> getRefundsByCashier(@NonNull Long cashierId) {
        log.debug("Retrieving refunds for cashier ID: {}", cashierId);
        return refundRepository.findByCashierId(cashierId).stream()
                .map(refundMapper::entityToModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public List<Refund> getDailyRefunds(@NonNull LocalDateTime date) {
        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        return getRefundsByDateRange(startOfDay, endOfDay);
    }

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public BigDecimal getTotalRefundsForDay(@NonNull LocalDateTime date) {
        return getDailyRefunds(date).stream()
                .map(Refund::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String generateRefundReceiptNumber() {
        return "R-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
package de.seuhd.campuscoffee.domain.impl;

import de.seuhd.campuscoffee.domain.model.*;
import de.seuhd.campuscoffee.domain.ports.SaleService;
import de.seuhd.campuscoffee.domain.ports.SaleDataPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {
    private final SaleDataPort saleDataPort;

    @Override
    @Transactional
    @NonNull
    public Sale createSale(@NonNull Sale sale) {
        log.debug("Creating new sale: {}", sale);
        sale.setStatus(SaleStatus.IN_PROGRESS);
        return saleDataPort.save(sale);
    }

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public Sale getSale(@NonNull Long id) {
        log.debug("Retrieving sale with ID: {}", id);
        return saleDataPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sale not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public List<Sale> getSalesByDateRange(@NonNull LocalDateTime start, @NonNull LocalDateTime end) {
        log.debug("Retrieving sales between {} and {}", start, end);
        return saleDataPort.findByTimestampBetween(start, end);
    }

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public List<Sale> getSalesByCashier(@NonNull Long cashierId) {
        log.debug("Retrieving sales for cashier ID: {}", cashierId);
        return saleDataPort.findByCashierId(cashierId);
    }

    @Override
    @NonNull
    public String generateReceiptNumber() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    @Transactional
    public void voidSale(@NonNull Long saleId, @NonNull String reason, @NonNull Long cashierId) {
        log.debug("Voiding sale ID: {} by cashier: {}", saleId, cashierId);
        Sale sale = saleDataPort.findById(saleId)
                .orElseThrow(() -> new IllegalArgumentException("Sale not found with ID: " + saleId));
        
        if (sale.getStatus() != SaleStatus.IN_PROGRESS) {
            throw new IllegalStateException("Cannot void sale that is not in progress");
        }
        
        sale.setStatus(SaleStatus.VOIDED);
        sale.setNotes(reason);
        saleDataPort.save(sale);
    }

    @Override
    @Transactional
    @NonNull
    public CartItem addItemToCart(@NonNull Long saleId, @NonNull CartItem item) {
        log.debug("Adding item to sale ID: {}", saleId);
        Sale sale = saleDataPort.findById(saleId)
                .orElseThrow(() -> new IllegalArgumentException("Sale not found with ID: " + saleId));
        
        if (sale.getStatus() != SaleStatus.IN_PROGRESS) {
            throw new IllegalStateException("Cannot modify sale that is not in progress");
        }

        // Calculate totals and add item
        BigDecimal itemTotal = calculateItemTotal(item);
        item.setTotal(itemTotal);
        sale.getItems().add(item);
        
        // Update sale totals
        updateSaleTotals(sale);
        saleDataPort.save(sale);
        
        return item;
    }

    @Override
    @Transactional
    public void removeItemFromCart(@NonNull Long saleId, @NonNull Long itemId) {
        log.debug("Removing item {} from sale {}", itemId, saleId);
        Sale sale = saleDataPort.findById(saleId)
                .orElseThrow(() -> new IllegalArgumentException("Sale not found with ID: " + saleId));
        
        if (sale.getStatus() != SaleStatus.IN_PROGRESS) {
            throw new IllegalStateException("Cannot modify sale that is not in progress");
        }
        
        sale.getItems().removeIf(item -> item.getId().equals(itemId));
        updateSaleTotals(sale);
        saleDataPort.save(sale);
    }

    @Override
    @Transactional
    public void clearCart(@NonNull Long saleId) {
        log.debug("Clearing cart for sale ID: {}", saleId);
        Sale sale = saleDataPort.findById(saleId)
                .orElseThrow(() -> new IllegalArgumentException("Sale not found with ID: " + saleId));
        
        if (sale.getStatus() != SaleStatus.IN_PROGRESS) {
            throw new IllegalStateException("Cannot modify sale that is not in progress");
        }
        
        sale.getItems().clear();
        updateSaleTotals(sale);
        saleDataPort.save(sale);
    }

    @Override
    @Transactional
    @NonNull
    public Sale applySaleDiscount(@NonNull Long saleId, @NonNull BigDecimal discount) {
        log.debug("Applying discount {} to sale {}", discount, saleId);
        Sale sale = saleDataPort.findById(saleId)
                .orElseThrow(() -> new IllegalArgumentException("Sale not found with ID: " + saleId));
        
        if (discount.compareTo(sale.getSubtotal()) > 0) {
            throw new IllegalArgumentException("Discount cannot be greater than subtotal");
        }
        
        sale.setDiscount(discount);
        updateSaleTotals(sale);
        return saleDataPort.save(sale);
    }

    @Override
    @Transactional
    @NonNull
    public Sale processSalePayment(@NonNull Long saleId, @NonNull PaymentMethod method, @NonNull BigDecimal amountTendered) {
        log.debug("Processing payment for sale {} with method {}", saleId, method);
        Sale sale = saleDataPort.findById(saleId)
                .orElseThrow(() -> new IllegalArgumentException("Sale not found with ID: " + saleId));
        
        if (sale.getStatus() != SaleStatus.IN_PROGRESS) {
            throw new IllegalStateException("Cannot process payment for sale that is not in progress");
        }
        
        if (amountTendered.compareTo(sale.getTotal()) < 0) {
            throw new IllegalArgumentException("Amount tendered must be greater than or equal to total");
        }
        
        sale.setPaymentMethod(method);
        sale.setCashReceived(amountTendered);
        sale.setChangeGiven(amountTendered.subtract(sale.getTotal()));
        sale.setStatus(SaleStatus.COMPLETED);
        return saleDataPort.save(sale);
    }

    @Override
    @Transactional
    @NonNull
    public Sale addTipToSale(@NonNull Long saleId, @NonNull BigDecimal tipAmount) {
        log.debug("Adding tip {} to sale {}", tipAmount, saleId);
        Sale sale = saleDataPort.findById(saleId)
                .orElseThrow(() -> new IllegalArgumentException("Sale not found with ID: " + saleId));
        
        if (sale.getStatus() != SaleStatus.IN_PROGRESS) {
            throw new IllegalStateException("Cannot add tip to sale that is not in progress");
        }
        
        sale.setTip(tipAmount);
        updateSaleTotals(sale);
        return saleDataPort.save(sale);
    }

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public List<Sale> getDailySales(@NonNull LocalDateTime date) {
        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        return getSalesByDateRange(startOfDay, endOfDay);
    }

    private void updateSaleTotals(Sale sale) {
        BigDecimal subtotal = sale.getItems().stream()
                .map(CartItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        sale.setSubtotal(subtotal);
        BigDecimal total = subtotal;
        
        if (sale.getDiscount() != null) {
            total = total.subtract(sale.getDiscount());
        }
        
        if (sale.getTip() != null) {
            total = total.add(sale.getTip());
        }
        
        sale.setTotal(total);
    }

    private BigDecimal calculateItemTotal(CartItem item) {
        BigDecimal unitTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
        if (item.getDiscount() != null) {
            return unitTotal.subtract(item.getDiscount());
        }
        return unitTotal;
    }
}
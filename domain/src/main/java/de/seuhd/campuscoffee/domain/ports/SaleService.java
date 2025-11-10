package de.seuhd.campuscoffee.domain.ports;

import de.seuhd.campuscoffee.domain.model.Sale;
import de.seuhd.campuscoffee.domain.model.CartItem;
import de.seuhd.campuscoffee.domain.model.PaymentMethod;
import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service interface for Sale operations.
 */
public interface SaleService {
    @NonNull
    Sale createSale(@NonNull Sale sale);
    
    @NonNull
    Sale getSale(@NonNull Long id);
    
    @NonNull
    List<Sale> getSalesByDateRange(@NonNull LocalDateTime start, @NonNull LocalDateTime end);
    
    @NonNull
    List<Sale> getSalesByCashier(@NonNull Long cashierId);
    
    @NonNull
    String generateReceiptNumber();
    
    void voidSale(@NonNull Long saleId, @NonNull String reason, @NonNull Long cashierId);
    
    @NonNull
    CartItem addItemToCart(@NonNull Long saleId, @NonNull CartItem item);
    
    void removeItemFromCart(@NonNull Long saleId, @NonNull Long itemId);
    
    void clearCart(@NonNull Long saleId);
    
    @NonNull
    Sale applySaleDiscount(@NonNull Long saleId, @NonNull BigDecimal discount);
    
    @NonNull
    Sale processSalePayment(@NonNull Long saleId, @NonNull PaymentMethod method, @NonNull BigDecimal amountTendered);
    
    @NonNull
    Sale addTipToSale(@NonNull Long saleId, @NonNull BigDecimal tipAmount);
    
    @NonNull
    List<Sale> getDailySales(@NonNull LocalDateTime date);
}
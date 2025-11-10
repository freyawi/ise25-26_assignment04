package de.seuhd.campuscoffee.domain.ports;

import de.seuhd.campuscoffee.domain.model.Product;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;

/**
 * Data port interface for Product persistence operations.
 * This port defines the contract for accessing product data,
 * allowing the domain layer to remain independent of persistence details.
 */
public interface ProductDataPort {
    @NonNull
    List<Product> findAll();
    
    @NonNull
    Optional<Product> findByBarcode(@NonNull String barcode);
    
    @NonNull
    Optional<Product> findById(@NonNull Long id);
    
    @NonNull
    Product save(@NonNull Product product);
    
    void deleteById(@NonNull Long id);
    
    boolean existsById(@NonNull Long id);
    
    @NonNull
    List<Product> searchByNameOrDescription(@NonNull String query);
}

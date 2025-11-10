package de.seuhd.campuscoffee.domain.ports;

import de.seuhd.campuscoffee.domain.model.Product;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Product operations.
 */
public interface ProductService {
    @NonNull
    List<Product> getAllProducts();
    
    @NonNull
    Optional<Product> findByBarcode(@NonNull String barcode);
    
    @NonNull
    Product createProduct(@NonNull Product product);
    
    @NonNull
    Product updateProduct(@NonNull Product product);
    
    void deleteProduct(@NonNull Long id);
    
    @NonNull
    List<Product> searchProducts(@NonNull String query);
    
    void updateProductAvailability(@NonNull Long productId, boolean available);
}
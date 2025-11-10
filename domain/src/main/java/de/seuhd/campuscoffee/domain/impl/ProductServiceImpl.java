package de.seuhd.campuscoffee.domain.impl;

import de.seuhd.campuscoffee.domain.model.Product;
import de.seuhd.campuscoffee.domain.ports.ProductDataPort;
import de.seuhd.campuscoffee.domain.ports.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductDataPort productDataPort;

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public List<Product> getAllProducts() {
        log.debug("Retrieving all products");
        return productDataPort.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public Optional<Product> findByBarcode(@NonNull String barcode) {
        log.debug("Finding product by barcode: {}", barcode);
        return productDataPort.findByBarcode(barcode);
    }

        @Override
    @Transactional
    @NonNull
    public Product createProduct(@NonNull Product product) {
        log.debug("Creating new product: {}", product.getBarcode());
        return productDataPort.save(product);
    }

    @Override
    @Transactional
    @NonNull
    public Product updateProduct(@NonNull Product product) {
        log.debug("Updating product: {}", product.getId());
        if (!productDataPort.existsById(product.getId())) {
            throw new IllegalArgumentException("Product not found");
        }
        return productDataPort.save(product);
    }

    @Override
    @Transactional
    public void deleteProduct(@NonNull Long id) {
        log.debug("Deleting product: {}", id);
        productDataPort.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public List<Product> searchProducts(@NonNull String query) {
        log.debug("Searching products: {}", query);
        return productDataPort.searchByNameOrDescription(query);
    }

    @Override
    @Transactional
    public void updateProductAvailability(@NonNull Long productId, boolean available) {
        log.debug("Updating product availability: {} -> {}", productId, available);
        productDataPort.findById(productId)
                .ifPresent(product -> {
                    product.setAvailable(available);
                    productDataPort.save(product);
                });
    }
}
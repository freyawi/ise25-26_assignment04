package de.seuhd.campuscoffee.domain.impl;

import de.seuhd.campuscoffee.domain.model.Product;
import de.seuhd.campuscoffee.domain.ports.ProductService;
import de.seuhd.campuscoffee.data.persistence.ProductEntity;
import de.seuhd.campuscoffee.data.persistence.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductEntityMapper productMapper;

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public List<Product> getAllProducts() {
        log.debug("Retrieving all products");
        return productRepository.findAll().stream()
                .map(productMapper::entityToModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public Optional<Product> findByBarcode(@NonNull String barcode) {
        log.debug("Finding product by barcode: {}", barcode);
        return productRepository.findByBarcode(barcode)
                .map(productMapper::entityToModel);
    }

    @Override
    @Transactional
    @NonNull
    public Product createProduct(@NonNull Product product) {
        log.debug("Creating new product: {}", product);
        ProductEntity entity = productMapper.modelToEntity(product);
        entity = productRepository.save(entity);
        return productMapper.entityToModel(entity);
    }

    @Override
    @Transactional
    @NonNull
    public Product updateProduct(@NonNull Product product) {
        log.debug("Updating product: {}", product);
        if (product.getId() == null) {
            throw new IllegalArgumentException("Product ID must not be null for update");
        }
        
        if (!productRepository.existsById(product.getId())) {
            throw new IllegalArgumentException("Product not found with ID: " + product.getId());
        }
        
        ProductEntity entity = productMapper.modelToEntity(product);
        entity = productRepository.save(entity);
        return productMapper.entityToModel(entity);
    }

    @Override
    @Transactional
    public void deleteProduct(@NonNull Long id) {
        log.debug("Deleting product with ID: {}", id);
        productRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    @NonNull
    public List<Product> searchProducts(@NonNull String query) {
        log.debug("Searching products with query: {}", query);
        return productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query)
                .stream()
                .map(productMapper::entityToModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateProductAvailability(@NonNull Long productId, boolean available) {
        log.debug("Updating product availability - ID: {}, available: {}", productId, available);
        productRepository.findById(productId)
                .map(entity -> {
                    entity.setAvailable(available);
                    return productRepository.save(entity);
                })
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));
    }
}
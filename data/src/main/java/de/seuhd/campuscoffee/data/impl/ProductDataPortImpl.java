package de.seuhd.campuscoffee.data.impl;

import de.seuhd.campuscoffee.data.mapper.ProductEntityMapper;
import de.seuhd.campuscoffee.data.persistence.ProductEntity;
import de.seuhd.campuscoffee.data.persistence.ProductRepository;
import de.seuhd.campuscoffee.domain.model.Product;
import de.seuhd.campuscoffee.domain.ports.ProductDataPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductDataPortImpl implements ProductDataPort {
    private final ProductRepository productRepository;
    private final ProductEntityMapper productEntityMapper;

    @Override
    @NonNull
    public List<Product> findAll() {
        return productRepository.findAll().stream()
                .map(productEntityMapper::entityToModel)
                .collect(Collectors.toList());
    }

    @Override
    @NonNull
    public Optional<Product> findByBarcode(@NonNull String barcode) {
        return productRepository.findByBarcode(barcode)
                .map(productEntityMapper::entityToModel);
    }

    @Override
    @NonNull
    public Optional<Product> findById(@NonNull Long id) {
        return productRepository.findById(id)
                .map(productEntityMapper::entityToModel);
    }

    @Override
    @NonNull
    public Product save(@NonNull Product product) {
        ProductEntity entity = productEntityMapper.modelToEntity(product);
        entity = productRepository.save(entity);
        return productEntityMapper.entityToModel(entity);
    }

    @Override
    public void deleteById(@NonNull Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public boolean existsById(@NonNull Long id) {
        return productRepository.existsById(id);
    }

    @Override
    @NonNull
    public List<Product> searchByNameOrDescription(@NonNull String query) {
        return productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query)
                .stream()
                .map(productEntityMapper::entityToModel)
                .collect(Collectors.toList());
    }
}

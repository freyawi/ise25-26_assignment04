package de.seuhd.campuscoffee.data.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findByBarcode(String barcode);
    List<ProductEntity> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);
    List<ProductEntity> findByAvailableTrue();
}